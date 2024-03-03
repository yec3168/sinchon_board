package com.example.demo.controller;

import com.example.demo.DataNotFoundException;
import com.example.demo.dto.AnswerFormDto;
import com.example.demo.dto.QuestionFormDto;
import com.example.demo.entity.Question;
import com.example.demo.entity.SiteUser;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.service.QuestionService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataLocationNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.print.attribute.standard.Sides;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
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

    @Autowired
    private final UserService userService;



    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0")int page){
        Page<Question> paging = questionService.getList(page);
        model.addAttribute("paging", paging);
       return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(@PathVariable("id")Integer id, AnswerFormDto answerFormDto,
                         HttpServletRequest request,
                         HttpServletResponse response,
                         Model model) throws Exception{
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        viewCountValidation(question, request, response);

        return "question_detail";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionFormDto questionFormDto, Model model){
        model.addAttribute("questionFormDto", questionFormDto);
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String postQuestionCreate(@Valid QuestionFormDto questionFormDto,
                                     BindingResult bindingResult, Model model,
                                     Principal principal){
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        SiteUser author = userService.getUser(principal.getName());
        questionService.create(questionFormDto.getSubject(), questionFormDto.getContent(), author);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/update/{id}")
    public String questionUpdate(QuestionFormDto questionFormDto, Principal principal,
                                 @PathVariable("id")Integer id, Model model){

        Question question = questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        System.out.println(question.getSubject());
        System.out.println(question.getContent());

        questionFormDto.setSubject(question.getSubject());
        questionFormDto.setContent(question.getContent());

        model.addAttribute("questionFormDto", questionFormDto);
        return "question_form";

    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    public String questionUpdate(@Valid QuestionFormDto questionFormDto, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id")Integer id){
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        Question question = questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        question.updateQuestion(questionFormDto);
        questionService.updateSave(question);
        return String.format("redirect:/question/detail/%s",id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(@PathVariable("id")Integer id, Principal principal){
        Question question = questionService.getQuestion(id);
        // Postman으로 접근하는 방식 지정.
        if(!question.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");

        questionService.delete(question);

        return "redirect:/";
    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/vote/{id}")
//    public String voteQuestion(@PathVariable("id")Integer id, Principal principal){
//        Question question = questionService.getQuestion(id);
//        SiteUser siteUser = userService.getUser(principal.getName());
//        if(!question.getVoter().contains(siteUser))
//            questionService.voteQuestion(question, siteUser);
//        else{
//            questionService.deleteVote(question, siteUser);
//        }
//        return "redirect:/question/detail/"+id;
//    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String voteQuestion(@PathVariable("id")Integer id, Principal principal){
        Question question = questionService.getQuestion(id);
        SiteUser siteUser = userService.getUser(principal.getName());
        if(!question.getVoter().contains(siteUser)){
            questionService.voteQuestion(question, siteUser);
        }
        else{
            questionService.deleteVote(question, siteUser);
        }


        return "redirect:/question/detail/"+id;
    }

    public void viewCountValidation(Question question, HttpServletRequest request,
                                    HttpServletResponse response){
        Cookie[] cookies = request.getCookies(); // 쿠키 얻어오기.
        /* 초기화 */
        Cookie cookie =null;
        boolean isCookie = false;

        /* request에 있는 쿠키들이 있을때 */
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            // postView 쿠키가 있을 때
            if (cookies[i].getName().equals("questionView")) {
                // cookie 변수에 저장
                cookie = cookies[i];
                // 만약 cookie 값에 현재 게시글 번호가 없을 때
                if (!cookie.getValue().contains("[" + question.getId() + "]")) {
                    // 해당 게시글 조회수를 증가시키고, 쿠키 값에 해당 게시글 번호를 추가
                    questionService.viewCountUp(question);
                    cookie.setValue(cookie.getValue() + "[" + question.getId() + "]");
                }
                isCookie = true;
                break;
            }
        }
        // 만약 postView라는 쿠키가 없으면 처음 접속한 것이므로 새로 생성
        if (!isCookie) {
            questionService.viewCountUp(question);
            cookie = new Cookie("questionView", "[" + question.getId() + "]"); // oldCookie에 새 쿠키 생성
        }

        // 쿠키 유지시간을 오늘 하루 자정까지로 설정
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);
    }
}
