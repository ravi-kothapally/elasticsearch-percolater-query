package com.example.testing.random;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class MyController {

  @GetMapping("/data")
  public ResponseEntity<String> getData() {
    // Simulate some data retrieval or processing
    String responseData = "This is the cached data.";
    // Set the Cache-Control header to allow caching for 1 hour
    System.out.println("calling method");

    CacheControl cacheControl = CacheControl.maxAge(1, TimeUnit.HOURS);
    return ResponseEntity.ok()
        .cacheControl(cacheControl)
        .body(responseData);
  }
}

