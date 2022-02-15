package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.TownDAO;
import com.theatreapp.theatre.dto.DTO;
import com.theatreapp.theatre.dto.TownDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class TownService extends DTO {
    private final TownDAO townDAO;

    public List<TownDTO> getAllTowns() {
        return townDAO.findAll()
                .stream()
                .map(this::convertToTownDTO)
                .collect(Collectors.toList());
    }
}
