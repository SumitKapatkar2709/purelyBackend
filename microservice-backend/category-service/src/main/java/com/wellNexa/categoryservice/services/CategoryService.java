package com.wellNexa.categoryservice.services;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wellNexa.categoryservice.dtos.ApiResponseDto;
import com.wellNexa.categoryservice.dtos.CategoryRequestDto;
import com.wellNexa.categoryservice.exceptions.CategoryAlreadyExistsException;
import com.wellNexa.categoryservice.exceptions.ServiceLogicException;

@Service
public interface CategoryService {

    ResponseEntity<ApiResponseDto<?>> getAllCategories() throws ServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> getCategoryById(String categoryId) throws ServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> createCategory(CategoryRequestDto categoryRequestDto) throws ServiceLogicException, CategoryAlreadyExistsException;

    ResponseEntity<ApiResponseDto<?>> editCategory(String categoryId, CategoryRequestDto categoryRequestDto) throws ServiceLogicException, CategoryAlreadyExistsException;

    ResponseEntity<ApiResponseDto<?>> deleteCategory(String categoryId) throws ServiceLogicException;
}
