package com.theatreapp.theatre.dto;

import com.theatreapp.theatre.data.entities.*;
import com.theatreapp.theatre.web.models.PlayDateViewModel;
import org.modelmapper.ModelMapper;

public class DTO {

    private final ModelMapper modelMapper = new ModelMapper();

    protected Actor convertToActor(ActorDTO actorDTO) {
        return modelMapper.map(actorDTO, Actor.class);
    }

    protected Theatre convertToTheatre(TheatreDTO theatreDTO) {
        return modelMapper.map(theatreDTO, Theatre.class);
    }

    protected Play convertToPlay(PlayDTO playDTO) {
        return modelMapper.map(playDTO, Play.class);
    }

    protected Picture convertToPicture(PictureDTO pictureDTO) {
        return modelMapper.map(pictureDTO, Picture.class);
    }

    protected News convertToNews(NewsDTO newsDTO) {
        return modelMapper.map(newsDTO, News.class);
    }
    protected NewsDTO convertToNewsDTO(News news) {
        return modelMapper.map(news, NewsDTO.class);
    }


    protected GameQuestionDTO convertToGameQuestionDTO(GameQuestion gameQuestion) {
        return modelMapper.map(gameQuestion, GameQuestionDTO.class);
    }

    public GameAnswerDTO convertToGameAnswerDTO(GameQuestion gameQuestion) {
        return modelMapper.map(gameQuestion, GameAnswerDTO.class);
    }

    protected PlayCommentDTO convertToCommentDTO(PlayComment playComment) {
        return modelMapper.map(playComment, PlayCommentDTO.class);
    }

    protected TheatreHallDTO convertToTheatreHallDTO(TheatreHall theatreHall) {
        return modelMapper.map(theatreHall, TheatreHallDTO.class);
    }

    protected PlayComment convertToPlayComment(PlayCommentDTO playCommentDTO) {
        return modelMapper.map(playCommentDTO, PlayComment.class);
    }

    protected PlayDateDTO convertToPlayDateDTO(PlayDateViewModel playDateViewModel) {
        return modelMapper.map(playDateViewModel, PlayDateDTO.class);
    }

    protected TheatreHall convertToTheatreHall(TheatreHallDTO theatreHall) {
        return modelMapper.map(theatreHall, TheatreHall.class);
    }

    protected ActorDTO convertToActorDTO(Actor actor) {
        return modelMapper.map(actor, ActorDTO.class);
    }

    protected TheatreDTO convertToTheatreDTO(Theatre theatre) {
        return modelMapper.map(theatre, TheatreDTO.class);
    }

    protected PlayDTO convertToPlayDTO(Play play) {
        return modelMapper.map(play, PlayDTO.class);
    }

    protected TheatreRatingDTO convertToTheatreRatingDTO(TheatreRating theatreRating) {
        return modelMapper.map(theatreRating, TheatreRatingDTO.class);
    }

    protected PlayRatingDTO convertToPlayRatingDTO(PlayRating playRating) {
        return modelMapper.map(playRating, PlayRatingDTO.class);
    }

    protected ActorRatingDTO convertToActorRatingDTO(ActorRating actorRating) {
        return modelMapper.map(actorRating, ActorRatingDTO.class);
    }

    protected ActorGenderDTO convertToGenderDTO(ActorGender actorGender) {
        return modelMapper.map(actorGender, ActorGenderDTO.class);
    }

    protected PictureDTO convertToPictureDTO(Picture picture) {
        return modelMapper.map(picture, PictureDTO.class);
    }

    protected PlaySeatDTO convertToPlaySeatDTO(PlaySeat playSeat) {
        return modelMapper.map(playSeat, PlaySeatDTO.class);
    }

    protected UserReservedSeatDTO convertToUserReservedSeatDTO(UserReservedSeat userReservedSeat) {
        return modelMapper.map(userReservedSeat, UserReservedSeatDTO.class);
    }

    protected PlayPriceDTO convertToPlayPriceDTO(PlayPrice playPrice) {
        return modelMapper.map(playPrice, PlayPriceDTO.class);
    }

    protected PlayGenreDTO convertToGenreDTO(PlayGenre playGenre) {
        return modelMapper.map(playGenre, PlayGenreDTO.class);
    }

    protected TownDTO convertToTownDTO(Town town) {
        return modelMapper.map(town, TownDTO.class);
    }

    protected TheatreTypeDTO convertToTheatreTypeDTO(TheatreType theatreType) {
        return modelMapper.map(theatreType, TheatreTypeDTO.class);
    }

    protected TheatreGenreDTO convertToTheatreGenreDTO(TheatreGenre theatreGenre) {
        return modelMapper.map(theatreGenre, TheatreGenreDTO.class);
    }
}
