package com.wellNexa.productservice.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.wellNexa.productservice.models.Product;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product,String> {

    List<Product> findByCategoryId(String categoryId);
    List<Product> findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(String ProductName, String description, String categoryName);

}
