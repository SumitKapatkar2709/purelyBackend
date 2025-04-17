package com.wellNexa.productservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wellNexa.productservice.dtos.ApiResponseDto;
import com.wellNexa.productservice.dtos.ProductRequestDto;
import com.wellNexa.productservice.exceptions.ResourceNotFoundException;
import com.wellNexa.productservice.exceptions.ServiceLogicException;
import com.wellNexa.productservice.services.ProductService;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponseDto<?>> addProduct(@RequestBody ProductRequestDto requestDto) throws ServiceLogicException, ResourceNotFoundException {
        return productService.addProduct(requestDto);
    }

    @PutMapping("/edit")
    public ResponseEntity<ApiResponseDto<?>> editProduct(@RequestParam String productId, @RequestBody ProductRequestDto requestDto) throws ServiceLogicException, ResourceNotFoundException {
        return productService.editProduct(productId, requestDto);
    }

}
