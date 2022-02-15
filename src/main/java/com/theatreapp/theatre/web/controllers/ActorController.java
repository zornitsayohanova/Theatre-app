package com.theatreapp.theatre.web.controllers;

import com.theatreapp.theatre.data.entities.User;
import com.theatreapp.theatre.dto.ActorDTO;
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
public class ActorController extends ViewModel {
    private final TheatreService theatreService;
    private final ActorService actorService;
    private final UserServiceImpl userService;
    private final GenderService genderService;
    private final NewsService newsService;

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(authentication.getName());
    }

    @GetMapping("/actors/search")
    public String viewActorsSearchPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("actor", new ActorViewModel());
        model.addAttribute("theatre", new TheatreViewModel());

        model.addAttribute("allTheatres",
                theatreService.getAllTheatres()
                        .stream()
                        .map(this::convertToTheatreViewModel)
                        .collect(Collectors.toList()));

        model.addAttribute("allActors",
                actorService.getAllActors()
                        .stream()
                        .map(this::convertToActorViewModel)
                        .collect(Collectors.toList()));

        model.addAttribute("allGenders",
                genderService.getAllGenders()
                        .stream()
                        .map(this::convertToGenderViewModel)
                        .collect(Collectors.toList()));

        return "search-actors";
    }

    @GetMapping("/actors/search/results")
    public String viewActorsSearchResultsPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("actorsRatings", new ArrayList<Float>());
        model.addAttribute("actorsResults", new ArrayList<ActorDTO>());

        return "actors-results";
    }

    @PostMapping("/actors/search/results")
    public String getActorsSearchResults(Model model, @ModelAttribute ActorViewModel actorViewModel,
                                         BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }

        List<ActorDTO> actorsResults = actorService.findSearchedActors(convertToActorDTO(actorViewModel));

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("actorsRatings", actorService.getActorsRating(actorsResults));
        model.addAttribute("actorsResults",
                actorsResults
                        .stream()
                        .map(this::convertToActorViewModel)
                        .collect(Collectors.toList()));

        return "actors-results";
    }

    @GetMapping(value = {"/actors/search/results/{name}/{id}", "/actors/{name}/{id}"})
    public String getActorPage(Model model, @PathVariable ("id") String actorId) {

        long id = Long.parseLong(actorId);

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("actor", this.convertToActorViewModel(actorService.findByIdDTO(id)));
        model.addAttribute("rating", new RatingViewModel());
        model.addAttribute("actorRating", actorService.getActorRating(id));
        model.addAttribute("userGivenRating", userService.getUserActorRating(this.getUser(), id));
        model.addAttribute("userLikedActor", userService.isActorLiked(this.getUser(), id));
        model.addAttribute("actorPlays",
                actorService.getActorPlays(id)
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));
        model.addAttribute("actorGallery",
                actorService.getActorGallery(id)
                        .stream()
                        .map(this::convertToPictureViewModel)
                        .collect(Collectors.toList()));

        return "actor";
    }

    @GetMapping("/get-actor-news/{name}/{id}")
    public String getActorNews(Model model, @PathVariable String id) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("newsResults", newsService.getActorNews(actorService.findByIdDTO(Long.parseLong(id)))
                .stream()
                .map(this::convertToNewsViewModel)
                .collect(Collectors.toList()));

        return "news-results";
    }

}
