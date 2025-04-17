package com.wellNexa.cartservice.services;

import org.springframework.http.ResponseEntity;

import com.wellNexa.cartservice.dtos.ApiResponseDto;
import com.wellNexa.cartservice.dtos.CartItemRequestDto;
import com.wellNexa.cartservice.exceptions.ResourceNotFoundException;
import com.wellNexa.cartservice.exceptions.ServiceLogicException;

public interface CartService {
    ResponseEntity<ApiResponseDto<?>> addItemToCart(String userId, CartItemRequestDto requestDto) throws ResourceNotFoundException, ServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> getCartItemsByUser(String userId) throws ResourceNotFoundException, ServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> removeCartItemFromCart(String userId, String productId) throws ServiceLogicException, ResourceNotFoundException;
    ResponseEntity<ApiResponseDto<?>> clearCartById(String id) throws ServiceLogicException, ResourceNotFoundException;
    ResponseEntity<ApiResponseDto<?>> getCartById(String id) throws ServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> getWaitlistItemsByUser(String userId) throws ResourceNotFoundException, ServiceLogicException;
ResponseEntity<ApiResponseDto<?>> removeWishlistItem(String userId, String productId) throws ServiceLogicException, ResourceNotFoundException;
}

