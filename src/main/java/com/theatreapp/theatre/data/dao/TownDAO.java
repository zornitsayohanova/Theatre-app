package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.Town;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TownDAO extends JpaRepository<Town, Long> {
}
