package com.theatreapp.theatre.web.controllers;

import com.theatreapp.theatre.data.entities.User;
import com.theatreapp.theatre.services.NewsService;
import com.theatreapp.theatre.services.UserServiceImpl;
import com.theatreapp.theatre.web.models.KeywordViewModel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class NewsController {
    private final NewsService newsService;
    private final UserServiceImpl userService;

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(authentication.getName());
    }

    @GetMapping("/news-results")
    public String viewNewsResultsPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("newsResults", newsService.getAllNews());

        return "news-results";
    }

    @GetMapping("/news/{name}/{id}")
    public String viewNewsPage(Model model, @PathVariable String id) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("news", newsService.findById(Long.parseLong(id)));

        return "news";
    }
}
