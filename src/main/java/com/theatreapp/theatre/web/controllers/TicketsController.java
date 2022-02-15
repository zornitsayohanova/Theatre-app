package com.theatreapp.theatre.web.controllers;

import com.theatreapp.theatre.data.entities.PlaySeat;
import com.theatreapp.theatre.data.entities.User;
import com.theatreapp.theatre.dto.PlayDTO;
import com.theatreapp.theatre.services.*;
import com.theatreapp.theatre.web.models.KeywordViewModel;
import com.theatreapp.theatre.web.models.PlayViewModel;
import com.theatreapp.theatre.web.models.ViewModel;

import lombok.AllArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class TicketsController extends ViewModel {
    private final TicketsService ticketService;
    private final PlayService playService;
    private final UserServiceImpl userService;
    private final TheatreHallService seatingPlanService;

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByUsername(authentication.getName());
    }

    @GetMapping("/choose-play-date/{name}/{id}")
    public String viewPlayDatesPage(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("playSeat", new PlaySeat());
        model.addAttribute("play", null);

        return "choose-play-date";
    }

    @PostMapping("/choose-play-date/{name}/{id}")
    public String getPlayDatesPage(Model model, @ModelAttribute PlayViewModel playViewModel,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "home";
        }

        playViewModel.setTheatreHall(playService.findByIdDTO(playViewModel.getId()).getTheatreHall());

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("playDates", ticketService.getPlayDates(convertToPlayDTO(playViewModel)));
        model.addAttribute("play", playViewModel);

        return "choose-play-date";
    }

    @GetMapping("/view-play-date-seating-plan/{name}/{id}")
    public String viewPlayDateSeats(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("play", null);

        return "theatre-hall";
    }

    @PostMapping("/view-play-date-seating-plan/{name}/{id}")
    public String getPlayDateSeats(Model model, @ModelAttribute PlayViewModel playViewModel,
                                  BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "redirect:/index";
        }

        if(playViewModel.getDate() == null) {
            model.addAttribute("message", "Въведете дата!");
            return "error-page";
        }

        PlayDTO playDTO = this.convertToPlayDTO(playViewModel);
        playViewModel.setTheatreHall(playService.findByIdDTO(playViewModel.getId()).getTheatreHall());
        Timestamp timestamp = ticketService.findDate(playViewModel.getDate(), playDTO);

        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("play", playViewModel);
        model.addAttribute("date", timestamp);
        model.addAttribute("playReservedSeats", ticketService.getPlayReservedSeats(playDTO, timestamp));
        model.addAttribute("seatingPlanDisabledSeats", seatingPlanService.getTheatreHallDisabledSeats(playDTO));
        model.addAttribute("seatingPlanRows", seatingPlanService.getTheatreHallRows(playDTO));
        model.addAttribute("seatingPlanColumns", seatingPlanService.getTheatreHallColumns(playDTO));
        model.addAttribute("seatsPrices",
                ticketService.getSeatPrices(playDTO)
                        .stream()
                        .map(this::convertToPlayPriceViewModel)
                        .collect(Collectors.toList()));

        return "theatre-hall";
    }

    public void getUserReservedSeatsAttributes(Model model) {
        model.addAttribute("user", this.getUser());
        model.addAttribute("keyword", new KeywordViewModel());
    }

    @GetMapping("/won-seats")
    public String viewUserWonTickets(Model model) {
        this.getUserReservedSeatsAttributes(model);
        model.addAttribute("selectedPlaySeats",
                userService.getAllUserWonSeats());
                    /*    .stream()
                        .map(this::convertToUserReservedSeatViewModel)
                        .collect(Collectors.toList()));*/

        return "user-won-seats";
    }

    @GetMapping("/reserved-seats")
    public String viewUserReservedTickets(Model model) {
        this.getUserReservedSeatsAttributes(model);
        model.addAttribute("selectedPlaySeats",
                userService.getAllUserReservedSeats());
                    /*    .stream()
                        .map(this::convertToUserReservedSeatViewModel)
                        .collect(Collectors.toList()));*/

        return "user-reserved-seats";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/reserved-seats/{playId}/{selectedSeats}/{date}/{currentTime}")
    public String viewUserReservations(Model model, @PathVariable (value = "playId") String playId,
                                      @PathVariable (value = "selectedSeats") String selectedSeats,
                                      @PathVariable String date, @PathVariable String currentTime) throws Exception {
        try {
            List<PlaySeat> seats = ticketService.getUserSelectedSeats(selectedSeats, playId, date, currentTime);
            ticketService.reserveSelectedSeats(seats);
            userService.reserveSelectedSeats(seats, currentTime, this.getUser(), false);
        } catch (Exception e) {
              model.addAttribute("message", e.getMessage());
              return "error-page";
        }
        this.getUserReservedSeatsAttributes(model);

        return "redirect:/reserved-seats";
    }

    @PostMapping("/get-user-reserved-seats")
    public String viewSpecificUserReservedSeats(Model model, @ModelAttribute User user) {
        model.addAttribute("user", user);
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("selectedPlaySeats",
                userService.getSpecificUserReservedSeats(user));
                    /*    .stream()
                        .map(this::convertToUserReservedSeatViewModel)
                        .collect(Collectors.toList()));*/

        return "user-reserved-seats";
    }

    @PostMapping("/get-user-won-seats")
    public String viewSpecificUserWonSeats(Model model, @ModelAttribute User user) {
        model.addAttribute("user", user);
        model.addAttribute("keyword", new KeywordViewModel());
        model.addAttribute("selectedPlaySeats",
                userService.getSpecificUserWonSeats(user));
                    /*    .stream()
                        .map(this::convertToUserReservedSeatViewModel)
                        .collect(Collectors.toList()));*/

        return "user-won-seats";
    }
}




    /*
       try {
            model.addAttribute("reservedSeats", ticketService.getPlayReservedSeats(play, new java.sql.Date(date.getTime())));
        } catch (Exception e) {

        }

      @PostMapping("/play/date/{id}")
    public String getPlayDateTickets(Model model, @ModelAttribute Play play,
                                     @RequestParam (name = "date") @DateTimeFormat(pattern = "yy-MM-dd") Date date,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        Play pl = playService.findById(String.valueOf(play.getId()));

        model.addAttribute("date", date);
        model.addAttribute("theatre", theatreService.findById(String.valueOf(pl.getTheatre().getId())));
        model.addAttribute("play", play);

        return "seating-plan";
    }



@GetMapping("/view-date-reserved-seats/{playId}/{selectedSeats}/{date}")
public String getReser(Model model, @PathVariable (value = "playId") String playId,
@PathVariable (value = "selectedSeats") String selectedSeats,
@PathVariable String date) throws Exception {

        model.addAttribute("reservation", new Reservation());
        model.addAttribute("selectedPlaySeats", ticketService.getUserSelectedSeats(selectedSeats, playId, date));

        return "view-reserved-seats";
        }

@PostMapping("/reserve-selected-seats")
public String reserveSelectedSeats(Model model, @ModelAttribute Reservation reservation,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
        return "index";
        }

        ticketService.reserveSelectedSeats(reservation.getReservedSeats());
        userService.reserveSelectedSeats(reservation.getReservedSeats());

        return "redirect:/my-reserved-seats";
        }
     */
    /*@PostMapping("/reserve-seats")
    public @ResponseBody
    String reserveSeats(@RequestBody SeatData seatData) throws Exception {

        return "";
    }*/
    /*@PostMapping("/save-reserved-seats/{playId}/{selectedSeats}/{date}")
    public String getRes(Model model, @PathVariable (value = "playId") String playId,
                           @PathVariable (value = "selectedSeats") String selectedSeats,
                           @PathVariable String date) throws Exception {

        model.addAttribute("selectedPlaySeats", playService.getSelectedSeats(selectedSeats, playId, date));

        return "save-reserved-seats";
    }*/


    /*  public String klll(Model model, @ModelAttribute Play play,
                    // @RequestParam (name = "date") @DateTimeFormat(pattern = "yy-MM-dd") Date date,
                     @RequestBody Integer[] s) {
*/

 /*  @PostMapping("/dates")
    public String getSelectDatePage(Model model, @ModelAttribute Play play) {
        Play pl = playService.findById(play.getName());

        model.addAttribute("playDates", ticketService.getPlayDates(pl));
        model.addAttribute("play", pl);
        model.addAttribute("playSeat", new PlaySeat());

        return "select-play-date";
    }*/
