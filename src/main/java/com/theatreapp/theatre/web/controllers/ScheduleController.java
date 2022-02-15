package com.theatreapp.theatre.web.controllers;

import com.theatreapp.theatre.data.entities.User;
import com.theatreapp.theatre.dto.PlayDTO;
import com.theatreapp.theatre.services.*;
import com.theatreapp.theatre.web.models.*;

import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class ScheduleController extends ViewModel {
    private final TownService townService;
    private final PlayService playService;
    private final TheatreService theatreService;
    private final ScheduleService scheduleService;
    private final UserServiceImpl userService;

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(authentication.getName());
    }

    @GetMapping("/schedule")
    private String viewTodayScheduleMainPage(Model model) {
        List<PlayDTO> playsResults = scheduleService.getTodaySchedulePlaysInSofia();

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("playsResults", playsResults);
        model.addAttribute("playsRatings", playService.getPlaysRating(playsResults));
        model.addAttribute("play", new PlayViewModel());
        model.addAttribute("theatre", new TheatreViewModel());
        model.addAttribute("town", new TownViewModel());

        model.addAttribute("allTowns",
                townService.getAllTowns()
                        .stream()
                        .map(this::convertToTownViewModel)
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

        model.addAttribute("allMonths", scheduleService.getAllMonths());

        return "schedule";
    }

    @PostMapping("/today-schedule")
    private String getTodaySchedule(Model model, @ModelAttribute PlayViewModel playViewModel,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "home";
        }

        try {
            List<PlayDTO> playsResults = scheduleService.getTodaySchedule(convertToPlayDTO(playViewModel));
            model.addAttribute("playsResults",
                    playsResults
                            .stream()
                            .map(this::convertToPlayViewModel)
                            .collect(Collectors.toList()));
            model.addAttribute("playsRatings", playService.getPlaysRating(playsResults));
        } catch (Exception e) {

        }

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        return "plays-results";
    }

    @PostMapping("/date-schedule")
    private String getDateSchedule(Model model, @ModelAttribute PlayViewModel playViewModel,
                                   BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return "home";
        }

        List<PlayDTO> playsResults = scheduleService.getDateSchedule(convertToPlayDTO(playViewModel));

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("playsResults",
                playsResults
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));
        model.addAttribute("playsRatings", playService.getPlaysRating(playsResults));

        return "plays-results";
    }

    @PostMapping("/month-schedule")
    private String getMonthSchedule(Model model, @ModelAttribute PlayViewModel playViewModel,
                                    BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "home";
        }

        List<PlayDTO> playsResults = scheduleService.getMonthSchedule(convertToPlayDTO(playViewModel));

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("playsResults",
                playsResults
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));
        model.addAttribute("playsRatings", playService.getPlaysRating(playsResults));

        return "plays-results";
    }

    ////////////////////bez model
    @GetMapping("/today-schedule/theatre/{name}/{id}")
    private String viewTodayTheatreSchedulePage(Model model, @PathVariable(value = "id") String id, @PathVariable String name) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("playResults", new ArrayList<PlayViewModel>());
        model.addAttribute("play", new PlayViewModel());
        model.addAttribute("theatre", new TheatreViewModel());
        model.addAttribute("allPlays", new PlayViewModel());
        model.addAttribute("allMonths", scheduleService.getAllMonths());

        return "theatre-schedule";
    }

    @PostMapping("/today-schedule/theatre/{name}/{id}")
    private String getTodayTheatreSchedule(Model model, @ModelAttribute TheatreViewModel theatreViewModel,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "home";
        }

        List<PlayDTO> resultPlays = scheduleService.getTheatreTodaySchedule(convertToTheatreDTO(theatreViewModel));

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("results", resultPlays);
        model.addAttribute("play", new PlayViewModel());
        model.addAttribute("theatre", this.convertToTheatreViewModel(theatreService.findByIdDTO(theatreViewModel.getId())));
        model.addAttribute("allMonths", scheduleService.getAllMonths());
        model.addAttribute("allPlays",
                playService.getAllPlays()
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));
        model.addAttribute("playsResults",
                resultPlays
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));

        return "theatre-schedule";
    }

    @PostMapping("/date-schedule/theatre/{name}/{id}")
    private String getDateTheatreSchedule(Model model, @ModelAttribute PlayViewModel playViewModel,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "home";
        }

        List<PlayDTO> resultPlays = scheduleService.getTheatreDateSchedule(convertToPlayDTO(playViewModel));

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("playsResults",
                resultPlays
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));

        return "plays-results";
    }

    @PostMapping("/month-schedule/theatre/{name}/{id}")
    private String getMonthTheatreSchedule(Model model, @ModelAttribute PlayViewModel playViewModel,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "home";
        }

        List<PlayDTO> resultPlays = scheduleService.getTheatreMonthSchedule(convertToPlayDTO(playViewModel));

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("playsResults",
                resultPlays
                        .stream()
                        .map(this::convertToPlayViewModel)
                        .collect(Collectors.toList()));

        return "plays-results";
    }
}
