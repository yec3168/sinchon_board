package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/sample")
@Controller
public class TestController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello";
    }

}

// /sample/hello

// /sample/sample/hello로 접속
