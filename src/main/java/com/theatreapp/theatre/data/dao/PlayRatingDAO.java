package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface PlayRatingDAO extends JpaRepository<PlayRating, Long> {
    List<PlayRating> findByPlay(Play play);

    @Transactional
    @Modifying
    void deleteAllByUser(User user);

    PlayRating findByUserAndPlay(User user, Play play);

    boolean existsPlayRatingByUserAndPlay(User user, Play play);

    @Modifying
    @Transactional
    void removeAllByPlay(Play play);

    @Query(value = "select play_rating.*" +
                   "from play_rating " +
                   "inner join play " +
                   "on play_rating.play_id = play.id " +
                   "where play_rating.user_id=:user " +
                   "order by play_rating.play_id DESC",
            nativeQuery = true)
    List<PlayRating> getUserRatedPlaysRating(User user);
}