package com.akinnova.bookstoredemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/testing")
public class TestController {

    @GetMapping
    public String greetings(){
        return "Welcome to our app!!";
    }

    @GetMapping("/calculate")
    public String calculate(){
        return String.valueOf(2 + 2);
    }
}

