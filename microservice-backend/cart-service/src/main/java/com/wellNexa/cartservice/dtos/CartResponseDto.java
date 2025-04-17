package com.wellNexa.cartservice.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Builder
public class CartResponseDto {

    private String cartId;
    private String userId;
    @Builder.Default
    private Set<CartItemResponseDto> cartItems = ConcurrentHashMap.newKeySet();
    private int noOfCartItems;
    private double subtotal;
}
