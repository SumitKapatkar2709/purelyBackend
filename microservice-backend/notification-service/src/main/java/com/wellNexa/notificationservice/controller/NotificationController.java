package com.wellNexa.notificationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wellNexa.notificationservice.dtos.ApiResponseDto;
import com.wellNexa.notificationservice.dtos.MailRequestDto;
import com.wellNexa.notificationservice.services.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    ResponseEntity<ApiResponseDto<?>> sendEmail(@RequestBody MailRequestDto requestDto) {
        return notificationService.sendEmail(requestDto);
    }

}
