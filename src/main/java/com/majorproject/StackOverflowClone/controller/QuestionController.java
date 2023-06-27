package com.majorproject.StackOverflowClone.controller;

import com.majorproject.StackOverflowClone.dto.QuestionDto;
import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.service.QuestionService;
import com.majorproject.StackOverflowClone.service.TagService;
import com.majorproject.StackOverflowClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;

    @Autowired
    TagService tagService;

    @PostMapping("/questions/{id}/vote/up")
    public String voteUpForQuestion(@PathVariable Long id) {
        questionService.votedUp(id);
        return "redirect:/questions/" + id;
    }

    @GetMapping("/home")
    public String homePage(@RequestParam(name = "sort", defaultValue = "votes", required = false) String sort,
                           Model model) {
        model.addAttribute("questions", questionService.getQuestionsForHomePage(sort));
        return "home";
    }

    @PostMapping("questions/{id}/vote/down")
    public String voteDownForQuestion(@PathVariable Long id) {
        questionService.votedDown(id);
        return "redirect:/questions/" + id;
    }

    @PostMapping("/questions/ask")
    public String addQuestion(@ModelAttribute QuestionDto questionDto, Model model) {
        long id=0;
        try {
               id = questionService.addQuestion(questionDto);
        } catch (Exception e) {
            model.addAttribute("exists", "Question already exists");
            return "ask_que_form";
        }
        return "redirect:/questions/" + id;
    }

    @GetMapping("/questions/ask")
    public String getQuestionPage(Model model) {
        return "ask_que_form";
    }

    @PostMapping("/questions/{id}/edit")
    public String editQuestion(@ModelAttribute QuestionDto questionDto, Model model) {
        long id = questionService.addEditQuestion(questionDto);
        return "redirect:/questions/" + id;
    }
    @PreAuthorize("@questionService.checkQuestionEditor(#id)")
    @GetMapping("/questions/{id}/edit")
    public String getEditQuestion(@PathVariable Long id, Model model){
        model.addAttribute("question",questionService.convertDaoToDto(questionService.getQuestionById(id)));
        return "ask_que_form";
    }

    @GetMapping("/questions/{id}")
    public String getQuestion(@RequestParam(name = "sort", defaultValue = "votes", required = false) String sortBy,
                              @PathVariable Long id,
                              Model model) {
        model.addAttribute("user", questionService.getUser());
        model.addAttribute("question", questionService.getQuestion(id, sortBy));
        return "perticularQue";
    }

    @GetMapping("/")
    public String questionsHomePage(@RequestParam(name = "search", required = false) String search,
                                    @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                    @RequestParam(name = "pagesize", required = false, defaultValue = "15") int pageSize,
                                    @RequestParam(name = "sort", defaultValue = "votes", required = false) String sort,
                                    Model model) {
        model.addAttribute("questions", questionService.getAllQuestions(search, page, pageSize, sort));
        return "allQue";
    }

    @GetMapping("/questionView/{id}")
    public String addView(@PathVariable Long id) {
        questionService.setViewForQuestion(id);
        return "redirect:/questions/" + id;
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}

