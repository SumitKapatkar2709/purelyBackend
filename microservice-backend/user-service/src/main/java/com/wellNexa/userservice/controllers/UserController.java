package com.wellNexa.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wellNexa.userservice.dtos.ApiResponseDto;
import com.wellNexa.userservice.exceptions.ServiceLogicException;
import com.wellNexa.userservice.exceptions.UserNotFoundException;
import com.wellNexa.userservice.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/exists/byId")
    public ResponseEntity<ApiResponseDto<?>> existsUserById(@RequestParam String userId) throws ServiceLogicException{
        return userService.existsUserById(userId);
    }

    @GetMapping("/get/byId")
    ResponseEntity<ApiResponseDto<?>> getUserById(@RequestParam String id) throws UserNotFoundException, ServiceLogicException {
        return userService.getUserById(id);
    }


}

