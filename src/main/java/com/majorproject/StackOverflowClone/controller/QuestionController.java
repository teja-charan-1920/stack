package com.majorproject.StackOverflowClone.controller;

import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.service.QuestionService;
import com.majorproject.StackOverflowClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

        User questionOwner = question.getUser();
        if (question.getVotedDownByUsers().contains(user)) {
            questionOwner.setReputation(questionOwner.getReputation()+5);
            question.setUser(questionOwner);
            question.getVotedDownByUsers().remove(user);
        } else {
            questionOwner.setReputation(questionOwner.getReputation()+10);
            question.setUser(questionOwner);
            question.getVotedUpByUsers().add(user);
        }
        questionService.updateQuestion(question);
        return "redirect:/viewQuestion?questionId=" + questionId;
    }

    @RequestMapping("/votedDown")
    public String voteDownForQuestion(@RequestParam("questionId") Long questionId) {
        User user = userService.getUserById(1L);
        Question question = questionService.getQuestionById(questionId);

        User questionOwner = question.getUser();
        if (question.getVotedUpByUsers().contains(user)) {
            questionOwner.setReputation(questionOwner.getReputation()-10);
            question.setUser(questionOwner);
            question.getVotedUpByUsers().remove(user);

        } else {
            questionOwner.setReputation(questionOwner.getReputation()-5);
            question.setUser(questionOwner);
            question.getVotedDownByUsers().add(user);
        }
        questionService.updateQuestion(question);
        return "redirect:/viewQuestion?questionId=" + questionId;
    }
        @PostMapping("/questions/ask")
        public String addQuestion(@ModelAttribute Question question, @RequestParam("tag") String tags) {
            questionService.addQuestion(question, tags);
            return "redirect:/questions/ask";
    }
    @GetMapping("/questions/ask")
    public String addNewQuestion() {
        return "ask_que_form";
    }
}
