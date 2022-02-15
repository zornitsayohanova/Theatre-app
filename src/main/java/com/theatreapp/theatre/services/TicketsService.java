package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.PlayDAO;
import com.theatreapp.theatre.data.dao.PlayPriceDAO;
import com.theatreapp.theatre.data.dao.PlaySeatDAO;
import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.PlaySeat;
import com.theatreapp.theatre.data.entities.Theatre;
import com.theatreapp.theatre.dto.DTO;
import com.theatreapp.theatre.dto.PlayDTO;
import com.theatreapp.theatre.dto.PlayPriceDTO;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@AllArgsConstructor
public class TicketsService extends DTO {
    private final PlayDAO playDAO;
    private final PlayPriceDAO playPriceDAO;
    private final PlaySeatDAO playSeatDAO;

    public List<PlayPriceDTO> getSeatPrices(PlayDTO playDTO) {
        Play play = convertToPlay(playDTO);
        Theatre theatre = playDAO.findById(playDTO.getId()).get().getTheatre();

        return playPriceDAO.findAllByPlayAndTheatre(play, theatre)
                .stream()
                .map(this::convertToPlayPriceDTO)
                .collect(Collectors.toList());
    }

    public void checkTimeReservation(String dateString, Play play, String timeReservationString) throws Exception{
        Timestamp timeReservation = Timestamp.valueOf(timeReservationString);
        Timestamp seatsDate = this.findDate(this.convertToDate(dateString), this.convertToPlayDTO(play));

        Timestamp reservationTimePlus30Min = new Timestamp (timeReservation.getTime() + (30*60*1000));

        if(reservationTimePlus30Min.after(seatsDate)){
            throw new Exception("Вече не може да резервирате места за тази постановка!");
        }
    }

    public List<PlaySeat> getUserSelectedSeats(String seats, String playId, String dateString, String timeOfReservationString)
            throws Exception {
        if(seats.equals("none")) {
            throw new Exception("Моля, изберете места!");
        }
        Play play = playDAO.findById(Long.parseLong(playId)).get();
        Timestamp seatsDate = this.findDate(this.convertToDate(dateString), this.convertToPlayDTO(play));
        this.checkTimeReservation(dateString, play, timeOfReservationString);

        List<PlaySeat> playSeats = new ArrayList<>();
        String[] selectedSeats = seats.split("-");

        for (String seat : selectedSeats) {
            playSeats.add(playSeatDAO.findAllBySeatIdentifierAndPlayAndDate(seat, play, seatsDate));
        }
        return playSeats;
    }

    public Date convertToDate(String dateString) throws ParseException {
        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = sdf.parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        return sqlDate;
    }

    public String getPlayReservedSeats(PlayDTO playDTO, Date date) throws Exception {
        Play play = convertToPlay(playDTO);
        Timestamp playDate = new Timestamp(date.getTime());

        return IntStream.of(playSeatDAO.getReservedSeats(play, playDate))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
    }

    public List<Timestamp> getPlayDates(PlayDTO playDTO) {
        return playSeatDAO.getPlayDates(this.convertToPlay(playDTO));
    }

    public Timestamp findDate(Date date, PlayDTO playDTO) {
        Timestamp timestamp = new Timestamp(date.getTime());

        return playSeatDAO.findByDateAndPlay(timestamp, this.convertToPlay(playDTO));
    }

    public void reserveSelectedSeats(List<PlaySeat> selectedPlaySeats) {
        for (PlaySeat playSeat : selectedPlaySeats) {
            PlaySeat currentPlaySeat = playSeatDAO.findAllBySeatIdentifierAndPlayAndDate
                    (playSeat.getSeatIdentifier(), playSeat.getPlay(), playSeat.getDate());

            currentPlaySeat.setFree(false);

            playSeatDAO.save(currentPlaySeat);
        }
    }

    public List<PlaySeat> reserveAndViewRandomSeatsForWinner() throws Exception {
        if(playSeatDAO.findAll().size() == 0) {
            throw new Exception("Възникна проблем в системата!");
        }

        if(playSeatDAO.getTwoRandomSeats() == null) {
            throw new Exception("Няма свободни места в София");
        }

        String[] seatsIds = playSeatDAO.getTwoRandomSeats().split(",");
        List<PlaySeat> seats = new ArrayList<>();

        for (String seatId: seatsIds) {
             PlaySeat playSeat = playSeatDAO.findById(Long.parseLong(seatId)).get();
             seats.add(playSeat);
        }

        this.reserveSelectedSeats(seats);

        return seats;
    }
}
