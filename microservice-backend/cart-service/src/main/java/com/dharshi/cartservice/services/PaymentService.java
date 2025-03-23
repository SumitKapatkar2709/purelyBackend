package com.dharshi.cartservice.services;

import com.dharshi.cartservice.dtos.ApiResponseDto;
import com.dharshi.cartservice.dtos.PaymentRequestDto;
import com.dharshi.cartservice.exceptions.PaymentException;
import com.dharshi.cartservice.modals.Payment;
import com.dharshi.cartservice.repositories.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
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
