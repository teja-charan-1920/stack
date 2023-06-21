package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.model.Tag;
import com.majorproject.StackOverflowClone.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;
    public List<Tag> getTagsByPage() {
       return tagRepository.findAll();
    }


    public List<Question> getQuestionsByTag(String name) {
        Tag tag = tagRepository.findByName(name);
        List<Question> tagQuestions = new ArrayList<>();
        try {
            tagQuestions = new ArrayList<>(tag.getQuestions());
        } catch (Exception e) {
        }

        return tagQuestions;
    }

    public long countTotalTags() {
        return tagRepository.count();

    }
}
