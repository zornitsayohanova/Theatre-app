package com.theatreapp.theatre.data.dao;


import com.theatreapp.theatre.data.entities.GameQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameQuestionDAO extends JpaRepository<GameQuestion, Long> {
}
