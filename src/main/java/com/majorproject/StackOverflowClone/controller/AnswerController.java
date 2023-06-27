package com.majorproject.StackOverflowClone.controller;

import com.majorproject.StackOverflowClone.model.Answer;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnswerController {
    @Autowired
    AnswerService answerService;

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
    public String addAnswer(@PathVariable Long questionId,
                            @RequestParam String answer) {
        answerService.addAnswer(questionId, answer);
        return "redirect:/questions/" + questionId;
    }

    @PostMapping("/questions/{questionId}/answer/{answerId}/edit")
    public String editAnswer(@PathVariable Long answerId,
                             @PathVariable Long questionId,
                             @RequestParam String answer,
                             @RequestParam String comment,
                             Model model) {
        answerService.editAnswer(answerId, answer, comment);
        return "redirect:/questions/" + questionId;
    }


    @PostMapping("/questions/{questionId}/answer/{answerId}/comment")
    public String addComment(@RequestParam String comment,
                             @PathVariable Long questionId,
                             @PathVariable Long answerId) {
        answerService.addComment(answerId, comment);
        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/question/{questionId}/answer/{answerId}")
    public String editAnswer(@PathVariable Long questionId, @PathVariable("answerId") Long answerId, Model model) {
        model.addAttribute("editAnswer", answerService.getAnswerById(answerId).getAnswer());
        model.addAttribute("answerId", answerId);
        model.addAttribute("questionId", questionId);
        return "editAnswer";
    }

    @GetMapping("/posts/{id}/timeline")
    public String getHistory(@PathVariable Long id,
                             Model model) {
        model.addAttribute("question", answerService.getAnswerById(id).getQuestion());
        model.addAttribute("histories", answerService.getHistoryByAnswerId(id));
        return "history";
    }
}
