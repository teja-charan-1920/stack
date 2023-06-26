package com.majorproject.StackOverflowClone.controller;

import com.majorproject.StackOverflowClone.model.Answer;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.service.AnswerService;
import com.majorproject.StackOverflowClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AnswerController {
    @Autowired
    AnswerService answerService;
    @Autowired
    UserService userService;

    @PostMapping("/questions/{questionId}/answer/{answerId}/voteUp")
    public String answerVotedUp(@PathVariable Long answerId,
                                @PathVariable Long questionId,
                                Model model) {
        User user = answerService.getUser();
        answerService.votedUp(answerId);
        Answer answer = answerService.getAnswerById(answerId);
        model.addAttribute("isPresent", !answer.getVotedUpByUsers().contains(user));
        return "redirect:/questions/" + questionId;
    }

    @PostMapping("/questions/{questionId}/answer/{answerId}/voteDown")
    public String answerVoteDown(@PathVariable Long answerId,
                                 @PathVariable Long questionId,
                                 Model model) {
        User user = answerService.getUser();
        answerService.votedDown(answerId);
        Answer answer = answerService.getAnswerById(answerId);
        model.addAttribute("isPresent", !answer.getVotedDownByUsers().contains(user));
        return "redirect:/questions/" + questionId;
    }

    @PostMapping("/questions/{questionId}/addAnswer")
    public String addAnswer(@RequestParam String answer,
                            @PathVariable Long questionId,
                            @RequestParam(value = "answerId",required = false) Long answerId) {
        if(answerId != null){
            answerService.editAnswer(answerId,answer);
        } else {
            answerService.addAnswer(questionId, answer);
        }
        return "redirect:/questions/" + questionId;
    }

    @PostMapping("/questions/{questionId}/answer/{answerId}/comment")
    public String addComment(@RequestParam String comment,
                             @PathVariable Long questionId, @PathVariable Long answerId) {
        answerService.addComment(answerId, comment);
        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/answerEdit/{questionId}/{answerId}")
    public String editAnswer(@PathVariable Long questionId, @PathVariable("answerId") Long answerId, Model model){
        model.addAttribute("editAnswer",answerService.getAnswerById(answerId).getAnswer());
        model.addAttribute("answerId",answerId);
        model.addAttribute("questionId",questionId);
        return "editAnswer";
    }
}
