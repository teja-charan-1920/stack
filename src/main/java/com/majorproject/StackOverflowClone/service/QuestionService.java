package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.dto.QuestionDto;
import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.model.Tag;
import com.majorproject.StackOverflowClone.repository.AnswerRepository;
import com.majorproject.StackOverflowClone.repository.QuestionRepository;
import com.majorproject.StackOverflowClone.repository.TagRepository;
import com.majorproject.StackOverflowClone.repository.UserRepository;
import com.majorproject.StackOverflowClone.specification.AnswerSpecification;
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
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    UserRepository userRepository;

    private AnswerSpecification answerSpecification = new AnswerSpecification();

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

    public Long addQuestion(QuestionDto questionDto) {
        if(questionDto.getTotalTags() == null) {
            return questionRepository.save(convertDtoToDao(questionDto)).getQuestionId();
        }
        Set<Tag> setOfTags = saveTags(questionDto.getTotalTags().split(","));
        Question question = convertDtoToDao(questionDto);
        for (Tag tag : setOfTags) {
                tag.getQuestions().add(question);
                tagRepository.save(tag);
            }
        question.setTags(setOfTags);
        question.setUser(userRepository.findById(1l).get());
        return questionRepository.save(question).getQuestionId();
    }

    private Question convertDtoToDao(QuestionDto questionDto) {
        Question question = new Question();

        question.setTitle(questionDto.getTitle());
        question.setDescription(questionDto.getDescription());
        question.setTags(questionDto.getTags());
        return question;
    }

    public QuestionDto getQuestion(Long id, String sortBy) {
        Question question = getQuestionById(id);
        QuestionDto questionDto =  convertDaoToDto(question);
        questionDto.setAnswers(new HashSet<>(answerRepository.findAll(answerSpecification.findByQuestionIdAndSortByVotes(id,sortBy))));
        questionDto.setSortBy(sortBy);
        return  questionDto;
    }

    public QuestionDto convertDaoToDto(Question question){
        QuestionDto questionDto = new QuestionDto();

        questionDto.setId(question.getQuestionId());
        questionDto.setTags(question.getTags());
        questionDto.setAnswers(question.getAnswers());
        questionDto.setTitle(question.getTitle());
        questionDto.setDescription(question.getDescription());
        questionDto.setCreatedAt(question.getCreationDateTime());
        questionDto.setVotes(question.getVotes());
        questionDto.setViews(question.getViews());

        return questionDto;
    }

    public Set<Tag> saveTags(String[] totalTags) {
        Set<Tag> tags = new HashSet<>();
        for (String singleTag : totalTags) {
            Tag tag = tagRepository.findByName(singleTag.trim());
            if (tag == null) {
                tag = new Tag();
                tag.setName(singleTag.trim());
            }
            tags.add(tag);
        }
        return tags;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }
}
