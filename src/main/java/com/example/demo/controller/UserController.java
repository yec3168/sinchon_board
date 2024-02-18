package com.example.demo.controller;

import com.example.demo.dto.UserCreateForm;
import com.example.demo.entity.SiteUser;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/signup")
    public String getSignup(){
        return "signup_form";
    }


    @PostMapping("/signup")
    public String postSignup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "signup_form";
        }
        Optional<SiteUser> op = userService.getUsername(userCreateForm.getUsername());

        if(!op.isPresent()){
            SiteUser user = userService.notSecurityCreate(userCreateForm);
            if(user == null){
                return "signup_form";
            }
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login_form";
    }
    @PostMapping("/login")
    public String PostLogin(@RequestParam("username")String username,
                            @RequestParam("password")String password,
                            Model model){
        SiteUser user = userService.noSecurityLogin(username, password);
        if(user == null){
            model.addAttribute("errorMsg", "아이디 혹은 비밀번호를 확인해주세요.");
            return "login_form";
        }

        return "redirect:/";
    }
}
