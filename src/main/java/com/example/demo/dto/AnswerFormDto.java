package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerFormDto {

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

}
