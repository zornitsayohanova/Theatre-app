package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.*;
import com.theatreapp.theatre.data.entities.*;
import com.theatreapp.theatre.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PlayService extends DTO {
    private final PlayDAO playDAO;
    private final PlayCommentDAO playCommentsDAO;
    private final PlayRatingDAO playRatingDAO;
    private final PlayPriceDAO playPriceDAO;
    private final PictureDAO pictureDAO;
    private final TheatreHallDAO theatreHallDAO;
    private final PlaySeatDAO playSeatDAO;
    private final UserDAO userDAO;
    private final NewsDAO newsDAO;
    private final UserReservedSeatDAO userReservedSeatDAO;

    public Play findById(long id) {
        return playDAO.findById(id).get();
    }

    public List<PlayDTO> findAllByLastAdded() {
        return playDAO.getByLastAdded()
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public PlayDTO findByIdDTO(long id) {
        return convertToPlayDTO(playDAO.findById(id).get());
    }

    public List<PlayDTO> findPlayByName(String keyword) {
        return playDAO.findByNameLike(keyword)
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public List<PlayDTO> findSearchedPlays(PlayDTO playDTO) {
        Play play = checkIfPlayIsNull(playDTO);

        if (play == null && playDTO.getTown() == null && playDTO.getPlayGenre() == null && playDTO.getTheatre() == null)
            return new ArrayList<>();
        else return playDAO.findByPlayOrTownOrPlayGenreOrTheatre(play, playDTO.getTown(), playDTO.getPlayGenre(), playDTO.getTheatre())
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public Play checkIfPlayIsNull(PlayDTO playDTO) {
        if (playDTO.getId() != 0) {
            return playDAO.findById(playDTO.getId()).get();
        }
        return null;
    }

    public List<PlayDTO> getAllPlays() {
        return playDAO.findAll()
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    public float calculateRating(PlayDTO playDTO) {
        List<PlayRating> usersPlayRating = playRatingDAO.findByPlay(convertToPlay(playDTO));
        float sumRating = (float) usersPlayRating.stream().mapToDouble(PlayRating::getRating).sum();

        return sumRating / usersPlayRating.size();
    }

    public float getPlayRating(long id) {
        PlayDTO playDTO = this.findByIdDTO(id);
        float finalResult = this.calculateRating(playDTO);

        return Float.isNaN(finalResult) ? 0 : finalResult;
    }

    public List<Float> getPlaysRating(List<PlayDTO> plays) {
        List<Float> ratings = new ArrayList<>();

        for (PlayDTO play : plays) {
            float finalResult = this.calculateRating(play);
            ratings.add(Float.isNaN(finalResult) ? 0 : finalResult);
        }
        return ratings;
    }

    public List<PictureDTO> getPlayGallery(long id) {
        Play play = this.findById(id);

        return play.getGallery()
                .stream()
                .map(this::convertToPictureDTO)
                .collect(Collectors.toList());
    }

    public String[] getPlayCreativeTeamData(long id) {
        Play play = this.findById(id);

        return play.getCreative().split(", ");
    }

    public String[] getPlayTicketOfficeData(long id) {
        Play play = this.findById(id);

        return play.getTheatre().getContacts().split(", ");
    }

    public List<PlayCommentDTO> getPlayComments(long id) {
        Play play = this.findById(id);

        return playCommentsDAO.findAllByPlay(play)
                .stream()
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList());
    }

    public void addComment(String id, PlayCommentDTO playCommentDTO, User user) {
        Play play = this.findById(Long.parseLong(id));
        PlayComment playComment = this.convertToPlayComment(playCommentDTO);
        playComment.setPlay(play);
        playComment.setUser(user);

        playCommentsDAO.save(playComment);
    }
    public void editComment(PlayCommentDTO playCommentDTO) {
        PlayComment playComment = playCommentsDAO.findById(playCommentDTO.getId()).get();
        playComment.setPositivePart(playCommentDTO.getPositivePart());
        playComment.setNegativePart(playCommentDTO.getNegativePart());

        playCommentsDAO.save(playComment);
    }

    public void deleteComment(String id) {
        PlayComment playComment = playCommentsDAO.findById(Long.parseLong(id)).get();

        playCommentsDAO.deleteById(playComment.getId());
    }

    public void addOrEditPlay(PlayDTO playDTO) throws Exception {
        Play play = this.convertToPlay(playDTO);

        this.checkValidations(play);

        if(play.getId() != 0 && playDAO.findByNameAndTheatre(play.getName(), play.getTheatre()) != null) {
            this.editPlay(play, playDTO);
        } else {
            this.addPlay(play, playDTO);
        }
        play.setAvatar(playDTO.getGallery().get(0));

        playDAO.save(play);
    }

    void checkValidations(Play play) throws Exception {
        if(play.getTheatre() != play.getTheatreHall().getTheatre()) {
            throw new Exception("Залата не се намира в избрания театър!");
        }
        if(play.getId() == 0 && playDAO.findByNameAndTheatre(play.getName(), play.getTheatre()) != null) {
            throw new Exception("Постановката вече съществува!");
        }
        if(play.getGallery().size() != 4) {
            throw new Exception("Моля, добавете точно 4 снимки!");
        }
    }

    void addPlay(Play play, PlayDTO playDTO) throws Exception {
        this.checkForUsedPictures(play);
        this.setPlayPrices(play, playDTO);
    }

    void checkForUsedPictures(Play play) throws Exception {
        for (Picture picture : play.getGallery()) {
            if (pictureDAO.findIfTheatrePictureGalleryExists(picture) ||
                    pictureDAO.findIfActorPictureGalleryExists(picture) ||
                    pictureDAO.findIfPlayPictureGalleryExists(picture)) {
                throw new Exception("Моля, изберете снимки, които не са на актьор/пост./театър !");
            }
        }
    }

    void editPlay(Play play, PlayDTO playDTO) throws Exception {
        if(!(playDAO.findById(playDTO.getId()).get().getGallery()).containsAll(playDTO.getGallery())) {
            this.checkForUsedPictures(play);
        }
        play.getGallery().clear();
        play.setGallery(playDTO.getGallery());
        this.updatePlayPrices(play, playDTO, playPriceDAO.getAllByPlayAndTheatre(play, play.getTheatre()));
    }

    public void updatePlayPrices(Play play, PlayDTO playDTO, List<PlayPrice> playPrices) throws Exception {
        int counter = 0;
        try {
            String[] priceCategories = playDTO.getPlayPrices().split("\\r?\\n");
            String[] lastCategory = priceCategories[priceCategories.length - 1].split(" ");
            String lastRow = lastCategory[lastCategory.length - 2];
            if (Integer.parseInt(lastRow) != play.getTheatreHall().getHallRows()) {
                throw new Exception("Моля, въведете категории за всички редове!");
            }
            for (String priceCategory : priceCategories) {
                String[] currentCategory = priceCategory.split(" ");
                PlayPrice playPrice = playPriceDAO.findById(playPrices.get(counter).getId()).get();
                playPrice.setFromRow(currentCategory[0]);
                playPrice.setToRow(currentCategory[1]);
                playPrice.setPrice(Float.valueOf(currentCategory[2]));
                playPrice.setPlay(play);
                playPrice.setTheatre(play.getTheatre());

               // playPriceDAO.save(playPrice);

                counter++;
            }
        } catch (Exception e) {
            throw new Exception("Моля, въведете категории за всички редове!");
        }
    }

    public void setPlayPrices(Play play, PlayDTO playDTO) throws Exception {
        try {
            String[] priceCategories = playDTO.getPlayPrices().split("\\r?\\n");
            String[] lastCategory = priceCategories[priceCategories.length - 1].split(" ");
            String lastRow = lastCategory[lastCategory.length - 2];
            if(Integer.parseInt(lastRow) != play.getTheatreHall().getHallRows()) {
                throw new Exception("Моля, въведете категории за всички редове!");
            }
            for (String priceCategory : priceCategories) {
                String[] currentCategory = priceCategory.split(" ");
                PlayPrice playPrice = new PlayPrice();
                playPrice.setFromRow(currentCategory[0]);
                playPrice.setToRow(currentCategory[1]);
                playPrice.setPrice(Float.valueOf(currentCategory[2]));
                playPrice.setPlay(play);
                playPrice.setTheatre(play.getTheatre());

                playPriceDAO.save(playPrice);
            }
        } catch (Exception e) {
            throw new Exception("Моля, въведете категории за всички редове!");
        }
    }

    public StringBuilder getPlayPrices(PlayDTO playDTO) {
        StringBuilder prices = new StringBuilder();
        Play play = this.convertToPlay(playDTO);
        List<PlayPrice> playPriceCategories = playPriceDAO.findAllByPlayAndTheatre(play, play.getTheatre());
        for (PlayPrice categoryPrice : playPriceCategories) {
            prices.append(categoryPrice.getFromRow()).append(" ");
            prices.append(categoryPrice.getToRow()).append(" ");
            prices.append(categoryPrice.getPrice()).append(" ");
            prices.append("\n");
        }

        return prices;
    }

    public void addPlayDate(PlayDateDTO playDateDTO) throws Exception {
        if(playDateDTO.getPlay().getTheatre()!= playDateDTO.getTheatre()) {
            throw new Exception("Постановката не се намира в избрания театър!");
        }

    /*    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = playDateDTO.getDate().format(formatter);
        String playDateTime = formattedDate + playDateDTO.getTime() + ":00";

        Timestamp playDateTimeStamp = Timestamp.valueOf(playDateTime);

        if(playSeatDAO.findByDateAndPlay(playDateTimeStamp, playDateDTO.getPlay()) != null) {
            throw new Exception("Постановката ");
        }*/

        Play play = playDateDTO.getPlay();

        DateTimeFormatter sdformat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        java.sql.Date date = java.sql.Date.valueOf(playDateDTO.getDate());
        java.util.Date d = new java.util.Date();
        java.sql.Date today = new java.sql.Date(d.getTime());

        Calendar playDate = Calendar.getInstance();
        playDate.setTime(date);
        playDate.set(Calendar.HOUR_OF_DAY, 0);
        playDate.set(Calendar.MINUTE, 0);
        playDate.set(Calendar.SECOND, 0);
        playDate.set(Calendar.MILLISECOND, 0);

        Calendar todayDate = Calendar.getInstance();
        todayDate.setTime(today);
        todayDate.set(Calendar.HOUR_OF_DAY, 0);
        todayDate.set(Calendar.MINUTE, 0);
        todayDate.set(Calendar.SECOND, 0);
        todayDate.set(Calendar.MILLISECOND, 0);

        String formattedDate = playDateDTO.getDate().format(sdformat);
        String playDateTime = formattedDate + " " + playDateDTO.getTime() + ":00";
        Timestamp playDateTimestamp = Timestamp.valueOf(playDateTime);

        if(playDate.before(todayDate)) {
            throw new Exception("Моля, изберете дата по-голяма или равна на днешната!");
        }
        if(playSeatDAO.findByPlayAndTheatreAndDate(play, play.getTheatre(), playDateTimestamp) != null) {
            throw new Exception("Датата и часът за тази постановка вече съществуват!");
        }

 /*       String dateString = date + " " + playDateDTO.getTime() + ":00";
        Timestamp timestamp = Timestamp.valueOf(dateString);*/

        Theatre theatre = playDateDTO.getTheatre();
        String seats = theatreHallDAO.findAllByHallNameAndTheatre(play.getTheatreHall().getHallName(), theatre).getEnabledSeats();
        String[] enabledSeats = seats.split(", ");
        for (String seat : enabledSeats) {
            PlaySeat playSeat = new PlaySeat();
            playSeat.setDate(playDateTimestamp);
            playSeat.setActive(true);
            playSeat.setFree(true);
            playSeat.setSeatIdentifier(seat);
            playSeat.setDay(playDateDTO.getDay());
            playSeat.setPlay(play);

            this.setPlaySeatPrice(playSeat, play, Integer.parseInt(seat));

            playSeatDAO.save(playSeat);
        }
    }

    public void setPlaySeatPrice(PlaySeat playSeat, Play play, int seat) {
        List<PlayPrice> allSeatPrices = playPriceDAO.findAllByPlayAndTheatre(play, play.getTheatre());

        for (PlayPrice priceCategory : allSeatPrices) {
            int fromRow = Integer.parseInt(priceCategory.getFromRow());
            int toRow = Integer.parseInt(priceCategory.getToRow());
            int cols = play.getTheatreHall().getHallColumns();
            if(seat >= (fromRow * cols) - cols && seat <= toRow * cols) {
                playSeat.setPrice(priceCategory);
                break;
            }
        }
    }

    public void deleteAllPlaysOfTheatre(TheatreDTO theatreDTO) {
        Theatre theatre = this.convertToTheatre(theatreDTO);
        playDAO.findAllByTheatre(theatre).forEach(p -> this.deletePlay(this.convertToPlayDTO(p)));
    }

    public void deleteAllPlaysOfTheatreHall(TheatreHallDTO theatreHallDTO) {
        TheatreHall theatreHall = theatreHallDAO.findById(theatreHallDTO.getId()).get();
        playDAO.findAllByTheatreHall(theatreHall).forEach(p -> this.deletePlay(this.convertToPlayDTO(p)));
    }

    public void deletePlay(PlayDTO playDTO) {
        Play play = this.convertToPlay(playDTO);

        List<Picture> gallery = List.copyOf(play.getGallery());
        playDAO.removePlayGallery(play);
        playDAO.removePlayActors(play);
        play.setAvatar(new Picture());
        playCommentsDAO.removeAllByPlay(play);
        playPriceDAO.removeAllByPlay(play);
        playSeatDAO.removeAllByPlay(play);
        playRatingDAO.removeAllByPlay(play);
        newsDAO.removePlayInNews(play);
        userDAO.removeTargetedPlay(play);
        userDAO.removeFavouritePlay(play);
        userDAO.removeWatchedPlay(play);
        playDAO.removePlayById(play.getId());

        gallery.forEach(pictureDAO::deleteAllByPicture);
    }

    public List<Timestamp> getPlayDates(PlayDTO playDTO) {
        Play play = this.convertToPlay(playDTO);

        return playSeatDAO.getPlayDates(play);
    }

    public void deletePlayDate(PlayDTO playDTO, Date date) {
        Play play = this.convertToPlay(playDTO);
        Timestamp timestamp = new Timestamp(date.getTime());
        List<PlaySeat> seats = playSeatDAO.findAllByDateAndPlay(timestamp, play);

        seats.forEach(userReservedSeatDAO::deleteAllByPlaySeat);
        playSeatDAO.removeAllByPlayAndDate(play, timestamp);
    }
}