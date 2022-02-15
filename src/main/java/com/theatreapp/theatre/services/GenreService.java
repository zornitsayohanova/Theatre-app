package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.GameDAO;
import com.theatreapp.theatre.data.dao.GameQuestionDAO;
import com.theatreapp.theatre.data.dao.PlayGenreDAO;
import com.theatreapp.theatre.data.dao.TheatreDAO;
import com.theatreapp.theatre.data.entities.Game;
import com.theatreapp.theatre.data.entities.GameQuestion;
import com.theatreapp.theatre.data.entities.PlayGenre;
import com.theatreapp.theatre.data.entities.TheatreGenre;
import com.theatreapp.theatre.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class GenreService extends DTO {
    private final PlayGenreDAO playGenreDAO;

    public List<PlayGenreDTO> getAllGenres() {
        return playGenreDAO.findAll()
                .stream()
                .map(this::convertToGenreDTO)
                .collect(Collectors.toList());
    }
}