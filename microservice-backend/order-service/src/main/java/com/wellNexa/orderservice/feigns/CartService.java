package com.wellNexa.orderservice.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.wellNexa.orderservice.dtos.ApiResponseDto;
import com.wellNexa.orderservice.dtos.CartDto;

@FeignClient("CART-SERVICE")
public interface CartService {

    @GetMapping("/cart/get/byId")
    ResponseEntity<ApiResponseDto<CartDto>> getCartById(@RequestParam String id, @RequestHeader("Authorization") String token);

    @DeleteMapping("/cart/clear/byId")
    ResponseEntity<ApiResponseDto<?>> clearCartById(@RequestParam String id, @RequestHeader("Authorization") String token);

}
