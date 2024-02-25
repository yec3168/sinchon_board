package com.example.demo.entity;

import com.example.demo.dto.QuestionFormDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Question {

    @Id //private key 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer id;

    @Column(length = 200)
    private String subject; //제목

    @Column(columnDefinition = "text")
    private String content; //내용

    private LocalDateTime createDate; //작성날짜

    private LocalDateTime updateDate;

    //onetomamy의 기본 fetch type은 fetchtype=eagle 이다.
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answerList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;

    public void updateQuestion(QuestionFormDto questionFormDto){
        this.subject =questionFormDto.getSubject();
        this.content = questionFormDto.getContent();
        this.updateDate = LocalDateTime.now();

    }
}
