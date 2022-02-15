package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.Actor;
import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.Theatre;
import com.theatreapp.theatre.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "update user " +
            "set is_game_eligible=1 " +
            "where total_activity_score=:requiredScore",
            nativeQuery = true)
    void setScoreGameEligibleUsers(int requiredScore);

    @Query(value = "select * " +
            "from user " +
            "where has_won_ticket=1 ",
            nativeQuery = true)
    User getUserWinner();

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from user_targeted_actors " +
            "where targeted_actors_id=:actor ",
            nativeQuery = true)
    void removeTargetedActor(Actor actor);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from user_targeted_plays " +
            "where targeted_plays_id=:play ",
            nativeQuery = true)
    void removeTargetedPlay(Play play);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from user_targeted_theatres " +
            "where targeted_theatres_id=:theatre ",
            nativeQuery = true)
    void removeTargetedTheatre(Theatre theatre);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from user_favourite_actors " +
            "where favourite_actors_id=:actor ",
            nativeQuery = true)
    void removeFavouriteActor(Actor actor);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from user_favourite_plays " +
            "where favourite_plays_id=:play ",
            nativeQuery = true)
    void removeFavouritePlay(Play play);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from user_favourite_theatres " +
            "where favourite_theatres_id=:theatre ",
            nativeQuery = true)
    void removeFavouriteTheatre(Theatre theatre);

   @Modifying
    @Transactional
    @Query(value = "delete " +
            "from user_watched_actors " +
            "where watched_actors_id=:actor ",
            nativeQuery = true)
    void removeWatchedActor(Actor actor);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from user_watched_plays " +
            "where watched_plays_id=:play ",
            nativeQuery = true)
    void removeWatchedPlay(Play play);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from user_watched_theatres " +
            "where watched_theatres_id=:theatre ",
            nativeQuery = true)
    void removeWatchedTheatre(Theatre theatre);

    @Query(value = "select user.* " +
            "from user " +
            "inner join user_targeted_theatres " +
            "on user.id = user_id " +
            "where targeted_theatres_id=:theatre ",
            nativeQuery = true)
    List<User> getUsersTargetTheatre(Theatre theatre);

    @Query(value = "select user.* " +
            "from user " +
            "inner join user_targeted_actors " +
            "on user.id = user_id " +
            "where targeted_actors_id=:actor ",
            nativeQuery = true)
    List<User> getUsersTargetActor(Actor actor);

    @Query(value = "select user.* " +
            "from user " +
            "inner join user_watched_theatres " +
            "on user.id = user_id " +
            "where watched_theatres_id=:theatre ",
            nativeQuery = true)
    List<User> getUsersWatchedTheatre(Theatre theatre);

    @Query(value = "select user.* " +
            "from user " +
            "inner join user_watched_actors " +
            "on user.id = user_id " +
            "where watched_actors_id=:actor ",
            nativeQuery = true)
    List<User> getUsersWatchedActor(Actor actor);
}
