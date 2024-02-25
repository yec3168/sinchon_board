package com.example.demo.controller;

import com.example.demo.dto.AnswerFormDto;
import com.example.demo.entity.Answer;
import com.example.demo.entity.Question;
import com.example.demo.entity.SiteUser;
import com.example.demo.repository.AnswerRepository;
import com.example.demo.service.AnswerService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/answer")
@Controller
public class AnswerController {
    @Autowired
    private final QuestionService questionService;


    @Autowired
    private final AnswerService answerService;

    @Autowired
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/create/{id}")
    public String newAnswer(@Valid AnswerFormDto answerFormDto, BindingResult bindingResult,
                            @PathVariable("id")Integer id, @RequestParam("content")String content,
                            Model model, Principal principal){
        Question question = this.questionService.getQuestion(id);
        SiteUser author = userService.getUser(principal.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("question", question);
            return "question_detail";
        }
        answerService.createAnswer(questionService.getQuestion(id), content, author);
        return "redirect:/question/detail/"+id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/update/{id}")
    public String updateAnswer(AnswerFormDto answerFormDto, @PathVariable("id")Integer id,
                              Principal principal){
        Answer answer = answerService.getAnswer(id);
        if(!answer.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");

        answerFormDto.setContent(answer.getContent());
        return "question_detail";
    }
}
