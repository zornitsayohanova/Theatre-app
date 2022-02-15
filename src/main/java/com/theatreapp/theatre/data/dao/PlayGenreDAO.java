package com.theatreapp.theatre.data.dao;
import com.theatreapp.theatre.data.entities.PlayGenre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayGenreDAO extends JpaRepository<PlayGenre, Long> {
}