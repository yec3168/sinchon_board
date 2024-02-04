package com.example.demo.controller;

import com.example.demo.repository.AnswerRepository;
import com.example.demo.service.AnswerService;
import com.example.demo.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class AnswerController {
    @Autowired
    private final QuestionService questionService;
    @Autowired
    private final AnswerRepository answerRepository;

    @Autowired
    private final AnswerService answerService;

    @PostMapping(value = "/answer/create/{id}")

    public String newAnswer(@PathVariable("id")Integer id, @RequestParam("content")String content, Model model){
        answerService.createAnswer(questionService.getQuestion(id), content);
        return "redirect:/question/detail/"+id;
    }
}
