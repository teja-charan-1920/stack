package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    @Autowired
    QuestionRepository questionRepository;

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public void updateQuestion(Question previousQuestion) {
//        Question question = new Question();
//        question.setQuestionId(previousQuestion.getQuestionId());
//        question.setTitle(previousQuestion.getTitle());
//        question.setDescription(previousQuestion.getDescription());
//        question.setTags(previousQuestion.getTags());
//        question.setViews(previousQuestion.getViews());
//        question.setCreationDateTime(previousQuestion.getCreationDateTime());
//        question.setAnswers(previousQuestion.getAnswers());
//        question.setVotedUpByUsers(previousQuestion.getVotedUpByUsers());
//        question.setVotedDownByUsers(previousQuestion.getVotedDownByUsers());

        questionRepository.save(previousQuestion);
    }

    public void addQuestion(Question question, String tags) {
    }
}