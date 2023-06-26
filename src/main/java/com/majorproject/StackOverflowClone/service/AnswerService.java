package com.majorproject.StackOverflowClone.service;

import com.majorproject.StackOverflowClone.model.Answer;
import com.majorproject.StackOverflowClone.model.Comment;
import com.majorproject.StackOverflowClone.model.History;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.repository.*;
import com.majorproject.StackOverflowClone.security.oauth.CustomUser;
import com.majorproject.StackOverflowClone.specification.HistorySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerService {
    public static final String ANSWERED = "Answered";
    public static final String COMMENT = "Comment Added";
    public static final String EDITED = "Edited";
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    HistoryRepository historyRepository;

    public Answer getAnswerById(Long answerId) {
        return answerRepository.findById(answerId).orElse(null);
    }

    public void updateAnswer(Answer answer) {
        answer.setVotes((long) (answer.getVotedUpByUsers().size() - answer.getVotedDownByUsers().size()));
        answerRepository.save(answer);
    }

    public List<Answer> fetchAnswers(String sortBy) {
        return answerRepository.findAll(Sort.by(sortBy));
    }

    public void votedDown(Long answerId) {
        User user = getUser();
        Answer answer = getAnswerById(answerId);
        User answerOwner = answer.getUser();

        if (answer.getVotedUpByUsers().contains(user)) {
            answerOwner.setReputation(answerOwner.getReputation() - 10);
            answer.setUser(answerOwner);
            answer.getVotedUpByUsers().remove(user);
        } else {
            answerOwner.setReputation(answerOwner.getReputation() - 5);
            answer.setUser(answerOwner);
            answer.getVotedDownByUsers().add(user);
        }
        updateAnswer(answer);
    }

    public void votedUp(Long answerId) {
        User user = getUser();
        Answer answer = getAnswerById(answerId);
        User answerOwner = answer.getUser();

        if (answer.getVotedDownByUsers().contains(user)) {
            answerOwner.setReputation(answerOwner.getReputation() + 5);
            answer.setUser(answerOwner);
            answer.getVotedDownByUsers().remove(user);
        } else {
            answerOwner.setReputation(answerOwner.getReputation() + 10);
            answer.setUser(answerOwner);
            answer.getVotedUpByUsers().add(user);
        }
        updateAnswer(answer);
    }

    public void addAnswer(Long questionId, String data) {
        User user = getUser();

        Answer answer = new Answer();
        answer.setAnswer(data);
        answer.setUser(user);
        answer.setQuestion(questionRepository.findById(questionId).get());
        answerRepository.save(answer);
        saveHistory(answer, ANSWERED, null);
    }

    public void addComment(Long answerId, String data) {
        User user = getUser();
        Answer answer = answerRepository.findById(answerId).orElse(null);

        Comment comment = new Comment();
        comment.setComment(data);
        comment.setUser(user);
        commentRepository.save(comment);
        answer.getComments().add(comment);
        answerRepository.save(answer);
        saveHistory(answer, COMMENT, data);
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

    public void editAnswer(Long answerId, String answer, String comment) {
        Answer editedAnswer = answerRepository.findById(answerId).get();
        editedAnswer.setAnswer(answer);
        answerRepository.save(editedAnswer);
        saveHistory(editedAnswer, EDITED, comment);
    }

    public void saveHistory(Answer answer, String action, String comment) {
        History history = new History();
        history.setAction(action);
        history.setUser(getUser());
        history.setAnswer(answer);
        history.setComment(comment);
        historyRepository.save(history);
    }

    public List<History> getHistoryByAnswerId(Long id) {
        HistorySpecification historySpecification = new HistorySpecification();
        Specification<History> specification = historySpecification.getHistorySpecification(id);
        return historyRepository.findAll(specification);
    }
}
