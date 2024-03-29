package com.example.demo.entity;

import com.example.demo.dto.QuestionFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @ColumnDefault("false")
    private boolean voteYn;

    @ColumnDefault("0")
    private int view;

    //onetomamy의 기본 fetch type은 fetchtype=eagle 이다.
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answerList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<SiteUser> voter;





    public void updateQuestion(QuestionFormDto questionFormDto){
        this.subject =questionFormDto.getSubject();
        this.content = questionFormDto.getContent();
        this.updateDate = LocalDateTime.now();

    }
}
