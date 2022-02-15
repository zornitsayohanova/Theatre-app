package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.ActorGenderDAO;
import com.theatreapp.theatre.dto.DTO;
import com.theatreapp.theatre.dto.ActorGenderDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class GenderService extends DTO {
    private final ActorGenderDAO actorGenderDAO;

    public List<ActorGenderDTO> getAllGenders() {
        return actorGenderDAO.findAll()
                .stream()
                .map(this::convertToGenderDTO)
                .collect(Collectors.toList());
    }
}
