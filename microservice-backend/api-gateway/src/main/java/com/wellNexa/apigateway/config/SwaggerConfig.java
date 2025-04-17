package com.wellNexa.apigateway.config;


import java.util.Collection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
public class SwaggerConfig {
	
	@Bean
       public Docket api() {
    	   return new Docket(DocumentationType.SWAGGER_2).apiInfo(getInfo()).select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();
       }
       
       @SuppressWarnings("deprecation")
	private ApiInfo getInfo() {
    	   return new ApiInfo("Blogging Application","This Project is developed by priyank shah","1.0","terms of Service","License of APIS","API license URL","Contact no:7041137206");
       };
}
