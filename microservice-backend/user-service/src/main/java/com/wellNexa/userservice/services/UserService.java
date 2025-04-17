package com.wellNexa.userservice.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wellNexa.userservice.dtos.ApiResponseDto;
import com.wellNexa.userservice.exceptions.ServiceLogicException;
import com.wellNexa.userservice.exceptions.UserNotFoundException;


@Service
public interface UserService {
    ResponseEntity<ApiResponseDto<?>> existsUserById(String userId) throws ServiceLogicException;

    ResponseEntity<ApiResponseDto<?>> getUserById(String id) throws ServiceLogicException, UserNotFoundException;
}
