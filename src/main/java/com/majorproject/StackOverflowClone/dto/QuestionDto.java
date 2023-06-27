package com.majorproject.StackOverflowClone.dto;

import com.majorproject.StackOverflowClone.model.Answer;
import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.model.Tag;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class QuestionDto {
    private Long id;
    private String title;
    private String description;
    private List<Answer> answers;
    private List<Tag> tags;
    private Long views;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long votes;
    private String sortBy;
    private List<Question> relatedQue;
    private String totalTags;
    private String username;
    private boolean showVoteUp = true;
    private boolean showVoteDown = true;
}
