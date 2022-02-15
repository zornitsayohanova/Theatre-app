package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TheatreDAO extends JpaRepository<Theatre, Long> {
    List<Theatre> findByNameLike(String name);

    @Query(value = "select * " +
                   "from theatre " +
                   "where " +
                   "(:theatre is null or theatre.id=:theatre) and " +
                   "(:type is null or type_id=:type) and " +
                   "(:town is null or town_id=:town) and " +
                   "(:genre is null or genre_id=:genre)",
            nativeQuery = true)
    List<Theatre> findByGenreOrTypeOrTown(Theatre theatre, TheatreGenre genre, TheatreType type, Town town);

    @Query(value = "select theatre.*" +
                   "from theatre " +
                   "inner join theatre_rating " +
                   "on theatre.id = theatre_rating.theatre_id " +
                   "where theatre_rating.user_id=:user " +
                   "order by theatre_rating.theatre_id DESC",
            nativeQuery = true)
    List<Theatre> findUserRatedTheatres(User user);

    Theatre findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from theatre_actors " +
            "where actor_id=:actor ",
            nativeQuery = true)
    void removeActor(Actor actor);

    @Modifying
    @Transactional
    void removeById(long id);
}