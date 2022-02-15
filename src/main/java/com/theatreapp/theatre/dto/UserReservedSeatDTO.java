package com.theatreapp.theatre.dto;

import com.theatreapp.theatre.data.entities.PlaySeat;
import com.theatreapp.theatre.data.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserReservedSeatDTO {
    long id;

    private User user;

    private PlaySeat playSeat;

    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp timeOfReservation;

    private String remainingTime;
}
