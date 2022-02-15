package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.PlayDAO;
import com.theatreapp.theatre.data.dao.TheatreDAO;
import com.theatreapp.theatre.data.entities.Month;
import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.Theatre;
import com.theatreapp.theatre.dto.DTO;
import com.theatreapp.theatre.dto.PlayDTO;
import com.theatreapp.theatre.dto.TheatreDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ScheduleService extends DTO {
    private final PlayDAO playDAO;

    public List<PlayDTO> getTodaySchedulePlaysInSofia() {
        return playDAO.getTodaySchedulePlaysInSofia()
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public List<PlayDTO> getTodaySchedule(PlayDTO playDTO) {
        if (playDTO.getTown() == null && playDTO.getTheatre() == null)
            return new ArrayList<>();
        return playDAO.getTodaySchedulePlays(playDTO.getTown(), playDTO.getTheatre())
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public List<PlayDTO> getDateSchedule(PlayDTO playDTO) {
        if (playDTO.getDate() == null || (playDTO.getDate() == null && playDTO.getTown() == null && playDTO.getTheatre() == null))
            return new ArrayList<>();

        java.sql.Date date = new java.sql.Date(playDTO.getDate().getTime());

        return playDAO.getDateSchedulePlays(date, playDTO.getTown(), playDTO.getTheatre())
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public List<PlayDTO> getMonthSchedule(PlayDTO playDTO) {
        return playDAO.getMonthSchedulePlays(playDTO.getMonth(), playDTO.getTown(), playDTO.getTheatre())
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public List<PlayDTO> getTheatreTodaySchedule(TheatreDTO theatreDTO) {
        return playDAO.getTheatreTodaySchedulePlays(this.convertToTheatre(theatreDTO))
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public List<PlayDTO> getTheatreDateSchedule(PlayDTO playDTO) {
        if(playDTO.getDate() == null) {
            return new ArrayList<>();
        }
        java.sql.Date date = new java.sql.Date(playDTO.getDate().getTime());

        return playDAO.getTheatreDateSchedulePlays(date, playDTO.getTheatre())
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public List<PlayDTO> getTheatreMonthSchedule(PlayDTO playDTO) {
        String month = playDTO.getMonth();

        return playDAO.getTheatreMonthSchedulePlays(month, playDTO.getTheatre())
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public List<String> getAllMonths() {
        Month months = new Month();
        return months.getMonths();
    }
}
