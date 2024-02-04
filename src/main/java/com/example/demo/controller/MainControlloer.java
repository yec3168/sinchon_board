package com.example.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class MainControlloer {

    @GetMapping("/hello")
    @ResponseBody //return으로 보내는 String을 그대로 html화면에 출력
    public String hello() {
        return "Hello World";
    }
    @PostMapping("/hello")
    public String helloworld(){
        return "hello world Post!";
    }

    @GetMapping("/sbb")
    @ResponseBody
    public String index() {
        return "안녕하세요 sbb에 오신 것을 환영합니다.";
    }

    @GetMapping("/")
    public String root(){
        return "redirect:/question/list";
    }

}
