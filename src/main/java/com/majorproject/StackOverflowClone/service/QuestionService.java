package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.model.Tag;
import com.majorproject.StackOverflowClone.repository.QuestionRepository;
import com.majorproject.StackOverflowClone.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuestionService {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    TagRepository tagRepository;

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
        previousQuestion.setVotes((long) (previousQuestion.getVotedUpByUsers().size()-previousQuestion.getVotedDownByUsers().size()));

        questionRepository.save(previousQuestion);
    }

    public void addQuestion(Question question, String tags) {
        Set<Tag> setOfTags = new HashSet<>();
        String[] arrayOfTag = tags.split(",");
        for (String tag : arrayOfTag) {
            String trimmedTag = tag.trim();
            Tag existingTag = tagRepository.findByName(trimmedTag);
            if (existingTag != null) {
                setOfTags.add(existingTag);
            } else {
                Tag newTag = new Tag();
                newTag.setName(trimmedTag);
                tagRepository.save(newTag);
                setOfTags.add(newTag);
            }
        }
        question.setTags(setOfTags);
        questionRepository.save(question);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
}
