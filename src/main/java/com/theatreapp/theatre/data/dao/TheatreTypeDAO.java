package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.TheatreType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheatreTypeDAO extends JpaRepository<TheatreType, Long> {
}