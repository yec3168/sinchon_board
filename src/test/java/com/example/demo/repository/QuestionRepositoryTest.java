package com.example.demo.repository;

import com.example.demo.entity.Answer;
import com.example.demo.entity.Question;
import com.example.demo.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;


    @Autowired
    private QuestionService questionService;

    @Test
    public void createQuestion(){
        Question question1 = new Question();
        question1.setSubject("sbb가 무엇인가요?");
        question1.setContent("sbb에 대해서 알고 싶습니다.");
        question1.setCreateDate(LocalDateTime.now());
        questionRepository.save(question1);

        Question question2 = new Question();
        question2.setSubject("스프링부트 모델 질문입니다.");
        question2.setContent("id는 자동으로 생성되나요?");
        question2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(question2);
    }

    @Test
    public void TestQuestion(){
        System.out.println("questionAll = " + questionRepository.findAll());
    }


    @Test
    public void listView(){
        List<Question> all = this.questionRepository.findAll();
        //assertEquals(2, all.size());


        Question q = all.get(0);
        assertEquals("sbb가 무엇인가요?", q.getSubject());

    }

    @Test
    public void findByIdQuestion(){
        Optional<Question> oq = questionRepository.findById(0);
        if(oq.isPresent()) {
            Question q = oq.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }

        Optional<Question> oq2 = questionRepository.findById(1);
        if(oq2.isPresent()) {
            Question q = oq2.get();
            assertEquals("sbb가 무엇인가요?", q.getSubject());
        }
    }

    @Test
    public void findBySubjectQuestion(){
        Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
        assertEquals(1, q.getId());
    }


    @Test
    public void findBySubjectAndContentTest(){
        Question q = questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        assertEquals(1, q.getId());
        /*
        where
        q1_0.subject=?
        and q1_0.content=?
        */
    }

    @Test
    public void findBySubjectLikeTest(){
        List<Question> questionList = questionRepository.findBySubjectLike("sbb가 무엇인가요?");

        Question q = questionList.get(0);

        assertEquals(1, q.getId());
    }

//    @Test
//    public void repositoryMethodTest(){
//        // or
//        Question qOr = questionRepository.findBySubjectOrContent("sbb가 무엇인가요?", "ㅈㄱㄴ");
//        assertEquals("sbb에 대해서 알고 싶습니다.", qOr.getContent());
//
//        // between
//        List<Question> qBetween = questionRepository.findByCreateDateBetween(LocalDateTime.MIN, LocalDateTime.now());
//        assertEquals(0, qBetween.size());
//
//        //lessthan
//        List<Question> qLessThan = questionRepository.findByIdLessThan(3);
//        assertEquals(2, qLessThan.size());
//
//        //GreaterThanEqual
//        List<Question> greateThanEqual = questionRepository.findByIdGreaterThanEqual(0);
//        assertEquals(2, greateThanEqual.size());
//
//    }


    @Test
    public void updateTest(){
        Optional<Question> question = questionRepository.findById(1);
        if(question.isPresent()){
            Question q = question.get();
            q.setSubject("첫번째질문");
            q.setContent("질문입니다");
            questionRepository.save(q);
        }
    }

    @Test
    public void deleteTest(){
        Question q = new Question();
        q.setSubject("test");
        q.setContent("test");
        q.setCreateDate(LocalDateTime.now());
        questionRepository.save(q);

        //delete
        Question question = questionRepository.findBySubject("test");
        if(question != null)
            questionRepository.delete(question);
    }

    @Test
    public void answerInsertTest(){
        Optional<Question> oq = questionRepository.findById(2);
        if(oq.isPresent()){
            Question q = oq.get();

            Answer answer = new Answer();
            answer.setContent("답변입니다.");
            answer.setCreateTime(LocalDateTime.now());
            answer.setQuestion(q);
            answerRepository.save(answer);
        }
    }

    @Test
    @Transactional(readOnly = true) //DB session을 유지, readOnly를 true로 하면 성능이 좋아짐.
    public void answerViewList(){
        Optional<Question> oq = questionRepository.findById(2);
        if(oq.isPresent()){
            Question q = oq.get();

            List<Answer> answerList = q.getAnswerList();
            assertEquals(1, answerList.size());
            assertEquals("답변입니다.", answerList.get(0).getContent());
        }
    }
    @Test
    void testJpa() {
        for (int i = 1; i <= 300; i++) {
            String subject = String.format("테스트 데이터입니다:[%03d]", i);
            String content = "내용무";
            this.questionService.create(subject, content, null);
        }
    }

}