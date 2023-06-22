package com.majorproject.StackOverflowClone.controller;

import com.majorproject.StackOverflowClone.model.Answer;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.service.AnswerService;
import com.majorproject.StackOverflowClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AnswerController {
    @Autowired
    AnswerService answerService;
    @Autowired
    UserService userService;

    @RequestMapping("/questions/{questionId}/answer/{answerId}/voteUp")
    public String answerVotedUp(@PathVariable Long answerId,
                                @PathVariable Long questionId) {
        answerService.votedUp(answerId);
        return "redirect:/questions/" + questionId;
    }

    @RequestMapping("/questions/{questionId}/answer/{answerId}/voteDown")
    public String answerVoteDown(@PathVariable Long answerId,
                                 @PathVariable Long questionId) {
        answerService.votedDown(answerId);
        return "redirect:/questions/" + questionId;
    }

    @PostMapping("/questions/{questionId}/addAnswer")
    public String addAnswer(@RequestParam  String answer,
                            @PathVariable Long questionId){
        answerService.addAnswer(questionId,answer);
        return "redirect:/questions/" + questionId;
    }

    @PostMapping("/questions/{questionId}/answer/{answerId}/comment")
    public String addComment(@RequestParam  String comment,
                            @PathVariable Long questionId, @PathVariable Long answerId){
        answerService.addComment(answerId,comment);
        return "redirect:/questions/" + questionId;
    }

}
