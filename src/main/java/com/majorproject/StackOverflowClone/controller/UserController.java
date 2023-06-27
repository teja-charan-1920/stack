package com.majorproject.StackOverflowClone.controller;

import com.majorproject.StackOverflowClone.model.User;
import com.majorproject.StackOverflowClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String addUser(@ModelAttribute User user, Model model) {
        try{
            userService.addUser(user);
        } catch (Exception e) {
            model.addAttribute("exists", "An account for that email already exists");
            return "signup";
        }
        return "login";
    }

    @GetMapping("/users")
    public String users(@RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "sort", defaultValue = "reputation", required = false) String sortBy,
                        @RequestParam(name = "page", defaultValue = "1", required = false) int page,
                        Model model) {
        model.addAttribute("users", userService.getAllUsers(search, sortBy, page));
        return "user";
    }

}
