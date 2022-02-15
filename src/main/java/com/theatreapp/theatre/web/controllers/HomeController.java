package com.theatreapp.theatre.web.controllers;

import com.theatreapp.theatre.data.entities.User;
import com.theatreapp.theatre.dto.PlayDTO;
import com.theatreapp.theatre.services.*;
import com.theatreapp.theatre.web.models.KeywordViewModel;
import com.theatreapp.theatre.web.models.ViewModel;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class HomeController extends ViewModel {
    private final PlayService playService;
    private final UserServiceImpl userService;

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(authentication.getName());
    }

    @GetMapping("/")
    public String viewPreloaderPage() {
        return "preloader";
    }

    @GetMapping("/home")
    public String viewIndexPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());

        List<PlayDTO> playsResults = playService.findAllByLastAdded();

        model.addAttribute("playsRatings", playService.getPlaysRating(playsResults));
        model.addAttribute("playsResults",
                playsResults
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));

        return "home";
    }
}
