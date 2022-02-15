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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class PlayController extends ViewModel {
    private final TownService townService;
    private final GenreService genreService;
    private final PlayService playService;
    private final TheatreService theatreService;
    private final UserServiceImpl userService;
    private final NewsService newsService;

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(authentication.getName());
    }

    @GetMapping("/plays/search")
    public String viewPlaysSearchPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("play", new PlayViewModel());
        model.addAttribute("theatre", new TheatreViewModel());
        model.addAttribute("town", new TownViewModel());
        model.addAttribute("genre", new PlayGenreViewModel());

        model.addAttribute("allTowns",
                townService.getAllTowns()
                        .stream()
                        .map(this::convertToTownViewModel)
                        .collect(Collectors.toList()));
        model.addAttribute("allGenres",
                genreService.getAllGenres()
                        .stream()
                        .map(this::convertToGenreViewModel)
                        .collect(Collectors.toList()));
        model.addAttribute("allPlays",
                playService.getAllPlays()
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));
        model.addAttribute("allTheatres",
                theatreService.getAllTheatres()
                        .stream()
                        .map(this::convertToTheatreViewModel)
                        .collect(Collectors.toList()));

        return "search-plays";
    }

    @GetMapping("/plays/search/results")
    public String getActorsSearchResults(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("playsRatings", new ArrayList<Float>());
        model.addAttribute("playsResults", new ArrayList<PlayDTO>());

        return "plays-results";
    }

    @PostMapping("/plays/search/results")
    private String getPlaysSearchResults(Model model, @ModelAttribute PlayViewModel playViewModel,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "home";
        }

        List<PlayDTO> playsResults = playService.findSearchedPlays(convertToPlayDTO(playViewModel));

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("playsRatings", playService.getPlaysRating(playsResults));
        model.addAttribute("playsResults",
                playsResults
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));

        return "plays-results";
    }

    @GetMapping(value = {"/plays/search/results/{name}/{id}", "/plays/{name}/{id}"})
    private String getPlayPage(Model model, @PathVariable ("id") String playId) {

        long id = Long.parseLong(playId);

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("comment", new PlayCommentViewModel());
        model.addAttribute("play", playService.findByIdDTO(id));
        model.addAttribute("theatre", new TheatreViewModel());
        model.addAttribute("rating", new Rating());
        model.addAttribute("playRating", playService.getPlayRating(id));
        model.addAttribute("userGivenRating", userService.getUserPlayRating(this.getUser(), id));
        model.addAttribute("userLikedPlay", userService.isPlayLiked(this.getUser(), id));
        model.addAttribute("playCreative", playService.getPlayCreativeTeamData(id));
        model.addAttribute("contacts", playService.getPlayTicketOfficeData(id));
        model.addAttribute("playComments",
                playService.getPlayComments(id)
                        .stream()
                        .map(this::convertToCommentViewModel)
                        .collect(Collectors.toList()));
        model.addAttribute("playGallery",
                playService.getPlayGallery(id)
                        .stream()
                        .map(this::convertToPictureViewModel)
                        .collect(Collectors.toList()));

        return "play";
    }

    @GetMapping("/get-play-news/{name}/{id}")
    public String getActorNews(Model model, @PathVariable String id) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("newsResults", newsService.getPlayNews(playService.findByIdDTO(Long.parseLong(id)))
                .stream()
                .map(this::convertToNewsViewModel)
                .collect(Collectors.toList()));

        return "news-results";
    }

    @PostMapping("/add-comment/{name}/{id}")
    public String addPlayComment(@PathVariable(value = "id") String id,
                                 @Valid @ModelAttribute PlayCommentViewModel playCommentViewModel,
                                 BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "redirect:/plays/search/results/{name}/{id}";
        }
        playService.addComment(id, this.convertToPlayCommentDTO(playCommentViewModel), this.getUser());

        return "redirect:/plays/search/results/{name}/{id}";
    }

    @PostMapping("/edit-comment/{name}/{id}/{playCommentId}")
    public String editPlayComment(@ModelAttribute PlayCommentViewModel playCommentViewModel,
                                  @PathVariable String id,
                                  @PathVariable String playCommentId) {

        playCommentViewModel.setId(Long.parseLong(playCommentId));
        playService.editComment(this.convertToPlayCommentDTO(playCommentViewModel));

        return "redirect:/plays/search/results/{name}/{id}";
    }

    @PostMapping("/delete-comment/{name}/{id}/{playCommentId}")
    public String deletePlayComment(@PathVariable(value = "playCommentId") String playCommentId) {

        playService.deleteComment(playCommentId);

        return "redirect:/plays/search/results/{name}/{id}";
    }
}
