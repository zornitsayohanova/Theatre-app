package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.*;
import com.theatreapp.theatre.data.entities.Actor;
import com.theatreapp.theatre.dto.ActorDTO;
import com.theatreapp.theatre.dto.DTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class SearchService extends DTO {
    private final PlayDAO playDAO;
    private final ActorDAO actorDAO;
    private final TheatreDAO theatreDAO;
    private final TheatreGenreDAO theatreGenreDAO;
    private final TheatreTypeDAO theatreTypeDAO;
    private final TheatreRatingDAO theatreRatingDAO;
}
