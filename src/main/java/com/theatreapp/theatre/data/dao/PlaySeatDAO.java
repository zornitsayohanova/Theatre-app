package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PlaySeatDAO extends JpaRepository<PlaySeat, Long> {

    @Query(value = "select seat_identifier from play_seat where play_id=:play AND date=:date AND is_free = 0", nativeQuery = true)
    int[] getReservedSeats(Play play, Timestamp date);

    @Query(value = "select distinct date from play_seat where play_id=:play AND is_active = 1 ORDER BY date ASC", nativeQuery = true)
    List<Timestamp> getPlayDates(Play play);

    PlaySeat findAllBySeatIdentifierAndPlayAndDate(String seatIdentifier, Play play, Timestamp date);

    @Modifying
    @Transactional
    void removeAllByPlay(Play play);

    @Modifying
    @Transactional
    void removeAllByPlayAndDate(Play play, Timestamp date);

    List<PlaySeat> findAllByDateAndPlay(Timestamp timestamp, Play play);

    @Query(value = "select distinct date " +
                   "from play_seat " +
                   "where play_id=:play AND DATE(date)=DATE(:timestamp)",
            nativeQuery = true)
    Timestamp findByDateAndPlay(Timestamp timestamp, Play play);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from play_seat " +
            "where " +
            "play_id=:play ",
            nativeQuery = true)
    void removeByPlay(Play play);

    @Query(value = "select play_seat .*" +
            "from play_seat " +
            "inner join play " +
            "on play_seat.play_id=play.id and theatre_id=:theatre " +
            "where play_id=:play and play_seat.date=:date " +
            "limit 1",
            nativeQuery = true)
    PlaySeat findByPlayAndTheatreAndDate(Play play, Theatre theatre, Timestamp date);

    @Query(value = "SELECT ta1.id as first_seat, ta2.id as second_seat " +
            "FROM play_seat ta1, play_seat ta2 " +
            "inner join play " +
            "ON play_id = play.id " +
            "INNER JOIN theatre " +
            "ON play.theatre_id = theatre.id "+
            "inner join town " +
            "ON theatre.town_id = town.id " +
            "WHERE " +
            "town.name = 'София' AND " +
            "ta1.id <> ta2.id AND " +
            "ta1.date = ta2.date AND " +
            "ta1.price_id = ta2.price_id AND " +
            "ta1.seat_identifier + 1 = ta2.seat_identifier AND " +
            "ta1.is_free = 1 and ta2.is_free = 1 AND " +
            "ta1.is_active = 1 and ta2.is_active = 1 " +
            "ORDER BY RAND() LIMIT 1",
            nativeQuery = true)
    String getTwoRandomSeats();
}
