package com.example.demo.entity;

import com.example.demo.dto.AnswerFormDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;

    public void update(AnswerFormDto answerFormDto){
        this.content = answerFormDto.getContent();
        this.updateTime = LocalDateTime.now();
    }
}
