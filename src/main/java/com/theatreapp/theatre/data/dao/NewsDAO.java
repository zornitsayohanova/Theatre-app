package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.Actor;
import com.theatreapp.theatre.data.entities.News;
import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewsDAO extends JpaRepository<News, Long> {

    @Query(value = "select * " +
            "from news_actors " +
            "inner join news " +
            "on news_id = news.id " +
            "where actors_id=:actor",
            nativeQuery = true)
    List<News> getAllActorNews(Actor actor);

    @Query(value = "select * " +
            "from news_theatres " +
            "inner join news " +
            "on news_id = news.id " +
            "where theatres_id=:theatre",
            nativeQuery = true)
    List<News> getAllTheatreNews(Theatre theatre);

    @Query(value = "select * " +
            "from news_plays " +
            "inner join news " +
            "on news_id = news.id " +
            "where plays_id=:play",
            nativeQuery = true)
    List<News> getAllPlayNews(Play play);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from news_plays " +
            "where plays_id=:play",
            nativeQuery = true)
    void removePlayInNews(Play play);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from news_actors " +
            "where actors_id=:actor",
            nativeQuery = true)
    void removeActorInNews(Actor actor);

    @Modifying
    @Transactional
    @Query(value = "delete " +
            "from news_theatres " +
            "where theatres_id=:theatre",
            nativeQuery = true)
    void removeTheatreInNews(Theatre theatre);
}
