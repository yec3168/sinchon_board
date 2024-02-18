package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {

    @NotEmpty(message = "아이디는 필수 입력값입니다.")
    private String username;

    @NotEmpty(message = "아이디는 필수 입력값입니다.")
    private String password1;

    @NotEmpty(message = "아이디는 필수 입력값입니다.")
    private String password2;

    @NotEmpty(message = "아이디는 필수 입력값입니다.")
    private String email;

}
