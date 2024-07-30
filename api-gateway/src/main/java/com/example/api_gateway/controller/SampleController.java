package com.example.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class SampleController {

    @GetMapping
    public String getHello(){
        System.out.println("Hello From GetHello");
        return "{ \"message\":\" Hello From GetHello Route \"}";
    }

}
