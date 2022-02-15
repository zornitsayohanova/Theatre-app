package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ActorDAO extends JpaRepository<Actor, Long> {
    List<Actor> findByNameLikeOrBioLike(String keyword, String keyword2);

    @Query(value = "select actor.* " +
                   "from actor " +
                   "inner join actor_rating " +
                   "on actor.id = actor_rating.actor_id " +
                   "where actor_rating.user_id=:user " +
                   "order by actor_rating.actor_id DESC",
            nativeQuery = true)
    List<Actor> findUserRatedActors(User user);

    @Query(value = "select distinct actor.* " +
                   "from actor " +
                   "inner join theatre_actors " +
                   "on theatre_actors.actor_id = actor.id " +
                   "where " +
                   "(:actor is null or actor.id=:actor) and " +
                   "(:actorGender is null or actor.actor_gender_id=:actorGender) and " +
                   "(:theatre is null or theatre_actors.theatre_id=:theatre)",
            nativeQuery = true)
    List<Actor> findByActorOrActorGenderOrTheatre(Actor actor, ActorGender actorGender, Theatre theatre);

    @Modifying
    @Transactional
    void removeActorById(long id);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(value = "delete " +
            "from actor_gallery " +
            "where actor_id=:actor ",
            nativeQuery = true)
    void removeActorGallery(Actor actor);
}