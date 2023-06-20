package com.majorproject.StackOverflowClone.dto;

import com.majorproject.StackOverflowClone.model.Answer;
import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class QuestionDto {
    private Long id;
    private String title;
    private String description;
    private Set<Answer> answers;
    private Set<Tag> tags;
    private Long views;
    private LocalDateTime createdAt;
    private Long votes;
    private String sortBy;
    private Set<Question> relatedQue;
    private String totalTags;
}
