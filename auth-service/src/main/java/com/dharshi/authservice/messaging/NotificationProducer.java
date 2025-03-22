package com.dharshi.authservice.messaging;

import com.dharshi.authservice.config.RabbitMQConfig;
import com.dharshi.authservice.dtos.MailRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmailNotification(MailRequestDto mailRequestDto) {
        log.info("Sending email notification: {}", mailRequestDto);
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.EMAIL_ROUTING_KEY,
            mailRequestDto
        );
        log.info("Email notification sent to queue");
    }
}
