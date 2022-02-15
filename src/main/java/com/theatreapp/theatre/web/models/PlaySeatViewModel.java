package com.theatreapp.theatre.web.models;

import com.mysql.cj.xdevapi.TableImpl;
import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.PlayPrice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaySeatViewModel {
    private long id;

    private Play play;

    private String seatIdentifier;

    private boolean isFree;

    private Timestamp date;

    private PlayPrice price;

    private boolean isActive;

    private String day;

    private String time;
}
