package com.majorproject.StackOverflowClone.controller;

import com.majorproject.StackOverflowClone.model.Tag;
import com.majorproject.StackOverflowClone.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TagController {
    @Autowired
    TagService tagService;
@GetMapping("/tags")
    public String tags(Model model, @RequestParam(value = "start", defaultValue = "1") int start,
                       @RequestParam(value = "limit", defaultValue = "10") int limit,
                       @RequestParam(value = "order", defaultValue = "") String sortOrder,
                       @RequestParam(value = "search", defaultValue = "") String searchKeyword) {
    List<Tag> tags;
//    if(!sortOrder.isEmpty()) {
//
//    }
//    else if(!searchKeyword.isEmpty()) {
//
//    }
//    else {
    tags = tagService.getTagsByPage();
//    }
    model.addAttribute("tags",tags);
    System.out.println(tags);
    return "tags";
}
}
