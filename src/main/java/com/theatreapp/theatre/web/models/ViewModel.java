package com.theatreapp.theatre.web.models;

import com.theatreapp.theatre.data.entities.*;
import com.theatreapp.theatre.dto.*;
import org.modelmapper.ModelMapper;

public class ViewModel {
    private final ModelMapper modelMapper = new ModelMapper();

    protected ActorGenderViewModel convertToGenderViewModel(ActorGenderDTO actorGenderDTO) {
        return modelMapper.map(actorGenderDTO, ActorGenderViewModel.class);
    }

    protected TheatreViewModel convertToTheatreViewModel(TheatreDTO theatreDTO) {
        return modelMapper.map(theatreDTO, TheatreViewModel.class);
    }

    protected ActorViewModel convertToActorViewModel(ActorDTO actorDTO) {
        return modelMapper.map(actorDTO, ActorViewModel.class);
    }

    protected PlayViewModel convertToPlayViewModel(PlayDTO playDTO) {
        return modelMapper.map(playDTO, PlayViewModel.class);
    }

    protected PictureViewModel convertToPictureViewModel(PictureDTO pictureDTO) {
        return modelMapper.map(pictureDTO, PictureViewModel.class);
    }

    protected PlayCommentViewModel convertToCommentViewModel(PlayCommentDTO playCommentDTO) {
        return modelMapper.map(playCommentDTO, PlayCommentViewModel.class);
    }

    protected PlaySeatViewModel convertToPlaySeatViewModel(PlaySeatDTO playSeatDTO) {
        return modelMapper.map(playSeatDTO, PlaySeatViewModel.class);
    }

    protected PlayPriceViewModel convertToPlayPriceViewModel(PlayPriceDTO playPriceDTO) {
        return modelMapper.map(playPriceDTO, PlayPriceViewModel.class);
    }

    protected GameQuestionDTO convertToGameQuestionDTO(GameQuestionViewModel gameQuestionViewModel) {
        return modelMapper.map(gameQuestionViewModel, GameQuestionDTO.class);
    }

    protected GameAnswerDTO convertToGameAnswerDTO(NewGameAnswerViewModel newGameAnswerViewModel) {
        return modelMapper.map(newGameAnswerViewModel, GameAnswerDTO.class);
    }

    protected PlayGenreDTO convertToPlayGenreDTO(PlayGenreViewModel playGenreViewModel) {
        return modelMapper.map(playGenreViewModel, PlayGenreDTO.class);
    }

    protected PlayCommentDTO convertToPlayCommentDTO(PlayCommentViewModel playCommentViewModel) {
        return modelMapper.map(playCommentViewModel, PlayCommentDTO.class);
    }


    protected ActorDTO convertToActorDTO(ActorViewModel actorViewModel) {
        return modelMapper.map(actorViewModel, ActorDTO.class);
    }

    protected PlayDTO convertToPlayDTO(PlayViewModel playViewModel) {
        return modelMapper.map(playViewModel, PlayDTO.class);
    }

    protected TheatreDTO convertToTheatreDTO(TheatreViewModel theatreViewModel) {
        return modelMapper.map(theatreViewModel, TheatreDTO.class);
    }

    protected Theatre convertToTheatre(TheatreDTO theatreDTO) {
        return modelMapper.map(theatreDTO, Theatre.class);
    }

    protected ActorDTO convertToActorDTO(Actor actor) {
        return modelMapper.map(actor, ActorDTO.class);
    }

    protected PlayDTO convertToPlayDTO(Play play) {
        return modelMapper.map(play, PlayDTO.class);
    }

    protected PlayDTO convertToNewPlayDTO(NewPlayViewModel newPlayViewModel) {
        return modelMapper.map(newPlayViewModel, PlayDTO.class);
    }

    protected PlayDTO convertToPlayDTO(NewPlayViewModel newPlayViewModel) {
        return modelMapper.map(newPlayViewModel, PlayDTO.class);
    }

    protected TheatreHallDTO convertToNewTheatreHallDTO(NewTheatreHallViewModel theatreHallViewModel) {
        return modelMapper.map(theatreHallViewModel, TheatreHallDTO.class);
    }

    protected ActorDTO convertToNewActorDTO(NewActorViewModel actor) {
        return modelMapper.map(actor, ActorDTO.class);
    }

    protected TheatreDTO convertToNewTheatreDTO(NewTheatreViewModel theatre) {
        return modelMapper.map(theatre, TheatreDTO.class);
    }

    protected NewsDTO convertToNewsDTO(NewNewsViewModel newNewsViewModel) {
        return modelMapper.map(newNewsViewModel, NewsDTO.class);
    }

    protected NewsViewModel convertToNewsViewModel(NewsDTO newNewsDTO) {
        return modelMapper.map(newNewsDTO, NewsViewModel.class);
    }

    protected ActorGenderDTO convertToGenderDTO(ActorGender actorGender) {
        return modelMapper.map(actorGender, ActorGenderDTO.class);
    }

    protected PictureDTO convertToPictureDTO(Picture picture) {
        return modelMapper.map(picture, PictureDTO.class);
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

    protected PlayDateDTO convertToPlayDateDTO(PlayDateViewModel playDateViewModel) {
        return modelMapper.map(playDateViewModel, PlayDateDTO.class);
    }

    protected TheatreGenreDTO convertToTheatreGenreDTO(TheatreGenre theatreGenre) {
        return modelMapper.map(theatreGenre, TheatreGenreDTO.class);
    }

    protected TownViewModel convertToTownViewModel(TownDTO townDTO) {
        return modelMapper.map(townDTO, TownViewModel.class);
    }

    protected PlayGenreViewModel convertToGenreViewModel(PlayGenreDTO playGenreDTO) {
        return modelMapper.map(playGenreDTO, PlayGenreViewModel.class);
    }

    protected NewPlayViewModel convertToNewPlayViewModel(PlayDTO playDTO) {
        return modelMapper.map(playDTO, NewPlayViewModel.class);
    }

    protected NewActorViewModel convertToNewActorViewModel(ActorDTO actorDTO) {
        return modelMapper.map(actorDTO, NewActorViewModel.class);
    }

    protected PictureDTO convertToPictureDTO(NewPictureViewModel newPictureViewModel) {
        return modelMapper.map(newPictureViewModel, PictureDTO.class);
    }

    protected PictureDTO convertToNewPictureDTO(NewPictureViewModel newPictureViewModel) {
        return modelMapper.map(newPictureViewModel, PictureDTO.class);
    }

    protected NewTheatreViewModel convertToNewTheatreViewModel(TheatreDTO theatreDTO) {
        return modelMapper.map(theatreDTO, NewTheatreViewModel.class);
    }

    protected NewTheatreHallViewModel convertToNewTheatreHallViewModel(TheatreHallDTO theatreHallDTO) {
        return modelMapper.map(theatreHallDTO, NewTheatreHallViewModel.class);
    }

    protected TheatreGenreViewModel convertToGenreViewModel(TheatreGenreDTO genreDTO) {
        return modelMapper.map(genreDTO, TheatreGenreViewModel.class);
    }

    protected TheatreTypeViewModel convertToTheatreTypeViewModel(TheatreTypeDTO typeDTO) {
        return modelMapper.map(typeDTO, TheatreTypeViewModel.class);
    }
}
