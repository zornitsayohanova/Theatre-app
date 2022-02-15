package com.theatreapp.theatre.web.controllers;

import com.theatreapp.theatre.data.entities.Rating;
import com.theatreapp.theatre.data.entities.User;
import com.theatreapp.theatre.dto.*;
import com.theatreapp.theatre.services.*;
import com.theatreapp.theatre.web.models.*;

import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class TheatreController extends ViewModel {
    private final TownService townService;
    private final TheatreService theatreService;
    private final UserServiceImpl userService;
    private final NewsService newsService;

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(authentication.getName());
    }

    @GetMapping("/theatres/search")
    public String viewTheatresSearchPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("theatre", new TheatreViewModel());
        model.addAttribute("town", new TownViewModel());

        model.addAttribute("allTheatres",
                theatreService.getAllTheatres()
                        .stream()
                        .map(this::convertToTheatreViewModel)
                        .collect(Collectors.toList()));

        model.addAttribute("allTowns",
                townService.getAllTowns()
                        .stream()
                        .map(this::convertToTownViewModel)
                        .collect(Collectors.toList()));

        model.addAttribute("allTheatreGenres",
                theatreService.getAllTheatreGenres()
                       .stream()
                        .map(this::convertToGenreViewModel)
                        .collect(Collectors.toList()));

        model.addAttribute("allTheatreTypes",
                theatreService.getAllTheatreTypes()
                        .stream()
                        .map(this::convertToTheatreTypeViewModel)
                        .collect(Collectors.toList()));

        return "search-theatres";
    }

    @GetMapping("/theatres/search/results")
    public String getActorsSearchResults(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("theatresRating", new ArrayList<Float>());
        model.addAttribute("theatreResults", new ArrayList<TheatreDTO>());

        return "theatres-results";
    }

    @PostMapping("/theatres/search/results")
    private String getTheatresSearchResults(Model model, @ModelAttribute TheatreViewModel theatreViewModel,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "home";
        }

        List<TheatreDTO> theatresResults = theatreService.findSearchedTheatres(convertToTheatreDTO(theatreViewModel));

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("theatresRatings", theatreService.getTheatresRating(theatresResults));
        model.addAttribute("theatreResults",
                theatresResults
                        .stream()
                        .map(this::convertToTheatreViewModel)
                        .collect(Collectors.toList()));

        return "theatres-results";
    }

    @GetMapping(value = {"/theatres/search/results/{name}/{id}", "/theatres/{name}/{id}"})
    private String getTheatrePage(Model model, @PathVariable ("id") String theatreId) {

        long id = Long.parseLong(theatreId);

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("theatre", theatreService.findById(id));
        model.addAttribute("rating", new Rating());
        model.addAttribute("theatreRating", theatreService.getTheatreRating(id));
        model.addAttribute("userGivenRating", userService.getUserTheatreRating(this.getUser(), id));
        model.addAttribute("userLikedTheatre", userService.isTheatreLiked(this.getUser(), id));
        model.addAttribute("workHours", theatreService.getWorkHoursData(id));
        model.addAttribute("contacts", theatreService.getContactsData(id));
        model.addAttribute("theatrePlays",
                theatreService.getTheatrePlays(id)
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));
        model.addAttribute("theatreGallery",
                theatreService.getTheatreGallery(id)
                        .stream()
                        .map(this::convertToPictureViewModel)
                        .collect(Collectors.toList()));

        return "theatre";
    }

    @GetMapping("/get-theatre-news/{name}/{id}")
    public String getActorNews(Model model, @PathVariable String id) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("newsResults", newsService.getTheatreNews(theatreService.findByIdDTO(Long.parseLong(id)))
                .stream()
                .map(this::convertToNewsViewModel)
                .collect(Collectors.toList()));

        return "news-results";
    }
}
