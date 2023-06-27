package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.dto.PageDto;
import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.model.Tag;
import com.majorproject.StackOverflowClone.repository.QuestionRepository;
import com.majorproject.StackOverflowClone.repository.TagRepository;
import com.majorproject.StackOverflowClone.specification.QuestionSpecification;
import com.majorproject.StackOverflowClone.specification.TagSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagService {
    @Autowired
    QuestionRepository questionRepository;
    private static final int PAGESIZE = 36;
    private TagSpecification tagSpecification = new TagSpecification();
    private QuestionSpecification questionSpecification  = new QuestionSpecification();
    @Autowired
    TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }


    public PageDto getQuestionsByTag(String name,int page,int pageSize,String sort) {
        PageDto pageDto = new PageDto();
        Pageable pageable = PageRequest.of(page-1,pageSize, Sort.by(Sort.Direction.DESC,sort));
        Specification<Question> specification = questionSpecification.getQuestionsWithTag(name);
        Page<Question> questionPage = questionRepository.findAll(specification,pageable);

        pageDto.setQuestions(questionPage.getContent());
        pageDto.setPage(page);
        pageDto.setTotalPages(questionPage.getTotalPages());
        pageDto.setSortBy(sort);
        pageDto.setAllQuestions(questionPage.getTotalElements());
        pageDto.setRelatedTags(getRelatedTags(name));
        pageDto.setTags(getAllTags());
        return pageDto;
    }

    public long countTotalTags() {
        return tagRepository.count();
    }

    public Object getTags(String search, int page, String sort) {
        Pageable pageable = null;
        if(sort.equals("name")){
             pageable = PageRequest.of(page - 1, PAGESIZE, Sort.by(Sort.Direction.ASC, sort));
        } else {
             pageable = PageRequest.of(page - 1, PAGESIZE, Sort.by(Sort.Direction.DESC, sort));
        }
        Specification<Tag> specification = Specification.where(null);

        if (search != null) {
            specification = tagSpecification.searchByKeyword(search);
        }
        Page<Tag> tagPage = tagRepository.findAll(specification, pageable);

        PageDto pageDto = new PageDto();
        pageDto.setTags(tagPage.getContent());
        pageDto.setTotalPages(tagPage.getTotalPages());
        pageDto.setPage(page);
        pageDto.setSearch(search);
        pageDto.setSortBy(sort);
        return pageDto;
    }

    public Map<String,Integer> getRelatedTags(String name){
        List<Tag> tags = getTagsWithName(name);
        tags.sort(Comparator.comparingInt(tag -> tag.getQuestions().size()));
        Map<String,Integer> relatedTags = new LinkedHashMap<>();
        ListIterator<Tag> tagListIterator = tags.listIterator(tags.size());

        while(tagListIterator.hasPrevious()) {
            Tag tag = tagListIterator.previous();
            relatedTags.put(tag.getName(),tag.getQuestions().size());
        }
        relatedTags.remove(name);
        return relatedTags;
    }

    public List<Tag> getTagsWithName(String name){
        if(name == null) {
            return tagRepository.findAll();
        }

        Set<Question> questions = tagRepository.findByName(name).getQuestions();
        Set<Tag> tagSet = new HashSet<>();
        for(Question question : questions) {
            tagSet.addAll(question.getTags());
        }
        return new ArrayList<>(tagSet);
    }
}
