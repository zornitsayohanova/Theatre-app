package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.PlayDAO;
import com.theatreapp.theatre.data.dao.PlayPriceDAO;
import com.theatreapp.theatre.data.dao.PlaySeatDAO;
import com.theatreapp.theatre.data.dao.TheatreHallDAO;
import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.PlaySeat;
import com.theatreapp.theatre.data.entities.Theatre;
import com.theatreapp.theatre.data.entities.TheatreHall;
import com.theatreapp.theatre.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class TheatreHallService extends DTO {
    private final TheatreHallDAO theatreHallDAO;
    private final PlayDAO playDAO;
    private final PlaySeatDAO playSeatDAO;
    private final PlayPriceDAO playPriceDAO;

    public TheatreHallDTO findByIdDTO(long id) {
        return this.convertToTheatreHallDTO(theatreHallDAO.findById(id).get());
    }

    public List<TheatreHallDTO> getAllTheatreHalls() {
        return theatreHallDAO.findAll()
                .stream()
                .map(this::convertToTheatreHallDTO)
                .collect(Collectors.toList());
    }

    public int getTheatreHallColumns(PlayDTO playDTO) {
        Play play = playDAO.findById(playDTO.getId()).get();

        return theatreHallDAO.findById(play.getTheatreHall().getId()).get().getHallColumns();
    }

    public int getTheatreHallRows(PlayDTO playDTO) {
        Play play = playDAO.findById(playDTO.getId()).get();

        return theatreHallDAO.findById(play.getTheatreHall().getId()).get().getHallRows();
    }

    public String getTheatreHallDisabledSeats(PlayDTO playDTO) {
        Play play = playDAO.findById(playDTO.getId()).get();

        return theatreHallDAO.findById(play.getTheatreHall().getId()).get().getDisabledSeats();
    }

    public void addOrEditTheatreHall(TheatreHallDTO theatreHallDTO) throws Exception {
        TheatreHall theatreHall = this.convertToTheatreHall(theatreHallDTO);

        if (theatreHallDTO.getId() == 0 && theatreHallDAO.findAllByHallNameAndTheatre(theatreHall.getHallName(), theatreHall.getTheatre()) != null) {
           throw new Exception("Сцената вече съществува!");
        }

        theatreHallDAO.save(theatreHall);
    }

    public void deleteAllTheatreHallsOfTheatre(TheatreDTO theatreDTO) {
        Theatre theatre = this.convertToTheatre(theatreDTO);
        theatreHallDAO.findAllByTheatre(theatre).forEach(th -> this.deleteTheatreHall(this.convertToTheatreHallDTO(th)));
    }

    public void deleteTheatreHall(TheatreHallDTO theatreHallDTO) {
        TheatreHall theatreHall = this.convertToTheatreHall(theatreHallDTO);

        theatreHallDAO.removeAllById(theatreHall.getId());
    }
}


/* if(theatreHallDTO.getId() != 0 && theatreHallDAO.findAllByHallNameAndTheatre(theatreHall.getHallName(), theatreHall.getTheatre()) != null) {
            List<Play> plays = playDAO.findByTheatreHall(theatreHall);
            for (Play play : plays) {
                playSeatDAO.removeByPlay(play);
            }
        }*/