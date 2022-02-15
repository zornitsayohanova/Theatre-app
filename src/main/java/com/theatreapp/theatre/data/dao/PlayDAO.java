package com.theatreapp.theatre.data.dao;
import com.theatreapp.theatre.data.entities.*;

import com.theatreapp.theatre.data.entities.GenrePercentage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface PlayDAO extends JpaRepository<Play, Long> {
    List<Play> findByNameLike(String name);

    @Query(value = "select *" +
            "from play " +
            "order by id desc",
            nativeQuery = true)
    List<Play> getByLastAdded();

    @Query(value = "select *" +
                   "from play " +
                   "inner join theatre " +
                   "on play.theatre_id = theatre.id " +
                   "where " +
                   "(:play is null or play.id=:play) and " +
                   "(:town is null or theatre.town_id=:town) and " +
                   "(:genre is null or play.play_genre_id=:genre) and " +
                   "(:theatre is null or theatre.id=:theatre)",
            nativeQuery = true)
    List<Play> findByPlayOrTownOrPlayGenreOrTheatre(Play play, Town town, PlayGenre genre, Theatre theatre);

    @Query(value = "select play.* " +
                   "from play " +
                   "inner join play_rating " +
                   "on play.id = play_rating.play_id " +
                   "where play_rating.user_id=:user " +
                   "order by play_rating.play_id DESC",
            nativeQuery = true)
    List<Play> findUserRatedPlays(User user);

    @Query(value = "select play.* " +
                   "from user_watched_plays as upl " +
                   "left join play_actors as pla " +
                   "on upl.watched_plays_id = pla.play_id "+
                   "right join play " +
                   "on play.id = upl.watched_plays_id " +
                   "where upl.user_id=:user AND pla.actor_id=:actor",
            nativeQuery = true)
    List<Play> getActorWatchedPlays(User user, Actor actor);

    @Query(value = "select play.* " +
                   "from play_actors as pla " +
                   "left join user_watched_plays as upl " +
                   "on pla.play_id != upl.watched_plays_id "+
                   "left join play " +
                   "on play.id != upl.watched_plays_id AND play.id = pla.play_id " +
                   "where upl.user_id=:user AND pla.actor_id=:actor",
            nativeQuery = true)
    List<Play> getActorUnwatchedPlays(User user, Actor actor);

    @Query(value = "select play.*" +
                   "from user_watched_plays as upl " +
                   "right join play " +
                   "on play.id = upl.watched_plays_id " +
                   "inner join theatre " +
                   "on play.theatre_id = theatre.id "+
                   "where upl.user_id=:user AND theatre_id=:theatre",
            nativeQuery = true)
    List<Play> getTheatreWatchedPlays(User user, Theatre theatre);

    @Query(value = "select play.*" +
                   "from user_watched_plays as upl " +
                   "right join play " +
                   "on play.id != upl.watched_plays_id " +
                   "inner join theatre " +
                   "on play.theatre_id = theatre.id "+
                   "where upl.user_id=:user AND theatre_id=:theatre",
            nativeQuery = true)
    List<Play> getTheatreUnwatchedPlays(User user, Theatre theatre);

    @Query(value = "select play.*" +
                   "from play " +
                   "where play.theatre_id =:theatre",
            nativeQuery = true)
    List<Play> getTheatrePlays(Theatre theatre);

    @Query(value = "select play.*" +
                   "from play " +
                   "inner join play_actors " +
                   "on play.id = play_actors.play_id " +
                   "where play_actors.actor_id =:actor",
            nativeQuery = true)
    List<Play> getActorPlays(Actor actor);

    @Query(value = "select distinct play.* " +
                   "from play_seat " +
                   "inner join play " +
                   "on play_id = play.id " +
                   "inner join theatre " +
                   "on theatre_id = theatre.id " +
                   "inner join town " +
                   "on theatre.town_id = town.id " +
                   "where "+
                   "is_active = 1 and " +
                   "DATE(date) = curdate() and " +
                   "town.name = 'София'",
            nativeQuery = true)
    List<Play> getTodaySchedulePlaysInSofia();

    @Query(value = "select distinct play.* " +
                   "from play " +
                   "inner join play_seat " +
                   "on play.id = play_id " +
                   "inner join theatre " +
                   "on play.theatre_id = theatre.id " +
                   "where " +
                   "is_active = 1 and " +
                   "DATE(date) = curdate() and " +
                   "(:town is null or theatre.town_id=:town) and " +
                   "(:theatre is null or theatre.id=:theatre)",
            nativeQuery = true)
    List<Play> getTodaySchedulePlays(Town town, Theatre theatre);

    @Query(value = "select distinct play.* " +
                   "from play " +
                   "inner join play_seat " +
                   "on play.id = play_id " +
                   "inner join theatre " +
                   "on play.theatre_id = theatre.id " +
                   "where " +
                   "is_active = 1 and " +
                   "DATE(date)=:date and " +
                   "date >= curdate() and " +
                   "(:town is null or theatre.town_id=:town) and " +
                   "(:theatre is null or theatre.id=:theatre)",
            nativeQuery = true)
    List<Play> getDateSchedulePlays(Date date, Town town, Theatre theatre);

   @Query(value = "select distinct play.* " +
                  "from play " +
                  "inner join play_seat " +
                  "on play.id = play_id " +
                  "inner join theatre " +
                  "on play.theatre_id = theatre.id " +
                  "where " +
                  "is_active = 1 and " +
                  "MONTH(date)=:month and " +
                  "date >= curdate() and " +
                  "(:town is null or theatre.town_id=:town) and " +
                  "(:theatre is null or theatre.id=:theatre)",
            nativeQuery = true)
    List<Play> getMonthSchedulePlays(String month, Town town, Theatre theatre);

    @Query(value = "select distinct play.* " +
            "from play " +
            "inner join play_seat " +
            "on play.id = play_id " +
            "inner join theatre " +
            "on play.theatre_id = theatre.id " +
            "where " +
            "is_active = 0 and " +
            "date = curdate() and " +
            "(:theatre is null or theatre.id=:theatre) ",
            nativeQuery = true)
    List<Play> getTheatreTodaySchedulePlays(Theatre theatre);

    @Query(value = "select distinct play.* " +
            "from play " +
            "inner join play_seat " +
            "on play.id = play_id " +
            "inner join theatre " +
            "on play.theatre_id = theatre.id " +
            "where " +
            "is_active = 0 and " +
            "(date=:date) and " +
            "date >= curdate() and " +
            "(:theatre is null or theatre.id=:theatre)",
            nativeQuery = true)
    List<Play> getTheatreDateSchedulePlays(Date date, Theatre theatre);

    @Query(value = "select distinct play.* " +
            "from play " +
            "inner join play_seat " +
            "on play.id = play_id " +
            "inner join theatre " +
            "on play.theatre_id = theatre.id " +
            "where " +
            "is_active = 0 and " +
            "MONTH(date)=:month and " +
            "date >= curdate() and " +
            "(:theatre is null or theatre.id=:theatre)",
            nativeQuery = true)
    List<Play> getTheatreMonthSchedulePlays(String month, Theatre theatre);

    List<Play> findAllByTheatreHall(TheatreHall theatreHall);

    List<Play> findAllByTheatre(Theatre theatre);

    Play findByNameAndTheatre(String name,Theatre Theatre);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from play_actors " +
            "where actor_id=:actor ",
            nativeQuery = true)
    void removeActor(Actor actor);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from play " +
            "where id=:playId ",
            nativeQuery = true)
    void removePlayById(long playId);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from play_gallery " +
            "where play_id=:play ",
            nativeQuery = true)
    void removePlayGallery(Play play);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from play_actors " +
            "where play_id=:play ",
            nativeQuery = true)
    void removePlayActors(Play play);

    @Modifying
    @Transactional
    void removeAllByTheatreHall(TheatreHall theatreHall);

    @Query(value = "SELECT play_genre_id, 100.0 * COUNT(*) / SUM(COUNT(*)) OVER () " +
                   "from play inner join user_watched_plays " +
                   "on play.id = watched_plays_id " +
                   "where user_id=:user " +
                   "GROUP BY play_genre_id ",
            nativeQuery = true)
    List<String> getUserWatchedGenresStatistics(User user);

    @Query(value = "SELECT theatre_id, 100.0 * COUNT(*) / SUM(COUNT(*)) OVER () " +
            "from play inner join user_watched_plays " +
            "on play.id = watched_plays_id " +
            "where user_id=:user " +
            "GROUP BY theatre_id ",
            nativeQuery = true)
    List<String> getUserWatchedTheatresStatistics(User user);
}