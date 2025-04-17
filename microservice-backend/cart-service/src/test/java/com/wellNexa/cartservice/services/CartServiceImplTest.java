package com.wellNexa.cartservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import com.wellNexa.cartservice.dtos.*;
import com.wellNexa.cartservice.exceptions.ResourceNotFoundException;
import com.wellNexa.cartservice.exceptions.ServiceLogicException;
import com.wellNexa.cartservice.feigns.ProductService;
import com.wellNexa.cartservice.feigns.UserService;
import com.wellNexa.cartservice.modals.Cart;
import com.wellNexa.cartservice.modals.CartItem;
import com.wellNexa.cartservice.repositories.CartRepository;
import com.wellNexa.cartservice.services.CartServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartServiceImpl cartService;

    private final String userId = "user123";
    private final String productId = "product123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartService = new CartServiceImpl(); // initializes the ExecutorService
        cartService.setCartRepository(cartRepository);
        cartService.setProductService(productService);
        cartService.setUserService(userService);
    }

    @Test
    void testAddItemToCart_Multithreaded() throws InterruptedException {
        CartItemRequestDto requestDto = CartItemRequestDto.builder()
                .productId(productId)
                .wishlist(false)
                .quantity(1)
                .build();

        when(userService.existsUserById(userId)).thenReturn(
                ResponseEntity.ok(ApiResponseDto.<Boolean>builder().response(true).build())
        );

        when(productService.getProductById(productId)).thenReturn(
                ResponseEntity.ok(ApiResponseDto.<ProductDto>builder()
                        .response(ProductDto.builder()
                                .id(productId)
                                .productName("Test")
                                .price(10.0)
                                .build())
                        .build())
        );

        Cart cart = Cart.builder()
                .id("cart123")
                .userId(userId)
                .cartItems(new HashSet<>())
                .build();

        when(cartRepository.existsByUserId(userId)).thenReturn(true);
        when(cartRepository.findByUserId(userId)).thenReturn(cart);

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int threadNum = i;
            executor.submit(() -> {
                System.out.println("Thread-" + threadNum + " started adding item to cart.");
                try {
                    cartService.addItemToCart(userId, requestDto);
                } catch (Exception e) {
                    System.out.println("Thread-" + threadNum + " exception: " + e.getMessage());
                    fail("Exception occurred: " + e.getMessage());
                } finally {
                    System.out.println("Thread-" + threadNum + " completed adding item to cart.");
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        TimeUnit.MILLISECONDS.sleep(500);

        verify(cartRepository, atLeastOnce()).save(any(Cart.class));

        assertEquals(1, cart.getCartItems().size());
        CartItem item = cart.getCartItems().iterator().next();
        assertEquals(productId, item.getProductId());
        assertTrue(item.getQuantity() >= 1);
    }

    @Test
    void testRemoveItemFromCart_Multithreaded() throws InterruptedException {
        CartItemRequestDto requestDto = CartItemRequestDto.builder()
                .productId(productId)
                .wishlist(false)
                .quantity(1)
                .build();

        when(userService.existsUserById(userId)).thenReturn(
                ResponseEntity.ok(ApiResponseDto.<Boolean>builder().response(true).build())
        );

        CartItem cartItem = CartItem.builder()
                .productId(productId)
                .quantity(1)
                .build();

        Cart cart = Cart.builder()
                .id("cart123")
                .userId(userId)
                .cartItems(new CopyOnWriteArraySet<>())
                .build();
        cart.getCartItems().add(cartItem);

        when(cartRepository.existsByUserId(userId)).thenReturn(true);
        when(cartRepository.findByUserId(userId)).thenReturn(cart);

        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int threadNum = i;
            executor.submit(() -> {
                System.out.println("Thread-" + threadNum + " started removing item from cart.");
                try {
                    cartService.removeCartItemFromCart(userId, productId);
                } catch (Exception e) {
                    System.out.println("Thread-" + threadNum + " exception: " + e.getMessage());
                    fail("Exception occurred: " + e.getMessage());
                } finally {
                    System.out.println("Thread-" + threadNum + " completed removing item from cart.");
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();
        TimeUnit.MILLISECONDS.sleep(1000);

        System.out.println("Cart items after removal: " + cart.getCartItems());

        verify(cartRepository, atLeastOnce()).save(any(Cart.class));
        verify(cartRepository, atMostOnce()).delete(any(Cart.class));
        assertTrue(cart.getCartItems().isEmpty(), "Cart should be empty after item removal");
    }

    @Test
    void testClearCart_Multithreaded() throws InterruptedException, ExecutionException {
        CartItemRequestDto requestDto = CartItemRequestDto.builder()
                .productId(productId)
                .wishlist(false)
                .quantity(1)
                .build();

        when(userService.existsUserById(userId)).thenReturn(
                ResponseEntity.ok(ApiResponseDto.<Boolean>builder().response(true).build())
        );

        CartItem cartItem1 = CartItem.builder()
                .productId("product1")
                .quantity(1)
                .build();

        CartItem cartItem2 = CartItem.builder()
                .productId("product2")
                .quantity(1)
                .build();

        Cart cart = Cart.builder()
                .id("cart123")
                .userId(userId)
                .cartItems(new HashSet<>())
                .build();
        cart.getCartItems().add(cartItem1);
        cart.getCartItems().add(cartItem2);

        when(cartRepository.existsById(userId)).thenReturn(true);
        when(cartRepository.findById(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        int threadCount = 5;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            int threadNum = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("Thread-" + threadNum + " started clearing cart.");
                try {
                    cartService.clearCartById(userId);
                } catch (Exception e) {
                    System.out.println("Thread-" + threadNum + " exception: " + e.getMessage());
                    fail("Exception occurred: " + e.getMessage());
                } finally {
                    System.out.println("Thread-" + threadNum + " completed clearing cart.");
                    latch.countDown();
                }
            }, executor);
            futures.add(future);
        }

        latch.await(5, TimeUnit.SECONDS);
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();
        TimeUnit.MILLISECONDS.sleep(500);

        verify(cartRepository, atLeastOnce()).save(any(Cart.class));
        assertEquals(0, cart.getCartItems().size());
    }
}
