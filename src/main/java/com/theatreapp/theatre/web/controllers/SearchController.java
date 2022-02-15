package com.theatreapp.theatre.web.controllers;
import com.theatreapp.theatre.dto.*;
import com.theatreapp.theatre.services.*;
import com.theatreapp.theatre.web.models.*;
import org.modelmapper.ModelMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class SearchController extends ViewModel{

    private final TownService townService;

    private final GenreService genreService;

    private final ModelMapper modelMapper = new ModelMapper();

    private final PlayService playService;

    private final TheatreService theatreService;

    private final ActorService actorService;

    private final DataService dataService;

    private final SearchService searchService;

    @PostMapping("/search-by-keyword")
    public String getKeywordResult(Model model, @ModelAttribute KeywordViewModel keywordViewModel, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }

        String keyword = "%" + keywordViewModel.getKeywordValue() + "%";

        List<ActorDTO> actorsResults = actorService.findActorByName(keyword);
        List<PlayDTO> playsResults = playService.findPlayByName(keyword);
        List<TheatreDTO> theatresResults = theatreService.findTheatreByName(keyword);

        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("searchedKeyword", keywordViewModel.getKeywordValue());
        model.addAttribute("actorsRatings", actorService.getActorsRating(actorsResults));
        model.addAttribute("actorsResults",
                actorsResults
                        .stream()
                        .map(this::convertToActorViewModel)
                        .collect(Collectors.toList()));

        model.addAttribute("playsRatings", playService.getPlaysRating(playsResults));
        model.addAttribute("playsResults",
                playsResults
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));

        model.addAttribute("theatresRatings", theatreService.getTheatresRating(theatresResults));
        model.addAttribute("theatreResults",
                theatresResults
                        .stream()
                        .map(this::convertToTheatreViewModel)
                        .collect(Collectors.toList()));

        return "search-results";
    }

    private String returnActorResults(Model model, List<ActorDTO> actorsResults) {
        model.addAttribute("actorsRatings", actorService.getActorsRating(actorsResults));
        model.addAttribute("actorsResults", actorsResults
                .stream()
                .map(this::convertToActorViewModel)
                .collect(Collectors.toList()));

        return "actors-results";
    }

    private String returnPlayResults(Model model, List<PlayDTO> playsResults) {
        model.addAttribute("playsRatings", playService.getPlaysRating(playsResults));
        model.addAttribute("playsResults", playsResults
                .stream()
                .map(this::convertToPlayViewModel)
                .collect(Collectors.toList()));

        return "plays-results";
    }
}





