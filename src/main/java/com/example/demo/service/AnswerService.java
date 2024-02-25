package com.example.demo.service;

import com.example.demo.DataNotFoundException;
import com.example.demo.entity.Answer;
import com.example.demo.entity.Question;
import com.example.demo.entity.SiteUser;
import com.example.demo.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {
    @Autowired
    private final AnswerRepository answerRepository;

    public void createAnswer(Question question, String content, SiteUser author){
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setContent(content);
        answer.setAuthor(author);
        answer.setCreateTime(LocalDateTime.now());
        answerRepository.save(answer);
    }

    public Answer getAnswer(Integer id){
        Optional<Answer> op = answerRepository.findById(id);
        if(op.isPresent()){
            return op.get();
        }
        else{
            throw new DataNotFoundException("Answer Not found");
        }
    }
}
