package com.theatreapp.theatre.web.controllers;

import com.theatreapp.theatre.data.entities.*;
import com.theatreapp.theatre.dto.PlaySeatDTO;
import com.theatreapp.theatre.dto.UserReservedSeatDTO;
import com.theatreapp.theatre.services.*;
import com.theatreapp.theatre.web.models.*;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class AdminController extends ViewModel {
    private final TownService townService;
    private final GenreService genreService;
    private final PlayService playService;
    private final TheatreService theatreService;
    private final ActorService actorService;
    private final PictureService pictureService;
    private final TheatreHallService theatreHallService;
    private final UserServiceImpl userServiceImpl;
    private final GameService gameService;
    private final TicketsService ticketsService;
    private final NewsService newsService;

    public void playPageAttributes(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("allPictures", pictureService.getAllPictures());
        model.addAttribute("allActors", actorService.getAllActors());
        model.addAttribute("allTowns", townService.getAllTowns());
        model.addAttribute("allSeatingPlans", theatreHallService.getAllTheatreHalls());
        model.addAttribute("allTheatres", theatreService.getAllTheatres());
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
        model.addAttribute("allTheatreHalls", theatreHallService.getAllTheatreHalls());
        model.addAttribute("message", "");
    }

    public void actorPageAttributes(Model model)  {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("allGenders", actorService.getAllActorGenders());
        model.addAttribute("allPlays",
                playService.getAllPlays()
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));
        model.addAttribute("allPictures", pictureService.getAllPictures());
        model.addAttribute("message", "");
    }

    public void theatrePageAttributes(Model model)  {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("allPictures", pictureService.getAllPictures());
        model.addAttribute("allActors", actorService.getAllActors());
        model.addAttribute("allTowns", townService.getAllTowns());
        model.addAttribute("allTheatreGenres", theatreService.getAllTheatreGenres());
        model.addAttribute("allTheatreTypes", theatreService.getAllTheatreTypes());
        model.addAttribute("allPlays",
                playService.getAllPlays()
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));
    }

    public void theatreHallPageAttributes(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("allTheatreHalls", theatreHallService.getAllTheatreHalls());
        model.addAttribute("allTheatres", theatreService.getAllTheatres());
        model.addAttribute("message", "");
    }

    public void newsPageAttributes(Model model) {
        model.addAttribute("message", "");
        model.addAttribute("allPictures", pictureService.getAllPictures());
        model.addAttribute("allPlays",
                playService.getAllPlays()
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));
        model.addAttribute("allTheatres", theatreService.getAllTheatres());
        model.addAttribute("allActors", actorService.getAllActors());
    }

    @GetMapping("/add-picture")
    public String viewAddPicturePage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("picture", new NewPictureViewModel());
        model.addAttribute("message", "");

        return "add-picture";
    }

    @PostMapping("/add-picture")
    public String addPicture(Model model, @Valid @ModelAttribute NewPictureViewModel newPictureViewModel,
                             BindingResult bindingResult) throws Exception {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("picture", new NewPictureViewModel());
        model.addAttribute("message", "");
        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "add-picture";
        }

        try {
            pictureService.addPicture(this.convertToNewPictureDTO(newPictureViewModel));
            model.addAttribute("message", "Успешно добавена снимка.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "add-picture";
    }

    @GetMapping("/add-play")
    public String viewAddPlayPage(Model model) {
        model.addAttribute("play", new NewPlayViewModel());
        this.playPageAttributes(model);

        return "add-play";
    }

    @PostMapping("/add-play")
    public String addPlay(Model model, @Valid @ModelAttribute NewPlayViewModel newPlayViewModel,
                             BindingResult bindingResult) {
        model.addAttribute("play", new NewPlayViewModel());
        this.playPageAttributes(model);

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "add-play";
        }

        try {
            playService.addOrEditPlay(this.convertToNewPlayDTO(newPlayViewModel));
            userServiceImpl.setTheatreFromWatchedToTarget(this.convertToNewPlayDTO(newPlayViewModel));
            userServiceImpl.setActorFromWatchedToTarget(this.convertToNewPlayDTO(newPlayViewModel));
            model.addAttribute("message", "Успешно добавена постановка.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "add-play";
    }

    @GetMapping("/select-play-to-edit")
    public String viewEditPlayPage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("play", new NewPlayViewModel());
        model.addAttribute("allPlays",
                playService.getAllPlays()
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));

        return "select-play-to-edit";
    }

    @PostMapping("/get-play-to-edit")
    public String viewPlayToEdit(Model model, @ModelAttribute NewPlayViewModel newPlayViewModel,
                                 BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "home";
        }

        if(newPlayViewModel.getId() == null) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
            model.addAttribute("play", new NewPlayViewModel());
            model.addAttribute("allPlays",
                    playService.getAllPlays()
                            .stream()
                            .map(this::convertToPlayViewModel)
                            .collect(Collectors.toList()));
            return "select-play-to-edit";
        }

        NewPlayViewModel play = this.convertToNewPlayViewModel(playService.findByIdDTO(newPlayViewModel.getId()));
        play.setPlayPrices(playService.getPlayPrices(this.convertToPlayDTO(play)).toString());
        model.addAttribute("play", play);
        this.playPageAttributes(model);

        return "edit-play-form";
    }

    @PostMapping("/edit-play")
    public String editPlay(Model model, @RequestParam ("id") String id, @ModelAttribute NewPlayViewModel newPlayViewModel,
                           BindingResult bindingResult) throws Exception {
        model.addAttribute("play", newPlayViewModel);
        this.playPageAttributes(model);

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "edit-play-form";
        }

        try {
            newPlayViewModel.setId(Long.parseLong(id));
            playService.addOrEditPlay(this.convertToNewPlayDTO(newPlayViewModel));
            model.addAttribute("message", "Успешно редактирана постановка.");

        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "edit-play-form";
    }

    @GetMapping("/delete-play")
    public String viewDeletePlayPage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("play", new NewPlayViewModel());
        model.addAttribute("allPlays", playService.getAllPlays());

        return "delete-play";
    }

    @PostMapping("/delete-play")
    public String deletePlay(Model model, @Valid @ModelAttribute PlayViewModel playViewModel,
                              BindingResult bindingResult) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("play", new NewPlayViewModel());
        model.addAttribute("allPlays", playService.getAllPlays());

        if(bindingResult.hasErrors() || playViewModel.getId() == null) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "delete-play";
        }
        try {
            userServiceImpl.setTheatreFromTargetToWatched(this.convertToPlayDTO(playViewModel));
            userServiceImpl.setActorFromTargetToWatched(this.convertToPlayDTO(playViewModel));
            playService.deletePlay(playService.findByIdDTO(playViewModel.getId()));
            model.addAttribute("message", "Успешно изтрита постановка.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "delete-play";
    }

    @GetMapping("/add-actor")
    public String viewAddActorPage(Model model) {
        model.addAttribute("actor", new NewActorViewModel());
        this.actorPageAttributes(model);

        return "add-actor";
    }

    @PostMapping("/add-actor")
    public String addActor(Model model, @Valid @ModelAttribute NewActorViewModel newActorViewModel,
                           BindingResult bindingResult) {
        model.addAttribute("actor", new NewActorViewModel());
        this.actorPageAttributes(model);

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "add-actor";
        }
        try {
            actorService.addOrEditActor(this.convertToNewActorDTO(newActorViewModel));
            model.addAttribute("message", "Успешно добавен актьор.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "add-actor";
    }

    @GetMapping("/select-actor-to-edit")
    public String viewEditActorPage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("actor", new NewActorViewModel());
        model.addAttribute("allActors",
                actorService.getAllActors()
                        .stream()
                        .map(this::convertToActorViewModel)
                        .collect(Collectors.toList()));

        return "select-actor-to-edit";
    }

    @PostMapping("/get-actor-to-edit")
    public String viewActorToEdit(Model model, @ModelAttribute NewActorViewModel newActorViewModel,
                                  BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }
        if(newActorViewModel.getId() == null) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
            model.addAttribute("actor", new NewActorViewModel());
            model.addAttribute("allActors",
                    actorService.getAllActors()
                            .stream()
                            .map(this::convertToActorViewModel)
                            .collect(Collectors.toList()));
            return "select-actor-to-edit";
        }
        model.addAttribute("actor", this.convertToNewActorViewModel(actorService.findByIdDTO(newActorViewModel.getId())));
        this.actorPageAttributes(model);

        return "edit-actor-form";
    }

    @PostMapping("/edit-actor")
    public String editActor(Model model, @RequestParam ("id") String id, @ModelAttribute NewActorViewModel newActorViewModel,
                            BindingResult bindingResult) throws Exception {
        model.addAttribute("actor", newActorViewModel);
        this.actorPageAttributes(model);

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "edit-actor-form";
        }

        try {
            newActorViewModel.setId(Long.parseLong(id));
            actorService.addOrEditActor(this.convertToNewActorDTO(newActorViewModel));
            model.addAttribute("message", "Успешно редактиран актьор.");

        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "edit-actor-form";
    }

    @GetMapping("/delete-actor")
    public String viewDeleteActorPage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("actor", new NewActorViewModel());
        model.addAttribute("allActors", actorService.getAllActors());

        return "delete-actor";
    }

    @PostMapping("/delete-actor")
    public String deleteActor(Model model, @Valid @ModelAttribute ActorViewModel actorViewModel,
                           BindingResult bindingResult) {
        model.addAttribute("actor", new NewActorViewModel());
        this.actorPageAttributes(model);

        if(bindingResult.hasErrors() || actorViewModel.getId() == null) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "delete-actor";
        }
        try {
            actorService.deleteActor(actorService.findByIdDTO(actorViewModel.getId()));
            model.addAttribute("message", "Успешно изтрит актьор.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "delete-actor";
    }

    @GetMapping("/add-theatre")
    public String viewAddTheatrePage(Model model) {
        model.addAttribute("theatre", new NewTheatreViewModel());
        this.theatrePageAttributes(model);

        return "add-theatre";
    }

    @PostMapping("/add-theatre")
    public String addTheatre(Model model, @Valid @ModelAttribute NewTheatreViewModel newTheatreViewModel,
                             BindingResult bindingResult) throws Exception {
        model.addAttribute("theatre", new NewTheatreViewModel());
        this.theatrePageAttributes(model);

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "add-theatre";
        }

        try {
            theatreService.addOrEditTheatre(this.convertToNewTheatreDTO(newTheatreViewModel));
            model.addAttribute("message", "Успешно добавен театър.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "add-theatre";
    }

    @GetMapping("/select-theatre-to-edit")
    public String viewEditTheatrePage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("theatre", new NewTheatreViewModel());
        model.addAttribute("allTheatres",
                theatreService.getAllTheatres()
                        .stream()
                        .map(this::convertToTheatreViewModel)
                        .collect(Collectors.toList()));

        return "select-theatre-to-edit";
    }

    @PostMapping("/get-theatre-to-edit")
    public String viewTheatreToEdit(Model model, @ModelAttribute NewTheatreViewModel newTheatreViewModel,
                                    BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }
        if(newTheatreViewModel.getId() == null) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
            model.addAttribute("theatre", new NewTheatreViewModel());
            model.addAttribute("allTheatres",
                    theatreService.getAllTheatres()
                            .stream()
                            .map(this::convertToTheatreViewModel)
                            .collect(Collectors.toList()));
            return "select-theatre-to-edit";
        }
        model.addAttribute("theatre", this.convertToNewTheatreViewModel(theatreService.findByIdDTO(newTheatreViewModel.getId())));
        this.theatrePageAttributes(model);

        return "edit-theatre-form";
    }

    @PostMapping("/edit-theatre")
    public String editTheatre(Model model, @RequestParam ("id") String id, @ModelAttribute NewTheatreViewModel newTheatreViewModel,
                              BindingResult bindingResult) throws Exception {
        model.addAttribute("theatre", newTheatreViewModel);
        this.theatrePageAttributes(model);

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "edit-theatre-form";
        }

        try {
            newTheatreViewModel.setId(Long.parseLong(id));
            theatreService.addOrEditTheatre(this.convertToNewTheatreDTO(newTheatreViewModel));
            model.addAttribute("message", "Успешно редактиран театър.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "edit-theatre-form";
    }

    @GetMapping("/delete-theatre")
    public String viewDeleteTheatrePage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("theatre", new NewTheatreViewModel());
        model.addAttribute("allTheatres", theatreService.getAllTheatres());

        return "delete-theatre";
    }

    @PostMapping("/delete-theatre")
    public String deleteTheatre(Model model, @Valid @ModelAttribute TheatreViewModel theatreViewModel,
                              BindingResult bindingResult) {
        model.addAttribute("theatre", new NewTheatreViewModel());
        this.theatrePageAttributes(model);

        if(bindingResult.hasErrors() || theatreViewModel.getId() == null) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "delete-theatre";
        }

        try {
            playService.deleteAllPlaysOfTheatre(theatreService.findByIdDTO(theatreViewModel.getId()));
            theatreHallService.deleteAllTheatreHallsOfTheatre(theatreService.findByIdDTO(theatreViewModel.getId()));
            theatreService.deleteTheatre(theatreService.findByIdDTO(theatreViewModel.getId()));
            model.addAttribute("message", "Успешно изтрит театър.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "delete-theatre";
    }

    @GetMapping("/add-theatre-hall")
    public String viewAddTheatreHallPage(Model model) {
        model.addAttribute("theatreHall", new NewTheatreHallViewModel());
        this.theatreHallPageAttributes(model);

        return "add-theatre-hall";
    }

    @PostMapping("/add-theatre-hall")
    public String addTheatreHallPage(Model model, @Valid @ModelAttribute NewTheatreHallViewModel newTheatreHallViewModel,
                                     BindingResult bindingResult) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("theatreHall", new TheatreHall());
        model.addAttribute("allTheatres", theatreService.getAllTheatres());
        model.addAttribute("message", "");

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "add-theatre-hall";
        }
        try {
            theatreHallService.addOrEditTheatreHall(this.convertToNewTheatreHallDTO(newTheatreHallViewModel));
            model.addAttribute("message", "Успешно добавена сцена.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "add-theatre-hall";
    }

    @GetMapping("/select-theatre-hall-to-edit")
    public String viewEditTheatreHallPage(Model model) {
        model.addAttribute("theatreHall", new NewTheatreHallViewModel());
        this.theatreHallPageAttributes(model);

        return "select-theatre-hall-to-edit";
    }

    @PostMapping("/get-theatre-hall-to-edit")
    public String viewTheatreHallToEdit(Model model, @ModelAttribute NewTheatreHallViewModel newTheatreHallViewModel,
                                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }

        if(newTheatreHallViewModel.getId() == null) {
            model.addAttribute("theatreHall", newTheatreHallViewModel);
            this.theatreHallPageAttributes(model);
            model.addAttribute("message", "Моля, въведете данни във всички полета");

            return "select-theatre-hall-to-edit";
        }

        model.addAttribute("theatreHall",
                this.convertToNewTheatreHallViewModel(theatreHallService.findByIdDTO(newTheatreHallViewModel.getId())));
        this.theatreHallPageAttributes(model);

        return "edit-theatre-hall-form";
    }

    @PostMapping("/edit-theatre-hall")
    public String editTheatreHall(Model model, @RequestParam ("id") String id,
                                  @ModelAttribute NewTheatreHallViewModel newTheatreHallViewModel,
                                  BindingResult bindingResult) throws Exception {
        model.addAttribute("theatreHall", newTheatreHallViewModel);
        this.theatreHallPageAttributes(model);

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "edit-theatre-hall-form";
        }

        try {
            newTheatreHallViewModel.setId(Long.parseLong(id));
            theatreHallService.addOrEditTheatreHall(this.convertToNewTheatreHallDTO(newTheatreHallViewModel));
            model.addAttribute("message", "Успешно редактирана сцена (зала).");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "edit-theatre-hall-form";
    }

    @GetMapping("/delete-theatre-hall")
    public String viewDeleteTheatreHallPage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("theatreHall", new NewTheatreHallViewModel());
        model.addAttribute("allTheatreHalls", theatreHallService.getAllTheatreHalls());

        return "delete-theatre-hall";
    }

    @PostMapping("/delete-theatre-hall")
    public String deleteTheatreHall(Model model, @ModelAttribute NewTheatreHallViewModel newTheatreHallViewModel,
                                BindingResult bindingResult) {
        model.addAttribute("theatreHall", new NewTheatreHallViewModel());
        this.theatreHallPageAttributes(model);

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
            model.addAttribute("theatreHall", new NewTheatreHallViewModel());
            model.addAttribute("allTheatreHalls", theatreHallService.getAllTheatreHalls());

            return "delete-theatre-hall";
        }
        try {
            playService.deleteAllPlaysOfTheatreHall(theatreHallService.findByIdDTO(newTheatreHallViewModel.getId()));
            theatreHallService.deleteTheatreHall(theatreHallService.findByIdDTO(newTheatreHallViewModel.getId()));
            model.addAttribute("message", "Успешно изтрита зала.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "delete-theatre-hall";
    }

    @GetMapping("/add-play-date")
    public String viewAddPlayDatePage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("playDate", new PlayDateViewModel());
        model.addAttribute("allTheatres", theatreService.getAllTheatres());
        model.addAttribute("allPlays",
                playService.getAllPlays()
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));

        return "add-play-date";
    }

    @PostMapping("/add-play-date")
    public String addPlayDate(Model model, @Valid @ModelAttribute PlayDateViewModel playDateViewModel,
                              BindingResult bindingResult) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
            model.addAttribute("playDate", new PlayDateViewModel());
            model.addAttribute("allTheatres", theatreService.getAllTheatres());
            model.addAttribute("allPlays",
                    playService.getAllPlays()
                            .stream()
                            .map(this::convertToPlayViewModel)
                            .collect(Collectors.toList()));

            return "add-play-date";
        }

        try {
            playService.addPlayDate(this.convertToPlayDateDTO(playDateViewModel));
            model.addAttribute("message", "Успешно добавена дата.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        model.addAttribute("playDate", playDateViewModel);
        model.addAttribute("allTheatres", theatreService.getAllTheatres());
        model.addAttribute("allPlays",
                playService.getAllPlays()
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));

        return "add-play-date";
    }

    @GetMapping("/get-play")
    public String viewPlayOfDateToDeletePage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("play", new PlayViewModel());
        model.addAttribute("allPlays", playService.getAllPlays());

        return "select-play-of-date-to-delete";
    }

    @PostMapping("/get-play-dates")
    public String getPlayDates(Model model, @Valid @ModelAttribute PlayViewModel playViewModel) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        if(playViewModel.getId() == null) {
            model.addAttribute("play", new PlayViewModel());
            model.addAttribute("allPlays", playService.getAllPlays());
            model.addAttribute("message", "Моля, въведете данни във всички полета");

            return "select-play-of-date-to-delete";
        }
        model.addAttribute("play", playViewModel);
        model.addAttribute("allPlayDates", playService.getPlayDates(playService.findByIdDTO(playViewModel.getId())));

        return "delete-play-date";
    }

    @PostMapping("/delete-play-date")
    public String deletePlayDate(Model model, @RequestParam("id") String id,
                                 @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss") Date date) {
        PlayViewModel playViewModel = this.convertToPlayViewModel(playService.findByIdDTO(Long.parseLong(id)));
        model.addAttribute("play", playViewModel);
        model.addAttribute("allPlayDates", playService.getPlayDates(playService.findByIdDTO(Long.parseLong(id))));
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        if(date == null) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "delete-play-date";
        }

        try {
            playService.deletePlayDate(playService.findByIdDTO(Long.parseLong(id)), date);
            model.addAttribute("message", "Успешно изтрита дата.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "delete-play-date";
    }

    @GetMapping("/add-news")
    public String viewAddNewsPage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("news", new NewNewsViewModel());
        this.newsPageAttributes(model);

        return "add-news";
    }

    @PostMapping("/add-news")
    public String addNews(Model model, @Valid @ModelAttribute NewNewsViewModel newNewsViewModel,
                             BindingResult bindingResult) throws Exception {
        model.addAttribute("news", new NewNewsViewModel());
        this.newsPageAttributes(model);

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "add-news";
        }

        try {
            newsService.addOrEditNews(this.convertToNewsDTO(newNewsViewModel));
            model.addAttribute("message", "Успешно добавена новина.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "add-news";
    }

    @GetMapping("/select-news-to-edit")
    public String viewEditNewsPage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("news", new NewNewsViewModel());
        model.addAttribute("allNews", newsService.getAllNews());

        return "select-news-to-edit";
    }

    @PostMapping("/get-news-to-edit")
    public String viewNewsToEdit(Model model, @ModelAttribute NewNewsViewModel newNewsViewModel,
                                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "home";
        }
        if(newNewsViewModel.getId() == null) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
            model.addAttribute("news", new NewNewsViewModel());
            model.addAttribute("allNews", newsService.getAllNews());

            return "select-news-to-edit";
        }
        model.addAttribute("news",
                this.convertToNewsViewModel(newsService.findByIdDTO(newNewsViewModel.getId())));
        this.newsPageAttributes(model);

        return "edit-news-form";
    }

    @PostMapping("/edit-news")
    public String editNews(Model model, @RequestParam ("id") String id,
                                  @ModelAttribute NewNewsViewModel newNewsViewModel,
                                  BindingResult bindingResult) throws Exception {
        model.addAttribute("news", newNewsViewModel);
        this.newsPageAttributes(model);

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "edit-news-form";
        }

        try {
            newNewsViewModel.setId(Long.parseLong(id));
            newsService.addOrEditNews(this.convertToNewsDTO(newNewsViewModel));
            model.addAttribute("message", "Успешно редактирана новина.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "edit-news-form";
    }

    @GetMapping("/delete-news")
    public String viewDeleteNewsPage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("news", new NewNewsViewModel());
        model.addAttribute("allNews", newsService.getAllNews());

        return "delete-news";
    }

    @PostMapping("/delete-news")
    public String deleteNews(Model model, @Valid @ModelAttribute NewsViewModel newsViewModel,
                                    BindingResult bindingResult) {
        model.addAttribute("news", new NewNewsViewModel());
        this.newsPageAttributes(model);

        if(bindingResult.hasErrors() || newsViewModel.getId() == null) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            return "delete-news";
        }
        try {
            newsService.deleteNews(newsService.findByIdDTO(newsViewModel.getId()));
            model.addAttribute("message", "Успешно изтрита новина.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "delete-news";
    }

    @GetMapping("/find-user")
    public String viewFindUserPage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("user", new User());
        model.addAttribute("allUsers", userServiceImpl.getAllUsers());

        return "find-user";
    }

    @PostMapping("/delete-seat/{id}")
    public String deleteSeat(Model model, @PathVariable("id") String id, @RequestParam String userId) {
        try {
            userServiceImpl.deleteSeatFromReserved(id, userId);
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "error-page";
        }

        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("user", new User());
        model.addAttribute("allUsers", userServiceImpl.getAllUsers());

        return "redirect:/find-user";
    }

    @GetMapping("/choose-game-condition")
    public String viewGameConditionPage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());

        return "add-game-condition";
    }

    @GetMapping("/pick-week-game-condition-points")
    public String viewPickGameConditionPointsPage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());

        return "game-condition-points";
    }

    @PostMapping("/send-week-game-condition-points")
    public String getPickGameConditionPointsPage(Model model, @RequestParam String points) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());

        try {
            gameService.setGameConditionPoints(points);
            model.addAttribute("message", "Успешно създадена игра с условие 'точки'.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "game-condition-points";
    }

    @GetMapping("/pick-week-game-condition-genre")
    public String viewPickGameConditionGenrePage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("genre", new PlayGenreViewModel());
        model.addAttribute("allGenres",
                genreService.getAllGenres()
                        .stream()
                        .map(this::convertToGenreViewModel)
                        .collect(Collectors.toList()));

        return "game-condition-genre";
    }

    @PostMapping("/send-week-game-condition-genre")
    public String getPickGameConditionGenrePage(Model model, @ModelAttribute PlayGenreViewModel playGenreViewModel) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("genre", new PlayGenreViewModel());
        model.addAttribute("allGenres",
                genreService.getAllGenres()
                        .stream()
                        .map(this::convertToGenreViewModel)
                        .collect(Collectors.toList()));
        try {
            gameService.setGameConditionGenre(this.convertToPlayGenreDTO(playGenreViewModel));
            model.addAttribute("message", "Успешно създадена игра с условие 'жанр'.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "game-condition-genre";
    }

    @GetMapping("/pick-week-game-condition-theatre")
    public String viewPickGameConditionTheatrePage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("theatre", new TheatreViewModel());
        model.addAttribute("allTheatres", theatreService.getAllTheatres());

        return "game-condition-theatre";
    }

    @PostMapping("/send-week-game-condition-theatre")
    public String getPickGameConditionTheatrePage(Model model, @ModelAttribute TheatreViewModel theatreViewModel) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("theatre", new TheatreViewModel());
        model.addAttribute("allTheatres", theatreService.getAllTheatres());
        try {
            gameService.setGameConditionTheatre(this.convertToTheatreDTO(theatreViewModel));
            model.addAttribute("message", "Успешно създадена игра с условие 'театър'.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "game-condition-theatre";
    }

    @GetMapping("/add-new-game-question")
    public String viewAddNewGameQuestionPage(Model model) {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("question", new NewGameAnswerViewModel());

        return "add-game-question";
    }

    @PostMapping("/add-new-game-question")
    public String getAddNewGameQuestionPage(Model model, @Valid @ModelAttribute NewGameAnswerViewModel newGameAnswerViewModel,
                                            BindingResult bindingResult) throws Exception {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("question", new GameAnswerViewModel());

        if(bindingResult.hasErrors()) {
            model.addAttribute("message", "Моля, въведете данни във всички полета");
            model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
            model.addAttribute("question", new GameAnswerViewModel());

            return "add-game-question";
        }

        try {
            gameService.addGameQuestion(this.convertToGameAnswerDTO(newGameAnswerViewModel));
            model.addAttribute("message", "Успешно добавен въпрос.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "add-game-question";
    }

    @GetMapping("/choose-won-seats")
    public String viewChooseWonSeatsPage(Model model) {
        model.addAttribute("message", "");
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("selectedPlaySeats", new ArrayList<UserReservedSeat>());
        model.addAttribute("game", gameService.getGame());

        return "won-seats";
    }

    @PostMapping("/choose-won-seats")
    public String getChooseWonSeatsPage(Model model) throws Exception {
        model.addAttribute("existsUserWinner", userServiceImpl.checkIfUserWinnerExists());
        model.addAttribute("game", gameService.getGame());

        if(userServiceImpl.getWonSeats().size() != 0) {
            model.addAttribute("selectedPlaySeats", new ArrayList<UserReservedSeat>());
            model.addAttribute("message", ("Вече сте избрали печеливши билети за тази седмица!"));

            return "won-seats";
        }

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        try {
            List<PlaySeat> playSeats = ticketsService.reserveAndViewRandomSeatsForWinner();
            userServiceImpl.reserveSelectedSeats(playSeats, time, userServiceImpl.getUserWinner(), true);
            model.addAttribute("selectedPlaySeats", userServiceImpl.getWonSeats());
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }

        return "won-seats";
    }

    @GetMapping("/start-game")
    public String viewStartGamePage(Model model) {
        if (gameService.getAllGameQuestions().size() != 5 || gameService.getGame() == null) {
            model.addAttribute("message", "Първо създайте условие и/или въпроси!");
        }
        return "start-game";
    }

    @PostMapping("/start-quiz-game")
    public String startGame(Model model) {
        gameService.removePreviousWonTicketsFlag();
        gameService.setEligibleUsers();
        model.addAttribute("message", "Играта е стартирана успешно!");

        return "start-game";
    }
}
