package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ActorRatingDAO extends JpaRepository<ActorRating, Long> {
    List<ActorRating> findByActor(Actor actor);

    @Transactional
    @Modifying
    void deleteAllByUser(User user);

    void removeAllByActor(Actor actor);

    ActorRating findByUserAndActor(User user, Actor actor);

    boolean existsActorRatingByUserAndActor(User user, Actor actor);

    @Query(value = "select actor_rating.* " +
                   "from actor_rating " +
                   "inner join actor " +
                   "on actor_rating.actor_id = actor.id " +
                   "where actor_rating.user_id=:user " +
                   "order by actor_rating.actor_id DESC",
            nativeQuery = true)
    List<ActorRating> getUserRatedActorsRating(User user);
}