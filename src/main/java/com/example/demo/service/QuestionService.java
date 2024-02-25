package com.example.demo.service;

import com.example.demo.DataNotFoundException;
import com.example.demo.entity.Question;
import com.example.demo.entity.SiteUser;
import com.example.demo.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {

    @Autowired
    private final QuestionRepository questionRepository;

    public List<Question> getList(){
        return questionRepository.findAll();
    }

    @Transactional
    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    @Transactional
    public void create(String subject, String content, SiteUser author){
        Question question = new Question();
        question.setSubject(subject);
        question.setContent(content);
        question.setAuthor(author);
        question.setCreateDate(LocalDateTime.now());
        questionRepository.save(question);
    }

    public Page<Question> getList(int page){
        //PageRequest : 현재 페이지와 한 페이지에 보여 줄 게시물 개수 등을 설정하여 페이징 요청을 하는 클래스이다.
        //PageRequest.of(page, 10); // page는 조회할 페이지 번호, 10은 한페이젱 보여줄 페이지 개수
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAll(pageable);
    }

    public void updateSave(Question question){
        questionRepository.save(question);
    }
}
