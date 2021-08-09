package com.productservice.config;


import springfox.documentation.service.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //configuring the contact detail
    public static final Contact DEFAULT_CONTACT = new Contact("Jewel Chowdhury", "http://www.codesnipeet.com", "codesnipeet");

    //configuring DEFAULT_API_INFO
    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Product Service", "Api Documentation for product-service", "1.0", "urn:tos",
            DEFAULT_CONTACT, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList<VendorExtension>());

    //two format which we want to produce and consume
    //private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<String>(Arrays.asList("application/json","application/xml"));


    //creating bean
    @Bean
    public Docket api()
    {
        ApiInfo apiInfo;
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(DEFAULT_API_INFO);
    }

}
