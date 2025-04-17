package com.wellNexa.cartservice.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellNexa.cartservice.modals.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {
    Cart findByUserId(String userId);
    boolean existsByUserId(String userId);

}
