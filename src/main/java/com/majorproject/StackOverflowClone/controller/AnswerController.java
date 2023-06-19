package com.majorproject.StackOverflowClone.controller;

import com.majorproject.StackOverflowClone.model.Answer;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.service.AnswerService;
import com.majorproject.StackOverflowClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnswerController {
    @Autowired
    AnswerService answerService;
    @Autowired
    UserService userService;

    @RequestMapping("/answerVotedUp")
    public String answerVotedUp(@RequestParam("answerId") Long answerId, @RequestParam("questionId") Long questionId) {
        User user = userService.getUserById(1L);
        Answer answer = answerService.getAnswerById(answerId);
        if (answer.getVotedDownByUsers().contains(user)) {
            answer.getVotedDownByUsers().remove(user);
            answerService.updateAnswer(answer);
        } else {
            answer.getVotedUpByUsers().add(user);
            answerService.updateAnswer(answer);
        }

        return "redirect:/viewQuestion?questionId=" + questionId;
    }

    @RequestMapping("/answerVotedDown")
    public String answerVoteDown(@RequestParam("answerId") Long answerId, @RequestParam("questionId") Long questionId) {
        User user = userService.getUserById(1L);
        Answer answer = answerService.getAnswerById(answerId);
        if (answer.getVotedUpByUsers().contains(user)) {
            answer.getVotedUpByUsers().remove(user);
            answerService.updateAnswer(answer);
        } else {
            answer.getVotedDownByUsers().add(user);
            answerService.updateAnswer(answer);
        }

        return "redirect:/viewQuestion?questionId=" + questionId;
    }
}
