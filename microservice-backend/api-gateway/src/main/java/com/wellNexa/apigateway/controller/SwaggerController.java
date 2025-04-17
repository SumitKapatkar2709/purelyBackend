package com.wellNexa.apigateway.controller;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwaggerController {

    @RequestMapping("/swagger-ui.html")
    public String getSwaggerUI() {
        return "Swagger UI Aggregated Endpoint";
    }
}
