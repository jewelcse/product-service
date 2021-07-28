package com.productservice.controller;


import com.productservice.service.CachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CachingController {

    @Autowired
    CachingService cachingService;

    @GetMapping("/clearAllCaches")
    public void clearAllCaches() {
        cachingService.evictAllCaches();
    }
}
