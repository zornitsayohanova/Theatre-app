package com.theatreapp.theatre.data.dao;
import com.theatreapp.theatre.data.entities.ActorGender;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorGenderDAO extends JpaRepository<ActorGender, Long> {

}