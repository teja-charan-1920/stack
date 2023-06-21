package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.model.Tag;
import com.majorproject.StackOverflowClone.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    TagRepository tagRepository;
    public List<Tag> getTagsByPage() {
       return tagRepository.findAll();
    }
}
