package com.wellNexa.cartservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.wellNexa.cartservice.modals.Payment;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    // Find payments by user ID
    List<Payment> findByUserId(String userId);

    // Find a payment by session ID
   Optional<Payment> findBySessionId(String sessionId);

    // Check if a payment exists by session ID
    boolean existsBySessionId(String sessionId);
}
