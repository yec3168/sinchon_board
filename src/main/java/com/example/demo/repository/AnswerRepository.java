package com.example.demo.repository;

import com.example.demo.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Optional<Answer> findById(Integer id);

    Page<Answer> findAll(Pageable pageable);
}
