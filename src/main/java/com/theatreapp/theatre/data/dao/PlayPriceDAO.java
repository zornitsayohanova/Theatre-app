package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.Theatre;
import com.theatreapp.theatre.data.entities.PlayPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PlayPriceDAO extends JpaRepository<PlayPrice, Long> {
    List<PlayPrice> findAllByPlayAndTheatre(Play play, Theatre theatre);

    List<PlayPrice> getAllByPlayAndTheatre(Play play, Theatre theatre);

    @Modifying
    @Transactional
    void removeAllByPlay(Play play);

   /* @Modifying
    @Transactional
    @Query(value = "delete " +
            "from play_price " +
            "where " +
            "play_id=:play and theatre_id=:theatre",
            nativeQuery = true)
    void findByPlayAndTheatre(Play play, Theatre theatre);*/
}
