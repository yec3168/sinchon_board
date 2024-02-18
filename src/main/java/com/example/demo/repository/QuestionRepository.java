package com.example.demo.repository;

import com.example.demo.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String subject);

    Question findBySubjectAndContent(String subject, String content);

    List<Question> findBySubjectLike(String subject);

    Page<Question> findAll(Pageable pageable);

//    Question findBySubjectOrContent(String subject, String content);
//    List<Question> findByCreateDateBetween(LocalDateTime from, LocalDateTime to);
//    List<Question> findByIdLessThan(Integer id);
//    List<Question> findByIdGreaterThanEqual(Integer id);

};
