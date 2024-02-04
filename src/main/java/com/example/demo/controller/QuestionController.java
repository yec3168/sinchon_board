package com.example.demo.controller;

import com.example.demo.DataNotFoundException;
import com.example.demo.entity.Question;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class QuestionController {

    @Autowired
    private final QuestionService questionService;

    @Autowired
    private final QuestionRepository questionRepository;

    @GetMapping("/question/list")
    public String list(Model model){
        model.addAttribute("questionList", questionService.getList());
        return "question_list";
    }

    @GetMapping(value = "/question/detail/{id}")
    public String detail(@PathVariable("id")Integer id, Model model) throws Exception{
        model.addAttribute("question", questionService.getQuestion(id));
        return "question_detail";
    }
}
