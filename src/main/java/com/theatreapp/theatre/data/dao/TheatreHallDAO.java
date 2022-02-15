package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.Theatre;
import com.theatreapp.theatre.data.entities.TheatreHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TheatreHallDAO extends JpaRepository<TheatreHall, Long> {
    TheatreHall findAllByHallNameAndTheatre(String name, Theatre theatre);

    List<TheatreHall> findAllByTheatre(Theatre theatre);

    void removeAllByTheatre(Theatre theatre);

    @Modifying
    @Transactional
    void removeAllById(long id);
}
