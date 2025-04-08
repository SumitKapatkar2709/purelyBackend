package com.dharshi.cartservice.controllers;

import com.dharshi.cartservice.dtos.ApiResponseDto;
import com.dharshi.cartservice.dtos.CartItemRequestDto;
import com.dharshi.cartservice.dtos.PaymentRequestDto;
import com.dharshi.cartservice.exceptions.ResourceNotFoundException;
import com.dharshi.cartservice.exceptions.ServiceLogicException;
import com.dharshi.cartservice.services.CartService;
import com.dharshi.cartservice.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5173")  // Allow frontend requests
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private PaymentService paymentService;  // Inject PaymentService

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> addItemToCart(Authentication authentication, @RequestBody CartItemRequestDto requestDto)
            throws ResourceNotFoundException, ServiceLogicException {
        return cartService.addItemToCart(authentication.getPrincipal().toString(), requestDto);
    }

    @GetMapping("/get/byUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> getCartItemsByUser(Authentication authentication)
            throws ResourceNotFoundException, ServiceLogicException {
        return cartService.getCartItemsByUser(authentication.getPrincipal().toString());
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> removeCartItemFromCart(Authentication authentication, @RequestParam String productId)
            throws ServiceLogicException, ResourceNotFoundException {
        return cartService.removeCartItemFromCart(authentication.getPrincipal().toString(), productId);
    }

    @GetMapping("/get/byId")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> getCartById(@RequestParam String id) throws ServiceLogicException {
        return cartService.getCartById(id);
    }

    @DeleteMapping("/clear/byId")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> clearCartById(@RequestParam String id) throws ResourceNotFoundException, ServiceLogicException {
        return cartService.clearCartById(id);
    }

    // ✅ Added checkout API from PaymentController
    @PostMapping("/checkout")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> createCheckoutSession(Authentication authentication,
                                                                   @RequestBody PaymentRequestDto paymentRequestDto)
            throws ResourceNotFoundException, ServiceLogicException {
        String userId = authentication.getPrincipal().toString();
        return paymentService.createCheckoutSession(userId, paymentRequestDto);
    }
}
