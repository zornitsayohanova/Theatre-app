package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.PlaySeat;
import com.theatreapp.theatre.data.entities.User;
import com.theatreapp.theatre.data.entities.UserReservedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserReservedSeatDAO extends JpaRepository<UserReservedSeat, Long>  {
    @javax.transaction.Transactional
    @Modifying
    void deleteAllByUser(User user);

    @Query(value = "select * " +
            "from user_reserved_seat " +
            "inner join play_seat " +
            "on user_reserved_seat.play_seat_id = play_seat.id " +
            "where user_reserved_seat.user_id =:user AND user_reserved_seat.is_won = false " +
            "order by date ASC",
            nativeQuery = true)
    List<UserReservedSeat> getUserReservedSeatsByDate(User user);

    @Query(value = "select * " +
            "from user_reserved_seat " +
            "inner join play_seat " +
            "on user_reserved_seat.play_seat_id = play_seat.id " +
            "where user_reserved_seat.user_id =:user AND user_reserved_seat.is_won = true " +
            "order by date ASC",
            nativeQuery = true)
    List<UserReservedSeat> getUserWonSeatsByDate(User user);

    @Query(value = "select * " +
            "from user_reserved_seat " +
            "inner join play_seat " +
            "on user_reserved_seat.play_seat_id = play_seat.id " +
            "where user_reserved_seat.is_won = true " +
            "order by date ASC",
            nativeQuery = true)
    List<UserReservedSeat> getWonSeatsByDate();

    @Modifying
    @Transactional
    @Query(value = "delete from user_reserved_seat " +
            "where id=:userReservedSeat ",
            nativeQuery = true)
    void deleteSeatFromReserved(UserReservedSeat userReservedSeat);

    @Modifying
    @Transactional
    void deleteAllByPlaySeat(PlaySeat playSeat);
}