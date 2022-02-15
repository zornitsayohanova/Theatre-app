package com.theatreapp.theatre.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.theatreapp.theatre.data.entities.*;
import com.theatreapp.theatre.dto.*;
import com.theatreapp.theatre.exceptions.InvalidDataException;
import com.theatreapp.theatre.exceptions.UserAlreadyExistsException;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

public interface UserService {
        void registerUser(User user) throws UserAlreadyExistsException, InvalidDataException;

        void checkIsValidUser(User user) throws InvalidDataException, UserAlreadyExistsException;

        void setAuthorities(User user,  Set<Role> authorities);

        void updateUser(User currentUser) throws UserAlreadyExistsException, InvalidDataException;

        void deleteProfile(String specialId);

        boolean checkIfUserExistByUsername(String username);

        boolean checkIfUserExistByEmail(String email);

        UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

        List<User> getAllUsers();

        User getUser();

        User findByUsername(String name);

        boolean checkIfUserWinnerExists();

        boolean isActorLiked(User user, long id);

        boolean isPlayLiked(User user, long id);

        boolean isTheatreLiked(User user, long id);

        void addToFavouriteActors(String id);

        void addToFavouritePlays(String id);

        void addToFavouriteTheatres(String id);

        void removeFromFavouriteActors(String id);

        void removeFromFavouritePlays(String id);

        void removeFromFavouriteTheatres(String id);

        void removeTargetedActor(String id);

        void removeTargetedPlay(String id);

        void removeTargetedTheatre(String id);

        void ratePlay(String id, Rating rating);

        void rateActor(String id, Rating rating);

        void rateTheatre(String id, Rating rating);

        void setTheatreFromWatchedToTarget(PlayDTO playDTO);

        void setActorFromWatchedToTarget(PlayDTO playDTO);

        void setActorFromTargetToWatched(PlayDTO playDTO);

        void setTheatreFromTargetToWatched(PlayDTO playDTO);

        void targetPlay(PlayDTO playDTO) throws Exception;

        void targetTheatre(TheatreDTO theatreDTO) throws Exception;

        void targetActor(ActorDTO actorDTO) throws Exception;

        void reserveSelectedSeats(List<PlaySeat> selectedPlaySeats, String time, User user, boolean isWon) throws ParseException;

        void deleteSeatFromReserved(String seatId, String userId) throws Exception;

        void possiblyAddPlayPoints(User user, Play play);

        void possiblyAddActorPoints(User user, Play play);

        void possiblyAddTheatrePoints(User user, Play play);

        User getUserWinner();

        List<UserReservedSeatDTO> getSpecificUserReservedSeats(User user);

        List<UserReservedSeatDTO> getSpecificUserWonSeats(User user);

        List<UserReservedSeatDTO> getWonSeats();

        List<UserReservedSeatDTO> getAllUserReservedSeats();

        List<UserReservedSeatDTO> getAllUserWonSeats();

        float getUserPlayRating(User user, long id);

        float getUserActorRating(User user, long id);

        float getUserTheatreRating(User user, long id);

        List<PlayDTO> getActorWatchedPlays(long id);

        List<PlayDTO> getActorUnwatchedPlays(long id);

        List<PlayDTO> getTheatreWatchedPlays(long id);

        List<PlayDTO> getTheatreUnwatchedPlays(long id);

        List<ActorDTO> getFavouriteActors();

        List<PlayDTO> getFavoritePlays();

        List<TheatreDTO> getFavoriteTheatres();

        List<PlayDTO> getUserTargetedPlays();

        List<ActorDTO> getUserTargetedActors();

        List<TheatreDTO> getUserTargetedTheatres();

        List<ActorRatingDTO> getRatedActorsRating();

        List<PlayRatingDTO> getRatedPlaysRating();

        List<TheatreRatingDTO> getRatedTheatresRating();

        List<ActorDTO> getUserRatedActors();

        List<PlayDTO> getUserRatedPlays();

        List<TheatreDTO> getUserRatedTheatres();

        List<ActorDTO> getUserWatchedActors();

        List<PlayDTO> getUserWatchedPlays();

        List<TheatreDTO> getUserWatchedTheatres();

        String getUserWatchedGenresNames(User user);

        String getUserWatchedGenresPercentages(User user);

        String getUserWatchedTheatreNames(User user);

        String getUserWatchedTheatrePercentages(User user);
}
