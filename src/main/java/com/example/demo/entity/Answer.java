package com.example.demo.entity;

import com.example.demo.dto.AnswerFormDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<SiteUser> voter;

    @ColumnDefault("false")
    private boolean voteYn;
    public void update(AnswerFormDto answerFormDto){
        this.content = answerFormDto.getContent();
        this.updateTime = LocalDateTime.now();
    }
}
