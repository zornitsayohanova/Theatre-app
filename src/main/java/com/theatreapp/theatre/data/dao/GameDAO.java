package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameDAO extends JpaRepository<Game, Long> {
}
