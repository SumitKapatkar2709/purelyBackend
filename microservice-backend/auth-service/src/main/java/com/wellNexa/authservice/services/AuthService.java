package com.wellNexa.authservice.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wellNexa.authservice.dtos.ApiResponseDto;
import com.wellNexa.authservice.dtos.SignInRequestDto;
import com.wellNexa.authservice.dtos.SignUpRequestDto;
import com.wellNexa.authservice.exceptions.ServiceLogicException;
import com.wellNexa.authservice.exceptions.UserAlreadyExistsException;
import com.wellNexa.authservice.exceptions.UserNotFoundException;
import com.wellNexa.authservice.exceptions.UserVerificationFailedException;

import java.io.UnsupportedEncodingException;

@Service
public interface AuthService {
    ResponseEntity<ApiResponseDto<?>> registerUser(SignUpRequestDto signUpRequestDto) throws UnsupportedEncodingException, UserAlreadyExistsException, ServiceLogicException;
    ResponseEntity<ApiResponseDto<?>> resendVerificationCode(String email) throws UnsupportedEncodingException, UserNotFoundException, ServiceLogicException;
    ResponseEntity<ApiResponseDto<?>> verifyRegistrationVerification(String code) throws UserVerificationFailedException;
    ResponseEntity<ApiResponseDto<?>> authenticateUser(SignInRequestDto signInRequestDto);
    ResponseEntity<ApiResponseDto<?>> validateToken(String token);
}
