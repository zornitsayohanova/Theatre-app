package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface TheatreRatingDAO extends JpaRepository<TheatreRating, Long> {
    List<TheatreRating> findByTheatre(Theatre theatre);

    @Transactional
    @Modifying
    void deleteAllByUser(User user);

    TheatreRating findByUserAndTheatre(User user, Theatre theatre);

    boolean existsTheatreRatingByUserAndTheatre(User user, Theatre theatre);

    @Query(value = "select theatre_rating.* " +
                   "from theatre_rating " +
                   "inner join theatre on theatre_rating.theatre_id = theatre.id " +
                   "where theatre_rating.user_id=:user " +
                   "order by theatre_rating.theatre_id DESC",
            nativeQuery = true)
    List<TheatreRating> getUserRatedTheatresRating(User user);

    void removeByTheatre(Theatre theatre);
}
