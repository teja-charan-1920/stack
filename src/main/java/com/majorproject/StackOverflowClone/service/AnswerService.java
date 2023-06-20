package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.model.Answer;
import com.majorproject.StackOverflowClone.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {
    @Autowired
    AnswerRepository answerRepository;
    public Answer getAnswerById(Long answerId) {
        return answerRepository.findById(answerId).orElse(null);
    }

    public void updateAnswer(Answer answer){
        answer.setVotes((long) (answer.getVotedUpByUsers().size()-answer.getVotedDownByUsers().size()));
        answerRepository.save(answer);
    }

    public List<Answer> fetchAnswers(String sortBy) {
      return answerRepository.findAll(Sort.by(sortBy));
    }
}
