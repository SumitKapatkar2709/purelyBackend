package com.wellNexa.cartservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CartItemRequestDto {
    private String productId;
    private int quantity;
    private boolean wishlist;
}
