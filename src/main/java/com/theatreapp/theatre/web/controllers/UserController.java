package com.theatreapp.theatre.web.controllers;

import com.theatreapp.theatre.data.entities.*;
import com.theatreapp.theatre.dto.GameAnswerDTO;
import com.theatreapp.theatre.services.*;
import com.theatreapp.theatre.web.models.*;

import lombok.AllArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class UserController extends ViewModel {
    private final PlayService playService;
    private final TheatreService theatreService;
    private final ActorService actorService;
    private final UserServiceImpl userService;
    private final GameService gameService;

    public void getTargetedPlaysPageAttributes(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("message", "");
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("play", new PlayViewModel());
        model.addAttribute("targetedPlays", userService.getUserTargetedPlays()
               /* .stream()
                .map(this::convertToPlayViewModel)
                .collect(Collectors.toList())*/);
        model.addAttribute("allPlays", playService.getAllPlays()
                .stream()
                .map(this::convertToPlayViewModel)
                .collect(Collectors.toList()));
        model.addAttribute("watchedPlays", userService.getUserWatchedPlays()
                /*.stream()
                .map(this::convertToPlayViewModel)
                .collect(Collectors.toList())*/);
    }

    public void getTargetedActorsPageAttributes(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("message", "");
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("actor", new ActorViewModel());
        model.addAttribute("allActors", actorService.getAllActors()
                .stream()
                .map(this::convertToActorViewModel)
                .collect(Collectors.toList()));
        model.addAttribute("targetedActors", userService.getUserTargetedActors()
                /*.stream()
                .map(this::convertToActorViewModel)
                .collect(Collectors.toList())*/);
        model.addAttribute("watchedActors", userService.getUserWatchedActors()
               /* .stream()
                .map(this::convertToActorViewModel)
                .collect(Collectors.toList())*/);
    }

    public void getTargetedTheatresPageAttributes(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("message", "");
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("theatre", new TheatreViewModel());
        model.addAttribute("allTheatres", theatreService.getAllTheatres()
                .stream()
                .map(this::convertToTheatreViewModel)
                .collect(Collectors.toList()));
        model.addAttribute("targetedTheatres", userService.getUserTargetedTheatres()
                /*.stream()
                .map(this::convertToTheatreViewModel)
                .collect(Collectors.toList())*/);
        model.addAttribute("watchedTheatres",
                userService.getUserWatchedTheatres()
               /*         .stream()
                        .map(this::convertToTheatreViewModel)
                        .collect(Collectors.toList())*/);
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    @GetMapping
    public String getBasePage(Model model) {
        model.addAttribute("keyword", new KeywordViewModel());

        return "base-fragment";
    }

    @GetMapping("/profile")
    private String viewUserProfilePage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("newUser", new User());
        model.addAttribute("keyword", new KeywordViewModel());

        return "user-profile";
    }

    @GetMapping("/favourite-actors")
    public String viewFavouriteActorsPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("favouriteActors",
                userService.getFavouriteActors()
                        /*.stream()
                        .map(this::convertToActorViewModel)
                        .collect(Collectors.toList())*/);

        return "user-favourite-actors";
    }

    @GetMapping("/favourite-plays")
    public String viewFavouritePlaysPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("favouritePlays",
                userService.getFavoritePlays()
                        /*.stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList())*/);

        return "user-favourite-plays";
    }

    @GetMapping("/favourite-theatres")
    public String viewFavouriteTheatresPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("favouriteTheatres",
                userService.getFavoriteTheatres()
                        /*.stream()
                        .map(this::convertToTheatreViewModel)
                        .collect(Collectors.toList())*/);

        return "user-favourite-theatres";
    }

    @GetMapping("/rated-actors")
    public String viewRatedActorsPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("rating", new RatingViewModel());
        model.addAttribute("userRatedActorsRating", userService.getRatedActorsRating());
        model.addAttribute("userRatedActors", userService.getUserRatedActors()
               /* .stream()
                .map(this::convertToActorViewModel)
                .collect(Collectors.toList())*/);

        return "user-rated-actors";
    }

    @GetMapping("/rated-plays")
    public String viewRatedPlaysPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("rating", new RatingViewModel());
        model.addAttribute("userRatedPlaysRating", userService.getRatedPlaysRating());
        model.addAttribute("userRatedPlays",
                userService.getUserRatedPlays()
                        /*.stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList())*/);

        return "user-rated-plays";
    }

    @GetMapping("/rated-theatres")
    public String viewRatedTheatresPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("rating", new RatingViewModel());
        model.addAttribute("userRatedTheatresRating", userService.getRatedTheatresRating());
        model.addAttribute("userRatedTheatres",
                userService.getUserRatedTheatres()
                        /*.stream()
                        .map(this::convertToTheatreViewModel)
                        .collect(Collectors.toList())*/);

        return "user-rated-theatres";
    }

    @PostMapping("/rate-actor/{name}/{id}")
    public String rateActor(@PathVariable(value = "id") String id, @ModelAttribute Rating rating) {

        userService.rateActor(id, rating);

        return "redirect:/actors/search/results/{name}/{id}";
    }

    @PostMapping("/change-actor-rating/{name}/{id}")
    public String changeActorRating(@PathVariable(value = "id") String id, @ModelAttribute Rating rating) {

        userService.rateActor(id, rating);

        return "redirect:/rated-actors";
    }

    @PostMapping("/rate-play/{name}/{id}")
    public String ratePlay(@PathVariable(value = "id") String id, @ModelAttribute Rating rating,
                           BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }

        userService.ratePlay(id, rating);

        return "redirect:/plays/search/results/{name}/{id}";
    }

    @PostMapping("/change-play-rating/{name}/{id}")
    public String changePlayRating(@PathVariable(value = "id") String id, @ModelAttribute Rating rating) {

        userService.ratePlay(id, rating);

        return "redirect:/rated-plays";
    }

    @PostMapping("/rate-theatre/{name}/{id}")
    public String rateTheatre(@PathVariable(value = "id") String id, @ModelAttribute Rating rating,
                              BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }

        userService.rateTheatre(id, rating);

        return "redirect:/theatres/search/results/{name}/{id}";
    }

    @PostMapping("/change-theatre-rating/{name}/{id}")
    public String changeTheatreRating(@PathVariable(value = "id") String id, @ModelAttribute Rating rating) {

        userService.rateTheatre(id, rating);

        return "redirect:/rated-theatres";
    }

    @GetMapping("/targeted-actors")
    public String viewTargetedActorsPage(Model model) {
        this.getTargetedActorsPageAttributes(model);

        return "user-targeted-and-watched-actors";
    }

    @GetMapping("/targeted-plays")
    public String viewTargetedPlaysPage(Model model) {
        this.getTargetedPlaysPageAttributes(model);

        return "user-targeted-and-watched-plays";
    }

    @GetMapping("/targeted-theatres")
    public String viewTargetedTheatresPage(Model model) {
        this.getTargetedTheatresPageAttributes(model);

        return "user-targeted-and-watched-theatres";
    }

    @PostMapping("/target-play")
    public String targetPlay(Model model, @ModelAttribute PlayViewModel playViewModel,
                                BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }

        this.getTargetedPlaysPageAttributes(model);
        try {
            userService.targetPlay(this.convertToPlayDTO(playViewModel));
            model.addAttribute("message","Успешно набелязана постановка! Върнете се назад.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "user-targeted-and-watched-plays";
    }

    @PostMapping("/target-actor")
    public String targetActor(Model model, @ModelAttribute ActorViewModel actorViewModel,
                                BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }

        this.getTargetedActorsPageAttributes(model);
        try {
            userService.targetActor(this.convertToActorDTO(actorViewModel));
            model.addAttribute("message","Успешно набелязан актьор! Върнете се назад.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "user-targeted-and-watched-actors";
    }

    @PostMapping("/target-theatre")
    public String targetTheatre(Model model, @ModelAttribute TheatreViewModel theatreViewModel,
                                           BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }

        this.getTargetedTheatresPageAttributes(model);
        try {
            userService.targetTheatre(this.convertToTheatreDTO(theatreViewModel));
            model.addAttribute("message","Успешно набелязан театър! Върнете се назад.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "user-targeted-and-watched-theatres";
    }

    @PostMapping("/get-targeted-actor-progress/{id}")
    public String getUserActorProgress(Model model, @ModelAttribute ActorViewModel actorViewModel,
                                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }

        long id = actorViewModel.getId();

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("actor", this.convertToActorViewModel(actorService.findByIdDTO(id)));
        model.addAttribute("actorWatchedPlays",
                userService.getActorWatchedPlays(id)
                        /*.stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList())*/);
        model.addAttribute("actorUnwatchedPlays",
                userService.getActorUnwatchedPlays(id)
                      /*  .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList())*/);

        return "user-actor-progress";
    }

    @PostMapping("/get-targeted-theatre-progress/{id}")
    public String getUserPlayProgress(Model model, @ModelAttribute TheatreViewModel theatreViewModel,
                                      BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }

        long id = theatreViewModel.getId();

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("theatre", this.convertToTheatreViewModel(theatreService.findByIdDTO(id)));
        model.addAttribute("theatreWatchedPlays",
                userService.getTheatreWatchedPlays(id));
                       /* .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));*/
        model.addAttribute("theatreUnwatchedPlays",
                userService.getTheatreUnwatchedPlays(id)
                       /* .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList())*/);

        return "user-theatre-progress";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/add-actor-to-favourites/{name}/{id}")
    public String addActorToFavourites(@PathVariable(value = "id") String id) {

        userService.addToFavouriteActors(id);

        return "redirect:/actors/search/results/{name}/{id}";
    }

    @PostMapping("/add-play-to-favourites/{name}/{id}")
    public String addPlayToFavourites(@PathVariable(value = "id") String id) {

        userService.addToFavouritePlays(id);

        return "redirect:/plays/search/results/{name}/{id}";
    }

    @PostMapping("/add-theatre-to-favourites/{name}/{id}")
    public String addTheatreToFavourites(@PathVariable(value = "id") String id) {

        userService.addToFavouriteTheatres(id);

        return "redirect:/theatres/search/results/{name}/{id}";
    }

    @PostMapping("/remove-targeted-actor/{id}")
    public String removeActorFromTargeted(@PathVariable(value = "id") String id) {
        userService.removeTargetedActor(id);

        return "redirect:/targeted-actors";
    }

    @PostMapping("/remove-targeted-play/{id}")
    public String removePlayFromTargeted(@PathVariable(value = "id") String id) {

        userService.removeTargetedPlay(id);

        return "redirect:/targeted-plays";
    }

    @PostMapping("/remove-targeted-theatre/{id}")
    public String removeTheatreFromTargeted(@PathVariable(value = "id") String id) {

        userService.removeTargetedTheatre(id);

        return "redirect:/targeted-theatres";
    }

    @PostMapping("/remove-actor-from-favourites-list/{name}/{id}")
    public String removeActorFromFavouritesList(@PathVariable String id) {

        userService.removeFromFavouriteActors(id);

        return "redirect:/favourite-actors";
    }

    @PostMapping("/remove-actor-from-favourites/{name}/{id}")
    public String removeActorFromFavourites(@PathVariable(value = "id") String id) {

        userService.removeFromFavouriteActors(id);

        return "redirect:/actors/{name}/{id}";
    }

    @PostMapping("/remove-play-from-favourites-list/{name}/{id}")
    public String removePlayFromFavouritesList(@PathVariable String id) {

        userService.removeFromFavouritePlays(id);

        return "redirect:/favourite-plays";
    }

    @PostMapping("/remove-play-from-favourites/{name}/{id}")
    public String removePlayFromFavourites(@PathVariable(value = "id") String id) {

        userService.removeFromFavouritePlays(id);

        return "redirect:/plays/{name}/{id}";
    }

    @PostMapping("/remove-theatre-from-favourites-list/{name}/{id}")
    public String removeTheatreFromFavouritesList(@PathVariable String id) {

        userService.removeFromFavouriteTheatres(id);

        return "redirect:/favourite-theatres";
    }

    @PostMapping("/remove-theatre-from-favourites/{name}/{id}")
    public String removeTheatreFromFavourites(@PathVariable String id) {

        userService.removeFromFavouriteTheatres(id);

        return "redirect:/theatres/{name}/{id}";
    }

    @GetMapping("/game-rules-and-user-statistics")
    public String viewGamePage(Model model) throws IllegalAccessException {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("message", "");
        model.addAttribute("hasAnswered", gameService.hasUserAnswered(this.getUser()));
        model.addAttribute("gameTitleOfCondition", gameService.getTitleOfCondition());
        model.addAttribute("genresNames", userService.getUserWatchedGenresNames(this.getUser()));
        model.addAttribute("genresPercentages", userService.getUserWatchedGenresPercentages(this.getUser()));
        model.addAttribute("theatresNames", userService.getUserWatchedTheatreNames(this.getUser()));
        model.addAttribute("theatresPercentages", userService.getUserWatchedTheatrePercentages(this.getUser()));

        return "user-game-and-statistics";
    }

    @GetMapping("/view-game-quiz")
    public String viewGameQuizPage(Model model) throws Exception {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("message", "");
        model.addAttribute("hasAnswered", gameService.hasUserAnswered(this.getUser()));
        GameQuestionViewModel gameQuestionViewModel = new GameQuestionViewModel();
        gameQuestionViewModel.setAllQuestions(gameService.getAllGameQuestions());
        model.addAttribute("quiz", gameQuestionViewModel);

        try {
            gameService.createGameParticipant(this.getUser());
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "game-quiz";
    }

    @PostMapping("/send-game-quiz")
    public String sendGameQuizPage(Model model, @ModelAttribute GameQuestionViewModel gameQuestionViewModel) throws ParseException {
        GameParticipant gameParticipant =
                gameService.sendQuizAnswer(this.convertToGameQuestionDTO(gameQuestionViewModel), this.getUser());

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("message", "Вашият резултат: " + gameParticipant.getCorrectAnswersCount() + "/5");
        gameQuestionViewModel.setAllQuestions(gameService.getAllGameQuestions());
        model.addAttribute("quiz", gameQuestionViewModel);

        return "game-quiz";
    }
}
