package com.theatreapp.theatre.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.PlayPrice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaySeatDTO {
    private long id;

    private Play play;

    private String seatIdentifier;

    private boolean isFree;

    private Date date;

    private PlayPrice price;

    private boolean isActive;

    private String day;

    private String time;
}
