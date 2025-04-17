package com.wellNexa.notificationservice.services;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wellNexa.notificationservice.dtos.ApiResponseDto;
import com.wellNexa.notificationservice.dtos.MailRequestDto;

import java.io.UnsupportedEncodingException;

@Service
public interface NotificationService {
    ResponseEntity<ApiResponseDto<?>> sendEmail(MailRequestDto requestDto);
}
