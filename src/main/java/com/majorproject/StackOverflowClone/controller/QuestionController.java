package com.majorproject.StackOverflowClone.controller;

import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.service.QuestionService;
import com.majorproject.StackOverflowClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class QuestionController {
    @Autowired

    QuestionService questionService;
    @Autowired
    UserService userService;

    @RequestMapping("/votedUp")
    public String voteUpForQuestion(@RequestParam("questionId") Long questionId) {
        User user = userService.getUserById(1L);
        Question question = questionService.getQuestionById(questionId);

        if (question.getVotedDownByUsers().contains(user)) {
            question.getVotedDownByUsers().remove(user);
            questionService.updateQuestion(question);
        } else {
            question.getVotedUpByUsers().add(user);
            questionService.updateQuestion(question);
        }
        return "redirect:/viewQuestion?questionId=" + questionId;
    }

    @RequestMapping("/votedDown")
    public String voteDownForQuestion(@RequestParam("questionId") Long questionId) {
        User user = userService.getUserById(1L);
        Question question = questionService.getQuestionById(questionId);

        if (question.getVotedUpByUsers().contains(user)) {
            question.getVotedUpByUsers().remove(user);
            questionService.updateQuestion(question);

        } else {
            question.getVotedDownByUsers().add(user);
            questionService.updateQuestion(question);
        }
        return "redirect:/viewQuestion?questionId=" + questionId;
    }
    @PostMapping("/questions/ask")
    public String addQuestion(@ModelAttribute Question question, @RequestParam("tags") String tags) {
        questionService.addQuestion(question, tags);
        return "redirect:/";
    }

}
