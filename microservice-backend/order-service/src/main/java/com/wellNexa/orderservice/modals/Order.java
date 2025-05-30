package com.wellNexa.orderservice.modals;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.wellNexa.orderservice.dtos.CartItemDto;
import com.wellNexa.orderservice.enums.EOrderPaymentStatus;
import com.wellNexa.orderservice.enums.EOrderStatus;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String userId;

    private String firstName;

    private String lastName;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String phoneNo;

    private double orderAmt;

    private LocalDateTime placedOn;

    private EOrderStatus orderStatus;

    private EOrderPaymentStatus paymentStatus;

    private Set<CartItemDto> orderItems;

}
