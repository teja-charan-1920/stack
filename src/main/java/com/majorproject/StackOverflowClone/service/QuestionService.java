package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.dto.PageDto;
import com.majorproject.StackOverflowClone.dto.QuestionDto;
import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.model.Tag;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.repository.AnswerRepository;
import com.majorproject.StackOverflowClone.repository.QuestionRepository;
import com.majorproject.StackOverflowClone.repository.TagRepository;
import com.majorproject.StackOverflowClone.repository.UserRepository;
import com.majorproject.StackOverflowClone.security.oauth.CustomUser;
import com.majorproject.StackOverflowClone.specification.AnswerSpecification;
import com.majorproject.StackOverflowClone.specification.QuestionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Boolean.FALSE;

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
    @Autowired
    TagService tagService;

    private final AnswerSpecification answerSpecification = new AnswerSpecification();
    private final QuestionSpecification questionSpecification = new QuestionSpecification();

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElse(null);
    }

    public void updateQuestionVotes(Question previousQuestion) {
        previousQuestion.setVotes((long) (previousQuestion.getVotedUpByUsers().size() - previousQuestion.getVotedDownByUsers().size()));
        questionRepository.save(previousQuestion);
    }

    public Long addQuestion(QuestionDto questionDto) {
        if (questionRepository.findByTitle(questionDto.getTitle()) != null) {
            throw new RuntimeException();
        }
        if (questionDto.getTotalTags() == null) {
            return questionRepository.save(convertDtoToDao(questionDto)).getQuestionId();
        }
        Set<Tag> setOfTags = saveTags(questionDto.getTotalTags().split(","));
        Question question = convertDtoToDao(questionDto);

        for (Tag tag : setOfTags) {
            tag.getQuestions().add(question);
            tagRepository.save(tag);
        }
        question.setTags(setOfTags);
        return questionRepository.save(question).getQuestionId();
    }

    private Question convertDtoToDao(QuestionDto questionDto) {
        Question question = new Question();

        question.setTitle(questionDto.getTitle());
        question.setDescription(questionDto.getDescription());
        question.setUser(getUser());
        return question;
    }

    public QuestionDto getQuestion(Long id, String sortBy) {
        Question question = getQuestionById(id);
        QuestionDto questionDto = convertDaoToDto(question);
        questionDto.setAnswers(answerRepository.findAll(answerSpecification.findByQuestionIdAndSortByVotes(id, sortBy)));
        questionDto.setSortBy(sortBy);
        questionDto.setCheckQuestionEditor(checkQuestionEditor(id));
        questionDto.setEmail(getUser().getEmail());
        questionDto.setRelatedQue(getRelatedQuestions(question));
        questionDto.setUsername(question.getUser().getUsername());
        return setVotedUpAndDown(questionDto);
    }

    public QuestionDto setVotedUpAndDown(QuestionDto questionDto) {
        User user = getUser();
        Question question = getQuestionById(questionDto.getId());

        if (question.getVotedUpByUsers().contains(user)) {
            questionDto.setShowVoteUp(FALSE);
        } else if (question.getVotedDownByUsers().contains(user)) {
            questionDto.setShowVoteDown(FALSE);
        }
        return questionDto;
    }

    public QuestionDto convertDaoToDto(Question question) {
        QuestionDto questionDto = new QuestionDto();

        questionDto.setId(question.getQuestionId());
        questionDto.setTags(new ArrayList<>(question.getTags()));
        questionDto.setAnswers(new ArrayList<>(question.getAnswers()));
        questionDto.setTitle(question.getTitle());
        questionDto.setDescription(question.getDescription());
        questionDto.setCreatedAt(question.getCreatedAt());
        questionDto.setUpdatedAt(question.getUpdatedAt());
        questionDto.setVotes(question.getVotes());
        questionDto.setViews(question.getViews());
        questionDto.setTotalTags(getTotalTagsInString(question.getTags()));

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

    public PageDto getAllQuestions(String search, int page, int pageSize, String sort) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.DESC, sort));
        Specification<Question> specification = Specification.where(null);

        if (search != null) {
            specification = questionSpecification.filterPostsOnKeyword(search);
        }
        Page<Question> questionPage = questionRepository.findAll(specification, pageable);
        int totalPages = questionPage.getTotalPages();

        PageDto pageDto = new PageDto();
        pageDto.setPage(page);
        pageDto.setPageSize(pageSize);
        pageDto.setTotalPages(totalPages);
        pageDto.setQuestions(questionPage.getContent());
        pageDto.setTags(tagRepository.findAll());
        pageDto.setRelatedTags(tagService.getRelatedTags(null));
        pageDto.setSortBy(sort);
        pageDto.setSearch(search);
        pageDto.setAllQuestions(questionPage.getTotalElements());
        return pageDto;
    }

    public void votedUp(Long id) {
        User user = getUser();
        Question question = getQuestionById(id);
        User questionOwner = question.getUser();

        if (question.getVotedDownByUsers().contains(user)) {
            questionOwner.setReputation(questionOwner.getReputation() + 5);
            question.setUser(questionOwner);
            question.getVotedDownByUsers().remove(user);
        } else {
            questionOwner.setReputation(questionOwner.getReputation() + 10);
            question.setUser(questionOwner);
            question.getVotedUpByUsers().add(user);
        }
        updateQuestionVotes(question);
    }

    public void votedDown(Long id) {
        User user = getUser();
        Question question = getQuestionById(id);
        User questionOwner = question.getUser();

        if (question.getVotedUpByUsers().contains(user)) {
            questionOwner.setReputation(questionOwner.getReputation() - 10);
            question.setUser(questionOwner);
            question.getVotedUpByUsers().remove(user);
        } else {
            questionOwner.setReputation(questionOwner.getReputation() - 5);
            question.setUser(questionOwner);
            question.getVotedDownByUsers().add(user);
        }
        updateQuestionVotes(question);
    }

    public PageDto getQuestionsForHomePage(String sort) {
        PageDto pageDto = new PageDto();
        pageDto.setTags(tagRepository.findAll());
        pageDto.setRelatedTags(tagService.getRelatedTags(null));
        Pageable pageable = PageRequest.of(0, 40, Sort.by(Sort.Direction.ASC, "updatedAt"));
        if (sort.equals("week")) {
            Specification<Question> specification = questionSpecification.getQuestionsInLastDays(7);
            pageDto.setQuestions(questionRepository.findAll(specification, pageable).getContent());
            return pageDto;
        } else if (sort.equals("month")) {
            Specification<Question> specification = questionSpecification.getQuestionsInLastDays(30);
            pageDto.setQuestions(questionRepository.findAll(specification, pageable).getContent());
            return pageDto;
        }
        pageable = PageRequest.of(0, 40, Sort.by(Sort.Direction.DESC, "votes"));
        Specification<Question> specification = questionSpecification.getQuestionsInLastDays(15);
        pageDto.setQuestions(questionRepository.findAll(specification, pageable).getContent());
        return pageDto;
    }

    public void setViewForQuestion(Long id) {
        Question question = getQuestionById(id);
        question.setViews(question.getViews() + 1);
        questionRepository.save(question);
    }

    public List<Question> getRelatedQuestions(Question question) {
        Specification<Question> specification = questionSpecification.getQuestionsWithTags(question.getTags());
        List<Question> questions = questionRepository.findAll(specification, Sort.by(Sort.Direction.DESC, "votes"));
        questions.remove(question);
        return questions;
    }

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userRepository.findByEmail(username).orElse(null);
        }
        if (principal instanceof OAuth2User) {
            CustomUser oAuth2User = new CustomUser((OAuth2User) principal);
            String email = oAuth2User.getEmail();
            return userRepository.findByEmail(email).orElse(null);
        }
        return null;
    }

    public QuestionDto getQuestionToEdit(Long id) {
        Question question = getQuestionById(id);
        question.setTitle(question.getTitle());
        question.setDescription(question.getDescription());

        return convertDaoToDto(question);
    }

    private String getTotalTagsInString(Set<Tag> tags) {
        String totalTags = "";
        for (Tag tag : tags) {
            totalTags += tag.getName() + ",";
        }
        return totalTags;
    }

    public Long addEditQuestion(QuestionDto questionDto) {
        Set<Tag> setOfTags = saveTags(questionDto.getTotalTags().split(","));
        Question question = getQuestionById(questionDto.getId());
        question.setDescription(questionDto.getDescription());
        question.setTitle(questionDto.getTitle());
        for (Tag tag : setOfTags) {
            tag.getQuestions().add(question);
            tagRepository.save(tag);
        }
        question.setTags(setOfTags);
        tagRepository.deleteUnusedTags();
        return questionRepository.save(question).getQuestionId();
    }

    public boolean checkQuestionEditor(long id) {
        try {
            Question question = questionRepository.findById(id).orElse(null);
            if (question == null)
                return true;
            return Objects.requireNonNull(userRepository.findByEmail(getUser().getEmail()).orElse(null)).getQuestions().contains(question);
        } catch (Exception e) {
            return false;
        }
    }
}
