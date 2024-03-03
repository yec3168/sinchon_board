package com.example.demo.service;

import com.example.demo.DataNotFoundException;
import com.example.demo.entity.Question;
import com.example.demo.entity.SiteUser;
import com.example.demo.repository.QuestionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
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

    public void voteQuestion(Question question, SiteUser siteUser){
        question.getVoter().add(siteUser);
        question.setVoteYn(true);
        questionRepository.save(question);
    }

    public void deleteVote(Question question ,SiteUser siteUser){
        question.getVoter().remove(siteUser);
        question.setVoteYn(false);
        questionRepository.save(question);
    }
    public void updateSave(Question question){
        questionRepository.save(question);
    }

    public void delete(Question question){
        questionRepository.delete(question);
    }

    public  void viewCountUp(Question question){
        question.setView(question.getView()+1);
        questionRepository.save(question);
    }

    public void viewCountValidation(Question question, HttpServletRequest request,
                                    HttpServletResponse response){
        Cookie[] cookies = request.getCookies(); // 쿠키 얻어오기.
        /* 초기화 */
        Cookie cookie =null;
        boolean isCookie = false;

        /* request에 있는 쿠키들이 있을때 */
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            // postView 쿠키가 있을 때
            if (cookies[i].getName().equals("questionView")) {
                // cookie 변수에 저장
                cookie = cookies[i];
                // 만약 cookie 값에 현재 게시글 번호가 없을 때
                if (!cookie.getValue().contains("[" + question.getId() + "]")) {
                    // 해당 게시글 조회수를 증가시키고, 쿠키 값에 해당 게시글 번호를 추가
                    viewCountUp(question);
                    cookie.setValue(cookie.getValue() + "[" + question.getId() + "]");
                }
                isCookie = true;
                break;
            }
        }
        // 만약 postView라는 쿠키가 없으면 처음 접속한 것이므로 새로 생성
        if (!isCookie) {
            viewCountUp(question);
            cookie = new Cookie("questionView", "[" + question.getId() + "]"); // oldCookie에 새 쿠키 생성
        }

        // 쿠키 유지시간을 오늘 하루 자정까지로 설정
        long todayEndSecond = LocalDate.now().atTime(LocalTime.MAX).toEpochSecond(ZoneOffset.UTC);
        long currentSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        cookie.setPath("/"); // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) (todayEndSecond - currentSecond));
        response.addCookie(cookie);
    }
}
