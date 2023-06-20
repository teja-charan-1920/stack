package com.majorproject.StackOverflowClone.controller;

import com.majorproject.StackOverflowClone.dto.QuestionDto;
import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.service.QuestionService;
import com.majorproject.StackOverflowClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;

    @PostMapping("/questions/{id}/vote/up")
    public String voteUpForQuestion(@PathVariable Long id) {
        User user = userService.getUserById(1L);
        Question question = questionService.getQuestionById(id);

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
        questionService.updateQuestion(question);
        return "redirect:/questions/" + id;
    }

    @PostMapping("questions/{id}/vote/down")
    public String voteDownForQuestion(@PathVariable Long id) {
        User user = userService.getUserById(1L);
        Question question = questionService.getQuestionById(id);

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
        questionService.updateQuestion(question);
        return "redirect:/questions/" + id;
    }

    @PostMapping("/questions/ask")
    public String addQuestion(@ModelAttribute QuestionDto questionDto) {
        questionService.addQuestion(questionDto);
        return "redirect:/questions/ask";
    }

    @GetMapping("/questions/ask")
    public String getQuestionPage() {
        return "ask_que_form";
    }

    @RequestMapping("/")
    public String homePage() {
        List<Question> allQuestions = questionService.getAllQuestions();
        return "allQue";
    }

    @GetMapping("/questions/{id}")
    public String getQuestion(@RequestParam(name = "sort", defaultValue = "votes", required = false) String sortBy,
                              @PathVariable Long id,
                              Model model){
        model.addAttribute("question",questionService.getQuestion(id,sortBy));
        return "history";
    }
}
