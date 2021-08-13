package com.productservice.controller;


import com.productservice.service.CachingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CachingController {

//    @Autowired
//    CachingService cachingService;
//
//    @GetMapping("/clearAllCaches")
//    public ResponseEntity<String> clearAllCaches() {
//        cachingService.evictAllCaches();
//        return new ResponseEntity<>("Cleared ALL Caches Successfully!", HttpStatus.OK);
//
//    }
}
