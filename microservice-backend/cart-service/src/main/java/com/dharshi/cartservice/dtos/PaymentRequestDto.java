package com.dharshi.cartservice.dtos;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private String userId;
    private double amount;
    private String description;
    private String successUrl;
    private String cancelUrl;
}
