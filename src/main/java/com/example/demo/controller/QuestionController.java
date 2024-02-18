package com.example.demo.controller;

import com.example.demo.DataNotFoundException;
import com.example.demo.dto.AnswerFormDto;
import com.example.demo.dto.QuestionFormDto;
import com.example.demo.entity.Question;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/question")
@Controller
public class QuestionController {

    @Autowired
    private final QuestionService questionService;

    @Autowired
    private final QuestionRepository questionRepository;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0")int page){
        Page<Question> paging = questionService.getList(page);
        model.addAttribute("paging", paging);
        model.addAttribute("questionList", questionService.getList());
        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(@PathVariable("id")Integer id, AnswerFormDto answerFormDto, Model model) throws Exception{
        model.addAttribute("question", questionService.getQuestion(id));
        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate(QuestionFormDto questionFormDto, Model model){
        model.addAttribute("questionFormDto", questionFormDto);
        return "question_form";
    }

    @PostMapping("/create")
    public String postQuestionCreate(@Valid QuestionFormDto questionFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        questionService.create(questionFormDto.getSubject(), questionFormDto.getContent());
        return "redirect:/question/list";
    }
}
