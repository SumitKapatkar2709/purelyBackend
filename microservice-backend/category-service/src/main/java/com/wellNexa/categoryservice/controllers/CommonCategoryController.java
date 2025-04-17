package com.wellNexa.categoryservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wellNexa.categoryservice.dtos.ApiResponseDto;
import com.wellNexa.categoryservice.exceptions.ServiceLogicException;
import com.wellNexa.categoryservice.services.CategoryService;


@RestController
@RequestMapping("/category")
public class CommonCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponseDto<?>> getAllCategories() throws ServiceLogicException {
        return categoryService.getAllCategories();
    }

    @GetMapping("/get/byId")
    public ResponseEntity<ApiResponseDto<?>> getCategoryById(@RequestParam String id) throws ServiceLogicException {
        return categoryService.getCategoryById(id);
    }

}
