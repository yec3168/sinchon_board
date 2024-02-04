package com.example.demo.controller;

import com.example.demo.dto.AnswerFormDto;
import com.example.demo.entity.Question;
import com.example.demo.repository.AnswerRepository;
import com.example.demo.service.AnswerService;
import com.example.demo.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String newAnswer(@Valid AnswerFormDto answerFormDto, BindingResult bindingResult, @PathVariable("id")Integer id, @RequestParam("content")String content, Model model){
        Question question = this.questionService.getQuestion(id);
        if(bindingResult.hasErrors()){
            model.addAttribute("question", question);
            return "question_detail";
        }
        answerService.createAnswer(questionService.getQuestion(id), content);
        return "redirect:/question/detail/"+id;
    }
}
