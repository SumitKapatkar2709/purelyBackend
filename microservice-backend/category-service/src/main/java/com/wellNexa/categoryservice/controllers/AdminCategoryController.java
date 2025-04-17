package com.wellNexa.categoryservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wellNexa.categoryservice.dtos.ApiResponseDto;
import com.wellNexa.categoryservice.dtos.CategoryRequestDto;
import com.wellNexa.categoryservice.exceptions.CategoryAlreadyExistsException;
import com.wellNexa.categoryservice.exceptions.ServiceLogicException;
import com.wellNexa.categoryservice.services.CategoryService;

@RestController
@RequestMapping("/admin/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponseDto<?>> createCategory(@RequestBody CategoryRequestDto categoryRequestDto) throws ServiceLogicException, CategoryAlreadyExistsException {
        return categoryService.createCategory(categoryRequestDto);
    }

    @PutMapping("/edit")
    public ResponseEntity<ApiResponseDto<?>> editCategory(@RequestParam String categoryId, @RequestBody CategoryRequestDto categoryRequestDto) throws ServiceLogicException, CategoryAlreadyExistsException {
        return categoryService.editCategory(categoryId, categoryRequestDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponseDto<?>> deleteCategory(@RequestParam String categoryId) throws ServiceLogicException, CategoryAlreadyExistsException {
        return categoryService.deleteCategory(categoryId);
    }

}
