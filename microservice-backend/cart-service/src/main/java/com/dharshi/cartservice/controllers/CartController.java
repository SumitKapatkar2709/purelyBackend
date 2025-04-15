package com.dharshi.cartservice.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dharshi.cartservice.dtos.ApiResponseDto;
import com.dharshi.cartservice.dtos.CartItemRequestDto;
import com.dharshi.cartservice.exceptions.ResourceNotFoundException;
import com.dharshi.cartservice.exceptions.ServiceLogicException;
import com.dharshi.cartservice.services.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<ApiResponseDto<?>> addItemToCart(Authentication authentication, @RequestBody CartItemRequestDto requestDto)
            throws ResourceNotFoundException, ServiceLogicException{
    	System.out.println("RequestForm....."+requestDto);
        return cartService.addItemToCart(authentication.getPrincipal().toString(), requestDto);
    }

    @GetMapping("/get/byUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<ApiResponseDto<?>> getCartItemsByUser(Authentication authentication)
            throws ResourceNotFoundException, ServiceLogicException{
        return cartService.getCartItemsByUser(authentication.getPrincipal().toString());
    }
    
    @GetMapping("/getwaitlist/byUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<ApiResponseDto<?>> getWaitlistItemsByUser(Authentication authentication)
            throws ResourceNotFoundException, ServiceLogicException{
        return cartService.getWaitlistItemsByUser(authentication.getPrincipal().toString());
    }

    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<ApiResponseDto<?>> removeCartItemFromCart(Authentication authentication, @RequestParam String productId)
            throws ServiceLogicException, ResourceNotFoundException {
        return cartService.removeCartItemFromCart(authentication.getPrincipal().toString(), productId);
    }
    
    @PutMapping("/removewishlist")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponseDto<?>> removeWishlistItem(
            Authentication authentication,
            @RequestBody Map<String, String> body
    ) throws ServiceLogicException, ResourceNotFoundException {
        String productId = body.get("productId");
        System.out.println("wishlist " + productId);
        return cartService.removeWishlistItem(authentication.getPrincipal().toString(), productId);
    }

    @GetMapping("/get/byId")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<ApiResponseDto<?>> getCartById(@RequestParam String id) throws ServiceLogicException {
        return cartService.getCartById(id);
    }

    @DeleteMapping("/clear/byId")
    @PreAuthorize("hasRole('ROLE_USER')")
    ResponseEntity<ApiResponseDto<?>> clearCartById(@RequestParam String id) throws ResourceNotFoundException, ServiceLogicException {
        return cartService.clearCartById(id);
    }

}

