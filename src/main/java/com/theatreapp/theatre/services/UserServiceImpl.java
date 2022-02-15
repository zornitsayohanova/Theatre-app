package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.*;
import com.theatreapp.theatre.data.entities.*;
import com.theatreapp.theatre.dto.*;
import com.theatreapp.theatre.exceptions.InvalidDataException;
import com.theatreapp.theatre.exceptions.UserAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl extends DTO implements UserDetailsService, UserService {
    private final ActorDAO actorDAO;
    private final UserDAO userDAO;
    private final PlayDAO playDAO;
    private final PlayGenreDAO playGenreDAO;
    private final TheatreDAO theatreDAO;
    private final PlayRatingDAO playRatingDAO;
    private final ActorRatingDAO actorRatingDAO;
    private final TheatreRatingDAO theatreRatingDAO;
    private final UserReservedSeatDAO userReservedSeatDAO;
    private final GameParticipantDAO gameParticipantDAO;
    private final PlayCommentDAO playCommentDAO;
    private final RoleDAO roleDAO;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void registerUser(User user) throws UserAlreadyExistsException, InvalidDataException {
        if(this.checkIfUserExistByUsername(user.getUsername()) || this.checkIfUserExistByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException("Този потребител вече съществува!");
        }

        this.checkIsValidUser(user);

        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        user.setPassword(encryptedPassword);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonExpired(true);
        user.setEnabled(true);
        user.setIsGameEligible(false);
        user.setHasWonTicket(false);
        user.setTotalActivityScore(0);
        Set<Role> authorities = new HashSet<>();
        this.setAuthorities(user, authorities);

        userDAO.save(user);

    }

    @Override
    public void checkIsValidUser(User user) throws InvalidDataException, UserAlreadyExistsException {
        String regexUsername = "^[A-Za-z]\\w{3,29}$"; //can have lowercase or uppercase or underscore and is between 3 and 30 characters
        Pattern patternUsername = Pattern.compile(regexUsername);
        String regexEmail = "^(.+)@(.+)$"; //can have lowercase or uppercase, numbers and may contain only dot, dash and underscore
        Pattern patternEmail = Pattern.compile(regexEmail);

        if(!patternUsername.matcher(user.getUsername()).matches()) {
            throw new InvalidDataException("Потребителското име е невалидно!");
        }

        if(!patternEmail.matcher(user.getEmail()).matches()) {
            throw new InvalidDataException("Имейлът е невалиден!");
        }

        if(user.getPassword().length() < 6) {
            throw new InvalidDataException("Паролата трябва да е над 5 символа!");
        }
    }

    @Override
    public boolean checkIfUserExistByUsername(String username) {
        return userDAO.findByUsername(username) != null;
    }

    @Override
    public boolean checkIfUserExistByEmail(String email) {
        return userDAO.findByEmail(email) != null;
    }

    @Override
    public void setAuthorities(User user,  Set<Role> authorities) {
        if(user.getUsername().equals("admin1")) {
            Role authority = roleDAO.findByAuthority("ADMIN1");
            authorities.add(authority);
            user.setAuthorities(authorities);
        } else if(user.getUsername().equals("admin2")) {
            Role authority = roleDAO.findByAuthority("ADMIN2");
            authorities.add(authority);
            user.setAuthorities(authorities);
        } else {
            Role authority = roleDAO.findByAuthority("USER");
            authorities.add(authority);
            user.setAuthorities(authorities);
        }
    }

    @Override
    public void updateUser(User currentUser) throws UserAlreadyExistsException, InvalidDataException {
        this.checkIsValidUser(currentUser);

        User user = userDAO.findById(currentUser.getId()).get();

        user.setEmail(currentUser.getEmail());
        user.setUsername(currentUser.getUsername());
        String newPassword = currentUser.getPassword();
        String newEncryptedPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(newEncryptedPassword);

        userDAO.save(user);
    }

    @Override
    public void deleteProfile(String userId) {
        User user = userDAO.findById(Long.parseLong(userId)).get();

        actorRatingDAO.deleteAllByUser(user);
        playRatingDAO.deleteAllByUser(user);
        theatreRatingDAO.deleteAllByUser(user);
        playCommentDAO.deleteAllByUser(user);
        gameParticipantDAO.deleteAllByUser(user);
        userReservedSeatDAO.deleteAllByUser(user);
        user.getAuthorities().clear();
        user.getWatchedActors().clear();
        user.getWatchedPlays().clear();
        user.getWatchedTheatres().clear();
        user.getTargetedActors().clear();
        user.getTargetedPlays().clear();
        user.getTargetedTheatres().clear();
        user.getFavouriteActors().clear();
        user.getFavouritePlays().clear();
        user.getFavouriteTheatres().clear();

        userDAO.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userDAO.findByUsername(auth.getName());
    }

    @Override
    public User findByUsername(String name) {
        return userDAO.findByUsername(name);
    }

    @Override
    public boolean checkIfUserWinnerExists() {
        return userDAO.getUserWinner() != null;
    }

    @Override
    public boolean isActorLiked(User user, long id) {
        if (user == null)
            return false;
        Actor actor = actorDAO.findById(id).get();

        return user.getFavouriteActors().contains(actor);
    }

    @Override
    public boolean isPlayLiked(User user, long id) {
        if (user == null)
            return false;
        Play play = playDAO.findById(id).get();

        return user.getFavouritePlays().contains(play);
    }

    @Override
    public boolean isTheatreLiked(User user, long id) {
        if (user == null)
            return false;
        Theatre theatre = theatreDAO.findById(id).get();

        return user.getFavouriteTheatres().contains(theatre);
    }

    @Override
    public void addToFavouriteActors(String id) {
        User user = this.getUser();
        Actor actor = actorDAO.findById(Long.parseLong(id)).get();

        this.getUser().getFavouriteActors().add(actor);
        userDAO.save(user);
    }

    @Override
    public void addToFavouritePlays(String id) {
        User user = this.getUser();
        Play play = playDAO.findById(Long.parseLong(id)).get();

        user.getFavouritePlays().add(play);
        userDAO.save(user);
    }

    @Override
    public void addToFavouriteTheatres(String id) {
        User user = this.getUser();
        Theatre theatre = theatreDAO.findById(Long.parseLong(id)).get();

        user.getFavouriteTheatres().add(theatre);
        userDAO.save(user);
    }

    @Override
    public void removeFromFavouriteActors(String id) {
        User user = this.getUser();
        Actor actor = actorDAO.findById(Long.parseLong(id)).get();

        user.getFavouriteActors().remove(actor);
        userDAO.save(user);
    }

    @Override
    public void removeFromFavouritePlays(String id) {
        User user = this.getUser();
        Play play = playDAO.findById(Long.parseLong(id)).get();

        user.getFavouritePlays().remove(play);
        userDAO.save(user);
    }

    @Override
    public void removeFromFavouriteTheatres(String id) {
        User user = this.getUser();
        Theatre theatre = theatreDAO.findById(Long.parseLong(id)).get();

        user.getFavouriteTheatres().remove(theatre);
        userDAO.save(user);
    }

    @Override
    public void removeTargetedActor(String id) {
        Actor actor = actorDAO.findById(Long.parseLong(id)).get();
        User user = this.getUser();

        user.getTargetedActors().remove(actor);
        userDAO.save(user);
    }

    @Override
    public void removeTargetedPlay(String id) {
        Play play = playDAO.findById(Long.parseLong(id)).get();
        User user = this.getUser();

        user.getTargetedPlays().remove(play);
        userDAO.save(user);
    }

    @Override
    public void removeTargetedTheatre(String id) {
        Theatre theatre = theatreDAO.findById(Long.parseLong(id)).get();
        User user = this.getUser();

        user.getTargetedTheatres().remove(theatre);
        userDAO.save(user);
    }

    @Override
    public void ratePlay(String id, Rating rating) {
        Play play = playDAO.findById(Long.parseLong(id)).get();
        User user = this.getUser();

        if (playRatingDAO.existsPlayRatingByUserAndPlay(user, play)) {
            PlayRating playRating = playRatingDAO.findByUserAndPlay(user, play);
            playRating.setRating(rating.getStars());
            playRatingDAO.save(playRating);
        } else {
            PlayRating playRating = new PlayRating();
            playRating.setPlay(play);
            playRating.setRating(rating.getStars());
            playRating.setUser(user);
            playRatingDAO.save(playRating);
        }
        userDAO.save(user);
    }

    @Override
    public void rateActor(String id, Rating rating) {
        Actor actor = actorDAO.findById(Long.parseLong(id)).get();
        User user = this.getUser();

        if (actorRatingDAO.existsActorRatingByUserAndActor(user, actor)) {
            ActorRating actorRating = actorRatingDAO.findByUserAndActor(user, actor);
            actorRating.setRating(rating.getStars());
            actorRatingDAO.save(actorRating);
        } else {
            ActorRating actorRating = new ActorRating();
            actorRating.setActor(actor);
            actorRating.setRating(rating.getStars());
            actorRating.setUser(user);
            actorRatingDAO.save(actorRating);
        }
        userDAO.save(user);
    }

    @Override
    public void rateTheatre(String id, Rating rating) {
        Theatre theatre = theatreDAO.findById(Long.parseLong(id)).get();
        User user = this.getUser();

        if (theatreRatingDAO.existsTheatreRatingByUserAndTheatre(user, theatre)) {
            TheatreRating theatreRating = theatreRatingDAO.findByUserAndTheatre(user, theatre);
            theatreRating.setRating(rating.getStars());
            theatreRatingDAO.save(theatreRating);
        } else {
            TheatreRating theatreRating = new TheatreRating();
            theatreRating.setTheatre(theatre);
            theatreRating.setRating(rating.getStars());
            theatreRating.setUser(user);
            theatreRatingDAO.save(theatreRating);
        }
        userDAO.save(user);
    }

    @Override
    public void setTheatreFromWatchedToTarget(PlayDTO playDTO) {
        Theatre theatre = playDTO.getTheatre();

        List<User> users = userDAO.getUsersWatchedTheatre(theatre);
        users.forEach(u -> {
            u.getWatchedTheatres().remove(theatre);
            u.getTargetedTheatres().add(theatre);
            u.setTotalActivityScore(u.getTotalActivityScore() - 7);
        });

        userDAO.saveAll(users);
    }

    @Override
    public void setActorFromWatchedToTarget(PlayDTO playDTO) {
        List<Actor> actors = playDTO.getActors();

        for (Actor actor : actors) {
            List<User> users = userDAO.getUsersWatchedActor(actor);
            users.forEach(u -> {
                u.getWatchedActors().remove(actor);
                u.getTargetedActors().add(actor);
                u.setTotalActivityScore(u.getTotalActivityScore() - 5);
            });

            userDAO.saveAll(users);
        }
    }

    @Override
    public void setActorFromTargetToWatched(PlayDTO playDTO) {
        Play play = playDAO.findById(playDTO.getId()).get();
        List<Actor> actors = play.getActors();

        for (Actor actor : actors) {
            List<User> users = userDAO.getUsersTargetActor(actor);
            for(User u : users) {
                List<Play> unwatchedActorPlays = playDAO.getActorUnwatchedPlays(u, actor);
                if(unwatchedActorPlays.size() == 1 && unwatchedActorPlays.contains(play)) {
                    u.getTargetedActors().remove(actor);
                    u.getWatchedActors().add(actor);
                    u.setTotalActivityScore(u.getTotalActivityScore() + 5);
                }
            }

            userDAO.saveAll(users);
        }
    }

    @Override
    public void setTheatreFromTargetToWatched(PlayDTO playDTO) {
        Play play = playDAO.findById(playDTO.getId()).get();
        Theatre theatre = play.getTheatre();

        List<User> users = userDAO.getUsersTargetTheatre(theatre);
        for(User u : users) {
            List<Play> unwatchedTheatrePlays = playDAO.getTheatreUnwatchedPlays(u, theatre);
            if(unwatchedTheatrePlays.size() == 1 && unwatchedTheatrePlays.contains(play)) {
                u.getTargetedTheatres().remove(theatre);
                u.getWatchedTheatres().add(theatre);
                u.setTotalActivityScore(u.getTotalActivityScore() + 7);
            }
        }

        userDAO.saveAll(users);
    }

    @Override
    public void targetPlay(PlayDTO playDTO) throws Exception {
        User user = this.getUser();
        Play play = playDAO.findById(playDTO.getId()).get();

        if (user.getTargetedPlays().contains(play)) {
            throw new Exception("Тази постановка вече е набелязана! Върнете се назад.");
        } else if (user.getWatchedPlays().contains(play)) {
            throw new Exception("Тази постановка вече е изгледана! Върнете се назад.");
        }
        user.getTargetedPlays().add(play);
        userDAO.save(user);
    }

    @Override
    public void targetTheatre(TheatreDTO theatreDTO) throws Exception {
        User user = this.getUser();
        Theatre theatre = theatreDAO.findById(theatreDTO.getId()).get();

        if (user.getTargetedTheatres().contains(theatre)) {
            throw new Exception("Този театър вече е набелязан! Върнете се назад.");
        } else if (user.getWatchedTheatres().contains(theatre)) {
            throw new Exception("Този театър вече е изгледан! Върнете се назад.");
        }

        List<Play> userUnwatchedTheatrePlays = playDAO.getTheatreUnwatchedPlays(user, theatre);

        if(userUnwatchedTheatrePlays.size() == 0) {
            user.getWatchedTheatres().add(theatre);
            user.setTotalActivityScore(user.getTotalActivityScore() + 7);
        } else {
            user.getTargetedTheatres().add(theatre);
        }
        userDAO.save(user);
    }

    @Override
    public void targetActor(ActorDTO actorDTO) throws Exception {
        User user = this.getUser();
        Actor actor = actorDAO.findById(actorDTO.getId()).get();

        if (user.getTargetedActors().contains(actor)) {
            throw new Exception("Този актьор вече е набелязан! Върнете се назад.");
        } else if (user.getWatchedActors().contains(actor)) {
            throw new Exception("Този актьор вече е изгледан! Върнете се назад.");
        }

        List<Play> userUnwatchedActorPlays = playDAO.getActorUnwatchedPlays(user, actor);

        if(userUnwatchedActorPlays.size() == 0) {
            user.getWatchedActors().add(actor);
            user.setTotalActivityScore(user.getTotalActivityScore() + 5);
        } else {
            user.getTargetedActors().add(actor);
        }
        userDAO.save(user);
    }

    @Override
    public void reserveSelectedSeats(List<PlaySeat> selectedPlaySeats, String time, User user, boolean isWon) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date parsedDate = dateFormat.parse(time);
        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

        for (PlaySeat playSeat : selectedPlaySeats) {
            UserReservedSeat userReservedSeat = new UserReservedSeat();
            userReservedSeat.setPlaySeat(playSeat);
            userReservedSeat.setIsWon(isWon);
            userReservedSeat.setUser(user);
            userReservedSeat.setTimeOfReservation(timestamp);

            userReservedSeatDAO.save(userReservedSeat);
        }
        userDAO.save(user);
    }

    @Override
    public void deleteSeatFromReserved(String seatId, String userId) throws Exception {
        User user = userDAO.findById(Long.parseLong(userId)).get();
        UserReservedSeat userReservedSeat = userReservedSeatDAO.findById(Long.parseLong(seatId)).get();
        if(userReservedSeat.getUser() != user) {
            throw new Exception("Възникна проблем. Моля, свържете се с поддръжка!");
        }

        Play play = userReservedSeat.getPlaySeat().getPlay();
        this.possiblyAddPlayPoints(user, play);
        this.possiblyAddActorPoints(user, play);
        this.possiblyAddTheatrePoints(user, play);

        userReservedSeatDAO.deleteSeatFromReserved(userReservedSeat);
    }

    @Override
    public void possiblyAddPlayPoints(User user, Play play) {
        if(user.getTargetedPlays().contains(play)) {
            user.getTargetedPlays().remove(play);
        }

        if(!user.getWatchedPlays().contains(play)) {
            user.getWatchedPlays().add(play);
            user.setTotalActivityScore(user.getTotalActivityScore() + 3);
        }

        userDAO.save(user);
    }

    @Override
    public void possiblyAddActorPoints(User user, Play play) {
        List<Actor> userTargetedActors = user.getTargetedActors();

        for (Actor actor : play.getActors()) {
            if(userTargetedActors.contains(actor)) {
                List<Play> userUnwatchedActorPlays = playDAO.getActorUnwatchedPlays(user, actor);

               if(userUnwatchedActorPlays.size() == 0) {
                   if(!user.getWatchedActors().contains(actor)) {
                       user.getWatchedActors().add(actor);
                       user.setTotalActivityScore(user.getTotalActivityScore() + 5);
                   }
               }
            }
        }
    }

    @Override
    public void possiblyAddTheatrePoints(User user, Play play) {
        List<Theatre> userTargetedTheatres = user.getTargetedTheatres();
        Theatre theatre = play.getTheatre();
        if(userTargetedTheatres.contains(theatre)) {
            List<Play> userUnwatchedTheatrePlays = playDAO.getTheatreUnwatchedPlays(user, theatre);

            if(userUnwatchedTheatrePlays.size() == 0) {
                if(!user.getWatchedTheatres().contains(theatre)) {
                    user.getWatchedTheatres().add(theatre);
                    user.setTotalActivityScore(user.getTotalActivityScore() + 7);
                }
            }
        }
    }

    @Override
    public User getUserWinner() {
        return userDAO.getUserWinner();
    }

    @Override
    public List<UserReservedSeatDTO> getSpecificUserReservedSeats(User user) {
        return userReservedSeatDAO.getUserReservedSeatsByDate(user)
                .stream()
                .map(this::convertToUserReservedSeatDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserReservedSeatDTO> getSpecificUserWonSeats(User user) {
        return userReservedSeatDAO.getUserWonSeatsByDate(user)
                .stream()
                .map(this::convertToUserReservedSeatDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserReservedSeatDTO> getWonSeats() {
        return userReservedSeatDAO.getWonSeatsByDate()
                .stream()
                .map(this::convertToUserReservedSeatDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserReservedSeatDTO> getAllUserReservedSeats() {
        User user = this.getUser();

        return userReservedSeatDAO.getUserReservedSeatsByDate(user)
                .stream()
                .map(this::convertToUserReservedSeatDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserReservedSeatDTO> getAllUserWonSeats() {
        User user = this.getUser();

        return userReservedSeatDAO.getUserWonSeatsByDate(user)
                .stream()
                .map(this::convertToUserReservedSeatDTO)
                .collect(Collectors.toList());
    }

    @Override
    public float getUserPlayRating(User user, long id) {
        if (user == null)
            return 0;
        Play play = playDAO.findById(id).get();
        PlayRating playRating = playRatingDAO.findByUserAndPlay(getUser(), play);

        if (playRating != null) {
            return playRating.getRating();
        } else {
            return 0;
        }
    }

    @Override
    public float getUserActorRating(User user, long id) {
        if (user == null)
            return 0;
        Actor actor = actorDAO.findById(id).get();
        ActorRating actorRating = actorRatingDAO.findByUserAndActor(user, actor);

        if (actorRating != null) {
            return actorRating.getRating();
        } else {
            return 0;
        }
    }

    @Override
    public float getUserTheatreRating(User user, long id) {
        if (user == null)
            return 0;
        Theatre theatre = theatreDAO.findById(id).get();
        TheatreRating theatreRating = theatreRatingDAO.findByUserAndTheatre(user, theatre);

        if (theatreRating != null) {
            return theatreRating.getRating();
        } else {
            return 0;
        }
    }

    @Override
    public List<PlayDTO> getActorWatchedPlays(long id) {
        Actor actor = actorDAO.findById(id).get();

        return playDAO.getActorWatchedPlays(this.getUser(), actor)
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayDTO> getActorUnwatchedPlays(long id) {
        Actor actor = actorDAO.findById(id).get();

        return playDAO.getActorUnwatchedPlays(this.getUser(), actor)
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayDTO> getTheatreWatchedPlays(long id) {
        Theatre theatre = theatreDAO.findById(id).get();

        return playDAO.getTheatreWatchedPlays(this.getUser(), theatre)
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayDTO> getTheatreUnwatchedPlays(long id) {
        Theatre theatre = theatreDAO.findById(id).get();

        return playDAO.getTheatreUnwatchedPlays(this.getUser(), theatre)
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActorDTO> getFavouriteActors() {
        return this.getUser().getFavouriteActors()
                .stream()
                .map(this::convertToActorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayDTO> getFavoritePlays() {
        return this.getUser().getFavouritePlays()
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TheatreDTO> getFavoriteTheatres() {
        return this.getUser().getFavouriteTheatres()
                .stream()
                .map(this::convertToTheatreDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayDTO> getUserTargetedPlays() {
        return this.getUser().getTargetedPlays()
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActorDTO> getUserTargetedActors() {
        return this.getUser().getTargetedActors()
                .stream()
                .map(this::convertToActorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TheatreDTO> getUserTargetedTheatres() {
        return this.getUser().getTargetedTheatres()
                .stream()
                .map(this::convertToTheatreDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActorRatingDTO> getRatedActorsRating() {
        return actorRatingDAO.getUserRatedActorsRating(this.getUser())
                .stream()
                .map(this::convertToActorRatingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayRatingDTO> getRatedPlaysRating() {
        return playRatingDAO.getUserRatedPlaysRating(this.getUser())
                .stream()
                .map(this::convertToPlayRatingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TheatreRatingDTO> getRatedTheatresRating() {
        return theatreRatingDAO.getUserRatedTheatresRating(this.getUser())
                .stream()
                .map(this::convertToTheatreRatingDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActorDTO> getUserRatedActors() {
        return actorDAO.findUserRatedActors(this.getUser())
                .stream()
                .map(this::convertToActorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayDTO> getUserRatedPlays() {
        return playDAO.findUserRatedPlays(this.getUser())
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TheatreDTO> getUserRatedTheatres() {
        return theatreDAO.findUserRatedTheatres(this.getUser())
                .stream()
                .map(this::convertToTheatreDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActorDTO> getUserWatchedActors() {
        return this.getUser().getWatchedActors()
                .stream()
                .map(this::convertToActorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlayDTO> getUserWatchedPlays() {
        return this.getUser().getWatchedPlays()
                .stream()
                .map(this::convertToPlayDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TheatreDTO> getUserWatchedTheatres() {
        return this.getUser().getWatchedTheatres()
                .stream()
                .map(this::convertToTheatreDTO)
                .collect(Collectors.toList());
    }

    @Override
    public String getUserWatchedGenresNames(User user) {
        List<String> genreStatistics = playDAO.getUserWatchedGenresStatistics(user);

        StringBuilder genresNames = new StringBuilder();
        for (String data : genreStatistics) {
            String[] dataString = data.split(",");
            String genreName = playGenreDAO.findById(Long.parseLong(dataString[0])).get().getName();
            genresNames.append(genreName);
            genresNames.append(",");
        }
        return genresNames.toString();
    }

    @Override
    public String getUserWatchedGenresPercentages(User user) {
        List<String> genreStatistics = playDAO.getUserWatchedGenresStatistics(user);

        StringBuilder genresPercentages = new StringBuilder();
        for (String data : genreStatistics) {
            String[] dataString = data.split(",");
            String genrePercentage = dataString[1];
            genresPercentages.append(genrePercentage);
            genresPercentages.append(",");
        }
        return genresPercentages.toString();
    }

    @Override
    public String getUserWatchedTheatreNames(User user) {
        List<String> theatreStatistics = playDAO.getUserWatchedTheatresStatistics(user);

        StringBuilder theatresNames = new StringBuilder();
        for (String data : theatreStatistics) {
            String[] dataString = data.split(",");
            String theatreName = theatreDAO.findById(Long.parseLong(dataString[0])).get().getName();
            theatresNames.append(theatreName);
            theatresNames.append(",");
        }
        return theatresNames.toString();
    }

    @Override
    public String getUserWatchedTheatrePercentages(User user) {
        List<String> theatreStatistics = playDAO.getUserWatchedTheatresStatistics(user);

        StringBuilder theatresPercentages = new StringBuilder();
        for (String data : theatreStatistics) {
            String[] dataString = data.split(",");
            String theatrePercentage = dataString[1];
            theatresPercentages.append(theatrePercentage);
            theatresPercentages.append(",");
        }
        return theatresPercentages.toString();
    }
}
// @Override
//public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//    return null;
//}
    /*private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void registerUser(User user) throws UserAlreadyExistsException, InvalidDataException {
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);

        if(checkIfUserExist(user.getUsername())){
            throw new UserAlreadyExistsException("Този потребител вече съществува!");
        } else {
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setAccountNonExpired(true);
            user.setEnabled(true);

            Set<Role> authorities = new HashSet<Role>(){};

            if(user.getUsername().contains("D")) {
                Role authority = roleRepository.findByAuthority("DOCTOR");
                authorities.add(authority);
                user.setAuthorities(authorities);
            } else if(user.getUsername().contains("P")) {
                Role authority = roleRepository.findByAuthority("PATIENT");
                authorities.add(authority);
                user.setAuthorities(authorities);
            } else if(user.getUsername().contains("A")) {
                Role authority = roleRepository.findByAuthority("ADMIN");
                authorities.add(authority);
            } else {
                throw new InvalidDataException("Потребителското име е невалидно!");
            }
            userRepository.save(user);
        }
    }

    @Override
    public void updateUser(User currentUser) throws UserAlreadyExistsException, InvalidDataException {
        User user = userRepository.findByUsername(currentUser.getUsername());

        if(user != null) {
            if(!bCryptPasswordEncoder.matches(currentUser.getPassword(),user.getPassword())) {
                String newPassword = currentUser.getPassword();
                String newEncryptedPassword = bCryptPasswordEncoder.encode(newPassword);
                user.setPassword(newEncryptedPassword);

                userRepository.save(user);
            } else {
                throw new InvalidDataException("Новата парола трябва да е различна от старата!");
            }
        } else {
            throw new UserAlreadyExistsException("Вашето ID не съществува. Моля, обърнете се към поддръжка.");
        }
    }

    @Override
    public void deleteUser(String specialId) {
        User user = (User) this.loadUserByUsername(specialId);

        user.setAccountNonLocked(false);
        user.setCredentialsNonExpired(false);
        user.setAccountNonExpired(false);
        user.setEnabled(false);
    }

    @Override
    public boolean checkIfUserExist(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }
}
/*
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    @Override
    public void deleteUser(String specialId) {

    }

    @Override
    public boolean checkIfUserExist(String username) {
        return false;
    }

   // @Override
    //public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    //    return null;
    //}
    /*private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void registerUser(User user) throws UserAlreadyExistsException, InvalidDataException {
        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);

        if(checkIfUserExist(user.getUsername())){
            throw new UserAlreadyExistsException("Този потребител вече съществува!");
        } else {
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setAccountNonExpired(true);
            user.setEnabled(true);

            Set<Role> authorities = new HashSet<Role>(){};

            if(user.getUsername().contains("D")) {
                Role authority = roleRepository.findByAuthority("DOCTOR");
                authorities.add(authority);
                user.setAuthorities(authorities);
            } else if(user.getUsername().contains("P")) {
                Role authority = roleRepository.findByAuthority("PATIENT");
                authorities.add(authority);
                user.setAuthorities(authorities);
            } else if(user.getUsername().contains("A")) {
                Role authority = roleRepository.findByAuthority("ADMIN");
                authorities.add(authority);
            } else {
                throw new InvalidDataException("Потребителското име е невалидно!");
            }
            userRepository.save(user);
        }
    }

    @Override
    public void updateUser(User currentUser) throws UserAlreadyExistsException, InvalidDataException {
        User user = userRepository.findByUsername(currentUser.getUsername());

        if(user != null) {
            if(!bCryptPasswordEncoder.matches(currentUser.getPassword(),user.getPassword())) {
                String newPassword = currentUser.getPassword();
                String newEncryptedPassword = bCryptPasswordEncoder.encode(newPassword);
                user.setPassword(newEncryptedPassword);

                userRepository.save(user);
            } else {
                throw new InvalidDataException("Новата парола трябва да е различна от старата!");
            }
        } else {
            throw new UserAlreadyExistsException("Вашето ID не съществува. Моля, обърнете се към поддръжка.");
        }
    }

    @Override
    public void deleteUser(String specialId) {
        User user = (User) this.loadUserByUsername(specialId);

        user.setAccountNonLocked(false);
        user.setCredentialsNonExpired(false);
        user.setAccountNonExpired(false);
        user.setEnabled(false);
    }

    @Override
    public boolean checkIfUserExist(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }
}



/*     //   String t = "2021-12-20 15:30:12";
      //  DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");//.withZone(ZoneOffset.UTC);
     //   LocalDateTime date = LocalDateTime.parse(t, sdf);
        //java.sql.Loc sqlDate = Timestamp.valueOf(date);
      //  try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(time);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

            UserReservedSeat userReservedSeat = new UserReservedSeat();
            for (PlaySeat playSeat: selectedPlaySeats) {
                userReservedSeat.setPlaySeat(playSeat);
                userReservedSeat.setUser(user);
                userReservedSeat.setReservedTime(timestamp);
            }
            userReservedSeatDAO.save(userReservedSeat);
            userDAO.save(user);
    //    } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
      //  }*/