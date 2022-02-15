package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.*;
import com.theatreapp.theatre.data.entities.*;
import com.theatreapp.theatre.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class GameService extends DTO {
    private final GameQuestionDAO gameQuestionDAO;
    private final GameParticipantDAO gameParticipantDAO;
    private final TheatreDAO theatreDAO;
    private final GameDAO gameDAO;
    private final PlayGenreDAO playGenreDAO;
    private final UserDAO userDAO;
    private final PlayDAO playDAO;

    public Game getGame() {
        return gameDAO.findAll().get(0);
    }

    public String getTitleOfCondition() {
        if(gameDAO.findAll().size() == 0) {
            return "";
        }

        String title = this.getGame().getTitleOfCondition();

        if(this.getGame().isPointsCondition()) {
            return "Тазседмичното условие е брой точки, равен на " + title;
        } else if(this.getGame().isTheatreCondition()) {
            return "Тазседмичното условие е най-гледан театър, а именно - " + title;
        } else {
            return "Тазседмичното условие е най-гледан жанр, а именно - " + title;
        }
    }

    public boolean hasUserAnswered(User user) {
        return gameParticipantDAO.findByUser(user) != null;
    }

    public List<GameAnswerDTO> getAllGameQuestions() {
        return gameQuestionDAO.findAll()
                .stream()
                .map(this::convertToGameAnswerDTO)
                .collect(Collectors.toList());
    }

    public void createGameParticipant(User user) throws Exception {
        if(gameParticipantDAO.findByUser(user) != null) {
            throw new Exception("Вече играхте за тази седмица!");
        }
        GameParticipant gameParticipant = new GameParticipant();
        gameParticipant.setUser(user);
        gameParticipant.setStartTime(this.getCurrentTime());

        gameParticipantDAO.save(gameParticipant);
    }

    public GameParticipant sendQuizAnswer(GameQuestionDTO gameQuestionDTO, User user) throws ParseException {
        String endTime = this.getCurrentTime();
        GameParticipant gameParticipant = gameParticipantDAO.findByUser(user);
        if(gameQuestionDTO.getAllQuestions() != null) {
            gameParticipant.setCorrectAnswersCount(this.findCorrectAnswers(gameQuestionDTO));
            gameParticipant.setTimeOfCompletionInMinutes(this.getTimeToCompleteQuiz(gameParticipant, endTime));
        }

        gameParticipantDAO.save(gameParticipant);

        user.setIsGameEligible(false);
        userDAO.save(user);

        return gameParticipant;
    }

    public String getCurrentTime() {
        int hours = LocalDateTime.now().getHour();
        int minutes = LocalDateTime.now().getMinute();
        int seconds =  LocalDateTime.now().getSecond();
        return hours + ":" + minutes + ":" + seconds;
    }

    public int findCorrectAnswers(GameQuestionDTO gameQuestionDTO) {
        int correctAnswers = 0;
        List<GameQuestion> questions = gameQuestionDAO.findAll();
        List<GameAnswerDTO> answers = gameQuestionDTO.getAllQuestions();

        for (int i = 0; i < answers.size(); i++) {
            if(answers.get(i).getGivenAnswer() != null) {
                if(questions.get(i).getCorrectAnswer().equals(answers.get(i).getGivenAnswer())) {
                    correctAnswers += 1;
                }
            }
        }
        return correctAnswers;
    }

    public float getTimeToCompleteQuiz(GameParticipant gameParticipant, String endTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Time end = new java.sql.Time(format.parse(endTime).getTime());
        Time start = new java.sql.Time(format.parse(gameParticipant.getStartTime()).getTime());
        float differenceInMilliSeconds = end.getTime() - start.getTime();
        return (differenceInMilliSeconds / 1000) / 60;
    }

    public void setGameConditionPoints(String points) throws Exception {

        if(gameDAO.findAll().size() != 0) {
            throw new Exception("Вече е въведено условие за седмичната игра!");
        }

        if(points.isEmpty()) {
            throw new Exception("Моля, въведете точки!");
        }

        Game game = new Game();
        game.setPointsCondition(true);
        game.setTitleOfCondition(points);
        game.setTimeOfCreation(Timestamp.from(Instant.now()));

        gameDAO.save(game);
    }

    public void setGameConditionGenre(PlayGenreDTO playGenreDTO) throws Exception {

        if(gameDAO.findAll().size() != 0) {
            throw new Exception("Вече е въведено условие за седмичната игра!");
        }

        if(playGenreDTO.getId() == 0) {
            throw new Exception("Моля, въведете жанр!");
        }

        Game game = new Game();
        game.setGenreCondition(true);
        game.setTitleOfCondition(playGenreDAO.findById(playGenreDTO.getId()).get().getName());
        game.setTimeOfCreation(Timestamp.from(Instant.now()));

        gameDAO.save(game);
    }

    public void setGameConditionTheatre(TheatreDTO theatreDTO) throws Exception {

        if(gameDAO.findAll().size() != 0) {
            throw new Exception("Вече е въведено условие за седмичната игра!");
        }

        if(theatreDTO.getId() == 0) {
            throw new Exception("Моля, въведете театър!");
        }

        Game game = new Game();
        game.setTheatreCondition(true);
        game.setTitleOfCondition(theatreDAO.findById(theatreDTO.getId()).get().getName());
        game.setTimeOfCreation(Timestamp.from(Instant.now()));

        gameDAO.save(game);
    }

    public void addGameQuestion(GameAnswerDTO gameAnswerDTO) throws Exception {
        if(gameQuestionDAO.findAll().size() == 5) {
            throw new Exception("Вече е въведен максималният брой въпроси за тази седмица!");
        }

        if(gameAnswerDTO.getFirstAnswer() == null ||
           gameAnswerDTO.getSecondAnswer() == null ||
           gameAnswerDTO.getThirdAnswer() == null ||
           gameAnswerDTO.getCorrectAnswer() == null) {
            throw new Exception("Моля, въведете въпрос!");
        }

        GameQuestion gameQuestion = new GameQuestion();
        gameQuestion.setTitle(gameAnswerDTO.getTitle());
        gameQuestion.setFirstAnswer(gameAnswerDTO.getFirstAnswer());
        gameQuestion.setSecondAnswer(gameAnswerDTO.getSecondAnswer());
        gameQuestion.setThirdAnswer(gameAnswerDTO.getThirdAnswer());
        gameQuestion.setCorrectAnswer(gameAnswerDTO.getCorrectAnswer());

        gameQuestionDAO.save(gameQuestion);
    }

    public void removePreviousWonTicketsFlag() {
        userDAO.findAll().forEach(u -> u.setHasWonTicket(false));
    }

    public void setEligibleUsers() {
        if(gameQuestionDAO.findAll().size() == 5) {
            Game game = gameDAO.findAll().get(0);
            if(game.isPointsCondition()) {
                this.setByPointsCondition(game);
            } else {
                List<User> users = userDAO.findAll();
                if (game.isTheatreCondition()) {
                    this.setByTheatreCondition(game, users);
                } else {
                    this.setByGenreCondition(game, users);
                }
            }
        }
    }

    public void setByPointsCondition(Game game) {
        int requiredScore = Integer.parseInt(game.getTitleOfCondition());
        userDAO.setScoreGameEligibleUsers(requiredScore);
    }

    public void setByTheatreCondition(Game game, List<User> users) {
        for (User user : users) {
            List<String> theatreStatistics = playDAO.getUserWatchedTheatresStatistics(user);
            if(theatreStatistics.size() > 0) {
                String theatreId = theatreStatistics.get(0).split(",")[0];
                String theatreName = theatreDAO.findById(Long.parseLong(theatreId)).get().getName();
                if(game.getTitleOfCondition().equals(theatreName)) {
                    user.setIsGameEligible(true);
                }
            }
        }
    }

    public void setByGenreCondition(Game game, List<User> users) {
        for (User user : users) {
            List<String> genresStatistics = playDAO.getUserWatchedGenresStatistics(user);
            if(genresStatistics.size() > 0) {
                String theatreId = genresStatistics.get(0).split(",")[0];
                String genreName = playGenreDAO.findById(Long.parseLong(theatreId)).get().getName();
                if(game.getTitleOfCondition().equals(genreName)) {
                    user.setIsGameEligible(true);
                }
            }
        }
    }
}
