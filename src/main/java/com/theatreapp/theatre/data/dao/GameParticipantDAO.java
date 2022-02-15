package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.GameParticipant;
import com.theatreapp.theatre.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

public interface GameParticipantDAO extends JpaRepository<GameParticipant, Long> {
    GameParticipant findByUser(User user);

    @Transactional
    @Modifying
    void deleteAllByUser(User user);
}
