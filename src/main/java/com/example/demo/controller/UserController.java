package com.example.demo.controller;

import com.example.demo.dto.UserCreateForm;
import com.example.demo.entity.SiteUser;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    public String getSignup(Model model){
        model.addAttribute("userCreateForm", new UserCreateForm());
        return "signup_form";
    }


    @PostMapping("/signup")
    public String postSignup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "signup_form";
        }

        if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())){
            bindingResult.rejectValue("password2", "passwordInCorrect","2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        Optional<SiteUser> op = userService.getUsername(userCreateForm.getUsername());

        if(!op.isPresent()){
            try{
                SiteUser user = userService.create(userCreateForm);
                if(user == null){
                    bindingResult.rejectValue("username","이미 등록된 아이디거나 형식이 올바르지 않습니다.");
                    return "signup_form";
                }
            }catch (DataIntegrityViolationException e){
                e.printStackTrace();
                bindingResult.rejectValue("signupFailed", "이미 등록된 사용자입니다.");
                return "signup_form";
            }
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login_form";
    }

//    @PostMapping("/login")
//    public String PostLogin(@RequestParam("username")String username,
//                            @RequestParam("password")String password,
//                            Model model){
//        SiteUser user = userService.noSecurityLogin(username, password);
//        if(user == null){
//            model.addAttribute("errorMsg", "아이디 혹은 비밀번호를 확인해주세요.");
//            return "login_form";
//        }
//
//        return "redirect:/";
//    }
}
