package com.majorproject.StackOverflowClone.controller;

import com.majorproject.StackOverflowClone.model.Answer;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.service.AnswerService;
import com.majorproject.StackOverflowClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        User answerOwner = answer.getUser();
        if (answer.getVotedDownByUsers().contains(user)) {
            answerOwner.setReputation(answerOwner.getReputation()+5);
            answer.setUser(answerOwner);
            answer.getVotedDownByUsers().remove(user);
            answerService.updateAnswer(answer);
        } else {
            answerOwner.setReputation(answerOwner.getReputation()+10);
            answer.setUser(answerOwner);
            answer.getVotedUpByUsers().add(user);
            answerService.updateAnswer(answer);
        }
        return "redirect:/viewQuestion?questionId=" + questionId;
    }

    @RequestMapping("/answerVotedDown")
    public String answerVoteDown(@RequestParam("answerId") Long answerId, @RequestParam("questionId") Long questionId) {
        User user = userService.getUserById(1L);
        Answer answer = answerService.getAnswerById(answerId);
        User answerOwner = answer.getUser();
        if (answer.getVotedUpByUsers().contains(user)) {
            answerOwner.setReputation(answerOwner.getReputation()-10);
            answer.setUser(answerOwner);
            answer.getVotedUpByUsers().remove(user);
            answerService.updateAnswer(answer);
        } else {
            answerOwner.setReputation(answerOwner.getReputation()-5);
            answer.setUser(answerOwner);
            answer.getVotedDownByUsers().add(user);
            answerService.updateAnswer(answer);
        }

        return "redirect:/viewQuestion?questionId=" + questionId;
    }

    @GetMapping
    public String sortAnswers(@RequestParam(name = "sort", defaultValue = "votes", required = false) String sortBy, Model model){
        model.addAttribute("question", answerService.fetchAnswers(sortBy));
        return "history";
    }
}
