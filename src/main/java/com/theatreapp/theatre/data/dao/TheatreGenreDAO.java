package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.TheatreGenre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheatreGenreDAO extends JpaRepository<TheatreGenre, Long> {
}