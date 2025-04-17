package com.wellNexa.orderservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellNexa.orderservice.modals.Order;

import java.util.Set;

public interface OrderRepository extends MongoRepository<Order,String> {

    Set<Order> findByUserIdOrderByIdDesc(String userId);


}
