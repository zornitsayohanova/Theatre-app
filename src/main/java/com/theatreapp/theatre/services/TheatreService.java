package com.theatreapp.theatre.services;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.theatreapp.theatre.data.dao.*;
import com.theatreapp.theatre.data.entities.*;
import com.theatreapp.theatre.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class TheatreService extends DTO {
    private final TheatreDAO theatreDAO;
    private final PlayDAO playDAO;
    private final TheatreGenreDAO theatreGenreDAO;
    private final TheatreTypeDAO theatreTypeDAO;
    private final TheatreRatingDAO theatreRatingDAO;
    private final PictureDAO pictureDAO;
    private final UserDAO userDAO;
    private final TheatreHallDAO theatreHallDAO;
    private final NewsDAO newsDAO;

    public Theatre findById(long id) {
        return theatreDAO.findById(id).get();
    }

    public TheatreDTO findByIdDTO(long id) {
        return convertToTheatreDTO(theatreDAO.findById(id).get());
    }

    public List<TheatreDTO> findTheatreByName(String keyword) {
        return theatreDAO.findByNameLike(keyword)
                .stream()
                .map(this::convertToTheatreDTO)
                .collect(Collectors.toList());
    }

    public List<TheatreDTO> findSearchedTheatres(TheatreDTO theatreDTO) {
        Theatre theatre = checkIfTheatreIsNull(theatreDTO);

        if (theatre == null && theatreDTO.getGenre() == null && theatreDTO.getType() == null && theatreDTO.getTown() == null)
            return new ArrayList<>();
        else return theatreDAO.findByGenreOrTypeOrTown(theatre, theatreDTO.getGenre(), theatreDTO.getType(), theatreDTO.getTown())
                .stream()
                .map(this::convertToTheatreDTO)
                .collect(Collectors.toList());
    }

    public Theatre checkIfTheatreIsNull(TheatreDTO theatreDTO) {
        if (theatreDTO.getId() != 0) {
            return theatreDAO.findById(theatreDTO.getId()).get();
        }
        return null;
    }

    public List<TheatreDTO> getAllTheatres() {
        return theatreDAO.findAll()
                .stream()
                .map(this::convertToTheatreDTO)
                .collect(Collectors.toList());
    }

    public List<TheatreGenreDTO> getAllTheatreGenres() {
        return theatreGenreDAO.findAll()
                .stream()
                .map(this::convertToTheatreGenreDTO)
                .collect(Collectors.toList());
    }

    public List<TheatreTypeDTO> getAllTheatreTypes() {
        return theatreTypeDAO.findAll()
                .stream()
                .map(this::convertToTheatreTypeDTO)
                .collect(Collectors.toList());
    }

    public float calculateRating(TheatreDTO theatreDTO) {
        List<TheatreRating> usersTheatreRating = theatreRatingDAO.findByTheatre(convertToTheatre(theatreDTO));
        float sumRating = (float) usersTheatreRating.stream().mapToDouble(TheatreRating::getRating).sum();

        return sumRating / usersTheatreRating.size();
    }

    public float getTheatreRating(long id) {
        TheatreDTO theatreDTO = this.findByIdDTO(id);
        float finalResult = this.calculateRating(theatreDTO);

        return Float.isNaN(finalResult) ? 0 : finalResult;
    }

    public List<Float> getTheatresRating(List<TheatreDTO> theatres) {
        List<Float> ratings = new ArrayList<>();

        for (TheatreDTO theatre : theatres) {
            float finalResult = this.calculateRating(theatre);
            ratings.add(Float.isNaN(finalResult) ? 0 : finalResult);
        }
        return ratings;
    }

    public List<PictureDTO> getTheatreGallery(long id) {
        Theatre theatre = this.findById(id);

        return theatre.getGallery()
                .stream()
                .map(this::convertToPictureDTO)
                .collect(Collectors.toList());
    }

    public List<PlayDTO> getTheatrePlays(long id)  {
        Theatre theatre = this.findById(id);

        return playDAO.getTheatrePlays(theatre)
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public String[] getWorkHoursData(long id) {
        Theatre theatre = this.findById(id);

        return theatre.getWorkHours().split(", ");
    }

    public String[] getContactsData(long id) {
        Theatre theatre = this.findById(id);

        return theatre.getContacts().split(", ");
    }

    public void addOrEditTheatre(TheatreDTO theatreDTO) throws Exception {
        Theatre theatre = this.convertToTheatre(theatreDTO);

        this.checkValidations(theatre);

        if (theatre.getId() != 0 && theatreDAO.findByName(theatre.getName()) != null) {
            this.editTheatre(theatre, theatreDTO);
        } else {
            this.addTheatre(theatre);
        }
        theatre.setAvatar(theatreDTO.getGallery().get(0));

        theatreDAO.save(theatre);
    }

    void addTheatre(Theatre theatre) throws Exception {
        this.checkForUsedPictures(theatre);
    }

    void editTheatre(Theatre theatre, TheatreDTO theatreDTO) throws Exception {
        if(!(theatreDAO.findById(theatreDTO.getId()).get().getGallery()).containsAll(theatreDTO.getGallery())) {
            this.checkForUsedPictures(theatre);
        }
        theatre.getGallery().clear();
        theatre.setGallery(theatreDTO.getGallery());
    }

    void checkValidations(Theatre theatre) throws Exception {
        if (theatre.getId() == 0 && theatreDAO.findByName(theatre.getName()) != null) {
            throw new Exception("Театърът вече съществува!");
        }

        if(theatre.getGallery().size() != 3) {
            throw new Exception("Моля, добавете точно 3 снимки!");
        }
    }

    void checkForUsedPictures(Theatre theatre) throws Exception {
        for (Picture picture : theatre.getGallery()) {
            if (pictureDAO.findIfTheatrePictureGalleryExists(picture) ||
                    pictureDAO.findIfActorPictureGalleryExists(picture) ||
                    pictureDAO.findIfPlayPictureGalleryExists(picture)) {
                throw new Exception("Моля, изберете снимки, които не са на актьор/пост./театър !");
            }
        }
    }

    public void deleteTheatre(TheatreDTO theatreDTO) {
        Theatre theatre = this.convertToTheatre(theatreDTO);

        List<Picture> gallery = List.copyOf(theatre.getGallery());
        theatre.getGallery().clear();
        theatre.getActors().clear();
        theatre.setAvatar(new Picture());
        theatreRatingDAO.removeByTheatre(theatre);
        theatreHallDAO.removeAllByTheatre(theatre);
        newsDAO.removeTheatreInNews(theatre);
        userDAO.removeTargetedTheatre(theatre);
        userDAO.removeFavouriteTheatre(theatre);
        userDAO.removeWatchedTheatre(theatre);
        theatreDAO.removeById(theatre.getId());

        gallery.forEach(pictureDAO::deleteAllByPicture);
    }
}
