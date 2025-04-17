package com.wellNexa.cartservice.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.wellNexa.cartservice.dtos.ApiResponseDto;
import com.wellNexa.cartservice.dtos.PaymentRequestDto;
import com.wellNexa.cartservice.exceptions.PaymentException;
import com.wellNexa.cartservice.modals.Payment;
import com.wellNexa.cartservice.repositories.PaymentRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository, @Value("${stripe.secret-key}") String secretKey) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = secretKey;  // Set Stripe API Key
    }

    public ResponseEntity<ApiResponseDto<?>> createCheckoutSession(String userId, PaymentRequestDto paymentRequestDto) {
        try {
            // Build Stripe session parameters
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(paymentRequestDto.getSuccessUrl())
                    .setCancelUrl(paymentRequestDto.getCancelUrl())
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("usd")
                                                    .setUnitAmount((long) (paymentRequestDto.getAmount() * 100))
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(paymentRequestDto.getDescription())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            // Create Stripe checkout session
            Session session = Session.create(params);

            // Save payment transaction in MongoDB
            Payment payment = new Payment(session.getId(), userId, paymentRequestDto.getAmount(), "PENDING");
            paymentRepository.save(payment);

            // Build response
            Map<String, String> response = new HashMap<>();
            response.put("sessionId", session.getId());
            response.put("checkoutUrl", session.getUrl());

            return ResponseEntity.ok(ApiResponseDto.builder()
                    .isSuccess(true)
                    .response(response)
                    .build());

        } catch (StripeException e) {
            log.error("Stripe Payment Error: " + e.getMessage());
            throw new PaymentException("Payment processing failed!");
        }
    }
}
