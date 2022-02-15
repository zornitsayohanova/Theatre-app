package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.*;
import com.theatreapp.theatre.data.entities.Actor;
import com.theatreapp.theatre.data.entities.News;
import com.theatreapp.theatre.data.entities.Picture;
import com.theatreapp.theatre.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class NewsService extends DTO {
    private final NewsDAO newsDAO;
    private final ActorDAO actorDAO;
    private final PlayDAO playDAO;
    private final TheatreDAO theatreDAO;
    private final PictureDAO pictureDAO;

    public NewsDTO findByIdDTO(long id) {
        return this.convertToNewsDTO(newsDAO.findById(id).get());
    }

    public News findById(long id) {
        return newsDAO.findById(id).get();
    }

    public List<News> getAllNews() {
        return newsDAO.findAll();
    }

    public void addOrEditNews(NewsDTO newsDTO) throws Exception {
        this.checkForUsedPictures(newsDTO.getAvatar());

        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = new Date(date.getTime());

        News news = this.convertToNews(newsDTO);

        if(newsDAO.findById(news.getId()).isPresent()) {
            news.setDate(newsDAO.findById(news.getId()).get().getDate());
            newsDAO.save(news);

            return;
        }

        news.setDate(sqlDate);
        newsDAO.save(news);
    }

    void checkForUsedPictures(Picture picture) throws Exception {
        if (pictureDAO.findIfTheatrePictureGalleryExists(picture) ||
                pictureDAO.findIfActorPictureGalleryExists(picture) ||
                pictureDAO.findIfPlayPictureGalleryExists(picture)) {
            throw new Exception("Моля, изберете снимки, които не са на актьор/пост./театър !");
        }
    }

    public void deleteNews(NewsDTO newsDTO) throws Exception {
        News news = newsDAO.findById(newsDTO.getId()).get();

        news.getActors().clear();
        news.getPlays().clear();
        news.getTheatres().clear();

        newsDAO.delete(news);
    }

    public List<NewsDTO> getActorNews(ActorDTO actorDTO) {
        return newsDAO.getAllActorNews(actorDAO.findById(actorDTO.getId()).get())
                .stream()
                .map(this::convertToNewsDTO)
                .collect(Collectors.toList());
    }

    public List<NewsDTO> getTheatreNews(TheatreDTO theatreDTO) {
        return newsDAO.getAllTheatreNews(theatreDAO.findById(theatreDTO.getId()).get())
                .stream()
                .map(this::convertToNewsDTO)
                .collect(Collectors.toList());
    }

    public List<NewsDTO> getPlayNews(PlayDTO playDTO) {
        return newsDAO.getAllPlayNews(playDAO.findById(playDTO.getId()).get())
                .stream()
                .map(this::convertToNewsDTO)
                .collect(Collectors.toList());
    }
}
