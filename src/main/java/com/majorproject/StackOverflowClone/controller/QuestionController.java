package com.majorproject.StackOverflowClone.controller;

import com.majorproject.StackOverflowClone.dto.QuestionDto;
import com.majorproject.StackOverflowClone.model.Question;
import com.majorproject.StackOverflowClone.service.QuestionService;
import com.majorproject.StackOverflowClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;

    @PostMapping("/questions/{id}/vote/up")
    public String voteUpForQuestion(@PathVariable Long id) {
        questionService.votedUp(id);
        return "redirect:/questions/" + id;
    }

    @PostMapping("questions/{id}/vote/down")
    public String voteDownForQuestion(@PathVariable Long id) {
        questionService.votedDown(id);
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


    @GetMapping("/")
    public String homePage() {
        List<Question> allQuestions = questionService.getAllQuestions();
        return "allQue";
    }

    @GetMapping("/questions/{id}")
    public String getQuestion(@RequestParam(name = "sort", defaultValue = "votes", required = false) String sortBy,
                              @PathVariable Long id,
                              Model model) {
        model.addAttribute("question", questionService.getQuestion(id, sortBy));
        return "history";
    }

    @RequestMapping("/")
    public String homePage(Model model) {
        List<Question> allQuestions = questionService.getAllQuestions();
        model.addAttribute("questions", allQuestions);
        return "allQue";
    }
}
