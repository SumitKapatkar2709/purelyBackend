package com.wellNexa.cartservice.services;

import com.wellNexa.cartservice.dtos.*;
import com.wellNexa.cartservice.exceptions.ResourceNotFoundException;
import com.wellNexa.cartservice.exceptions.ServiceLogicException;
import com.wellNexa.cartservice.feigns.ProductService;
import com.wellNexa.cartservice.feigns.UserService;
import com.wellNexa.cartservice.modals.Cart;
import com.wellNexa.cartservice.modals.CartItem;
import com.wellNexa.cartservice.repositories.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<ApiResponseDto<?>> addItemToCart(String userId, CartItemRequestDto requestDto) throws ResourceNotFoundException, ServiceLogicException {
        try {
            if (!Objects.requireNonNull(userService.existsUserById(userId).getBody()).getResponse()) {
                throw new ResourceNotFoundException("User not found with id " + userId);
            }
            if (Objects.requireNonNull(productService.getProductById(requestDto.getProductId()).getBody()).getResponse()==null) {
                throw new ResourceNotFoundException("Product not found with id " + requestDto.getProductId());
            }

            Cart userCart = getCart(userId);
            Set<CartItem> userCartItems = userCart.getCartItems();
            CartItem cartItem = createCartItem(userCartItems, requestDto);

            userCartItems.add(cartItem);
            userCart.setCartItems(userCartItems);

            cartRepository.save(userCart);

            return ResponseEntity.ok(
                    ApiResponseDto.builder()
                            .isSuccess(true)
                            .message("Item successfully added to cart!")
                            .build()
            );
        }catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }catch (Exception e) {
            log.error("Failed to add item to cart: " + e.getMessage());
            throw new ServiceLogicException("Unable to add item to cart!");
        }
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getCartItemsByUser(String userId) throws ResourceNotFoundException, ServiceLogicException {
        try {
            if (Objects.requireNonNull(userService.existsUserById(userId).getBody()).getResponse()) {
                
                if (!cartRepository.existsByUserId(userId)) {
                    createAndSaveNewCart(userId);
                }

                Cart userCart = getCart(userId);

                Set<CartItem> filteredCartItems = userCart.getCartItems().stream()
                        .filter(item -> !item.isWishlist() || item.getQuantity() > 0)
                        .collect(Collectors.toSet());

                userCart.setCartItems(filteredCartItems); // temporarily update the cart to filtered set

                CartResponseDto cartResponse = cartToCartResponseDto(userCart);

                return ResponseEntity.ok(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .response(cartResponse)
                                .build()
                );
            }
        } catch (Exception e) {
            log.error("Failed to find cart: " + e.getMessage());
            throw new ServiceLogicException("Unable to find cart!");
        }

        throw new ResourceNotFoundException("User not found with id " + userId);
    }
    
    
    @Override
    public ResponseEntity<ApiResponseDto<?>> getWaitlistItemsByUser(String userId) throws ResourceNotFoundException, ServiceLogicException {
        try {
            if (Objects.requireNonNull(userService.existsUserById(userId).getBody()).getResponse()) {
                
                if (!cartRepository.existsByUserId(userId)) {
                    createAndSaveNewCart(userId);
                }

                Cart userCart = getCart(userId);

                Set<CartItem> filteredCartItems = userCart.getCartItems().stream()
                        .filter(item -> item.isWishlist() )
                        .collect(Collectors.toSet());

                userCart.setCartItems(filteredCartItems); // temporarily update the cart to filtered set

                CartResponseDto cartResponse = cartToCartResponseDto(userCart);

                return ResponseEntity.ok(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .response(cartResponse)
                                .build()
                );
            }
        } catch (Exception e) {
            log.error("Failed to find cart: " + e.getMessage());
            throw new ServiceLogicException("Unable to find cart!");
        }

        throw new ResourceNotFoundException("User not found with id " + userId);
    }


    @Override
    public ResponseEntity<ApiResponseDto<?>> removeCartItemFromCart(String userId, String productId) throws ServiceLogicException, ResourceNotFoundException {
        try {
            if(cartRepository.existsByUserId(userId)) {
                Cart userCart = cartRepository.findByUserId(userId);
                Set<CartItem> removedItemsSet = removeCartItem(userCart.getCartItems(), productId);
                userCart.setCartItems(removedItemsSet);
                cartRepository.save(userCart);

                return ResponseEntity.ok(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Item successfully removed to cart!")
                                .build()
                );

            }

        }catch (Exception e) {
            log.error("Failed to add item to cart: " + e.getMessage());
            throw new ServiceLogicException("Unable to add item to cart!");
        }
        throw new ResourceNotFoundException("No cart found for user " + userId);
    }
    
    @Override
    public ResponseEntity<ApiResponseDto<?>> removeWishlistItem(String userId, String productId)
            throws ServiceLogicException, ResourceNotFoundException {
        try {
            if (cartRepository.existsByUserId(userId)) {
                Cart userCart = cartRepository.findByUserId(userId);
                boolean updated = false;

                Set<CartItem> cartItems = userCart.getCartItems();
                for (CartItem item : cartItems) {
                    if (item.getProductId().equals(productId) && item.isWishlist()) {
                        item.setWishlist(false); // ✅ Just set wishlist to false
                        updated = true;
                        break;
                    }
                }

                if (!updated) {
                    throw new ResourceNotFoundException("Wishlist item not found: " + productId);
                }

                userCart.setCartItems(cartItems);
                cartRepository.save(userCart);

                return ResponseEntity.ok(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Item successfully removed from wishlist!")
                                .build()
                );
            }
        } catch (Exception e) {
            log.error("Failed to remove item from wishlist: " + e.getMessage());
            throw new ServiceLogicException("Unable to remove item from wishlist!");
        }

        throw new ResourceNotFoundException("No cart found for user " + userId);
    }



    @Override
    public ResponseEntity<ApiResponseDto<?>> clearCartById(String id) throws ServiceLogicException, ResourceNotFoundException {
        try {
            if(cartRepository.existsById(id)) {
                Cart userCart = cartRepository.findById(id).orElse(null);
                userCart.setCartItems(new HashSet<>());
                cartRepository.save(userCart);

                return ResponseEntity.ok(
                        ApiResponseDto.builder()
                                .isSuccess(true)
                                .message("Cart has been successfully cleared!")
                                .build()
                );

            }

        }catch (Exception e) {
            log.error("Failed to add item to cart: " + e.getMessage());
            throw new ServiceLogicException("Unable to add item to cart!");
        }
        throw new ResourceNotFoundException("No cart found for id " + id);
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getCartById(String id) throws ServiceLogicException {
        try {

            Cart cart = cartRepository.findById(id).orElse(null);
            CartResponseDto cartResponse = cartToCartResponseDto(cart);

            return ResponseEntity.ok(
                    ApiResponseDto.builder()
                            .isSuccess(true)
                            .message("Cart received successfully!")
                            .response(cartResponse)
                            .build()
            );

        }catch (Exception e) {
            log.error("Failed to find cart: " + e.getMessage());
            throw new ServiceLogicException("Unable to find cart!");
        }
    }

    private void createAndSaveNewCart(String userId) {
        if(!cartRepository.existsByUserId(userId)) {
            Cart cart = Cart.builder()
                    .userId(userId)
                    .cartItems(new HashSet<>())
                    .build();
            cartRepository.insert(cart);
        }
    }

    private CartItem getNewCartItem(CartItemRequestDto requestDto) {
        return CartItem.builder()
                .productId(requestDto.getProductId())
                .quantity(1)
                .wishlist(requestDto.isWishlist()) // 👈 important!
                .build();
    }

    private CartItem getExistingCartItem(Set<CartItem> userCartItems, String productId) {
        List<CartItem> existingCartItems = userCartItems.stream().filter(item -> item.getProductId().equals(productId)).toList();
        if (existingCartItems.isEmpty()){
            return null;
        }
        return existingCartItems.get(0);
    }

    private Cart getCart(String userId) {
        //  if cart is not already present create new cart
        createAndSaveNewCart(userId);
        return cartRepository.findByUserId(userId);
    }

    private CartItem createCartItem(Set<CartItem> userCartItems, CartItemRequestDto requestDto) {
        CartItem cartItem = getExistingCartItem(userCartItems, requestDto.getProductId());

        if (cartItem == null) {
            cartItem = getNewCartItem(requestDto);

            // Don't set quantity if it's wishlist
            if (requestDto.isWishlist()) {
                cartItem.setQuantity(0); // Or you could choose to set 1 or skip this
            } else {
                cartItem.setQuantity(1); // Default quantity when added to cart
            }

        } else {
            userCartItems.remove(cartItem);

            boolean wasWishlist = cartItem.isWishlist();
            cartItem.setWishlist(requestDto.isWishlist());

            // Only modify quantity if it's being added to cart (i.e., not wishlist)
            if (!requestDto.isWishlist()) {
                int quantityToAdd = requestDto.getQuantity();

                // Normalize quantity input
                if (quantityToAdd <= 0) quantityToAdd = -1;
                else quantityToAdd = 1;

                // Don't let quantity go negative
                int newQuantity = cartItem.getQuantity() + quantityToAdd;
                cartItem.setQuantity(Math.max(newQuantity, 0));
            }

            // If moved to wishlist, just retain the existing quantity without touching it
        }

        return cartItem;
    }



    private CartResponseDto cartToCartResponseDto(Cart userCart) {
        int noOfCartItems = 0;
        double subtotal = 0.0;

        Set<CartItemResponseDto> cartItems = new HashSet<>();
        for (CartItem cartItem: userCart.getCartItems()) {
            CartItemResponseDto cartItemResponse = cartItemToCartItemResponseDto(cartItem);
            noOfCartItems += cartItemResponse.getQuantity();
            subtotal += cartItemResponse.getAmount();
            cartItems.add(cartItemResponse);
        }

        return CartResponseDto.builder()
                .cartId(userCart.getId())
                .userId(userCart.getUserId())
                .cartItems(cartItems)
                .noOfCartItems(noOfCartItems)
                .subtotal(subtotal)
                .build();
    }

    private CartItemResponseDto cartItemToCartItemResponseDto(CartItem cartItem) {
        ProductDto product = productService.getProductById(cartItem.getProductId()).getBody().getResponse();

        return CartItemResponseDto.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .quantity(cartItem.getQuantity())
                .categoryName(product.getCategoryName())
                .imageUrl(product.getImageUrl())
                .amount(product.getPrice() * cartItem.getQuantity())
                .build();
    }

    private Set<CartItem> removeCartItem(Set<CartItem> userCartItems, String productId) {
        CartItem existingCartItem = getExistingCartItem(userCartItems, productId);

        if (existingCartItem != null) {
            userCartItems.remove(existingCartItem);
        }

        return userCartItems;
    }
}
