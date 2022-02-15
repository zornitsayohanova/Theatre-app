package com.theatreapp.theatre.dto;

import com.theatreapp.theatre.data.entities.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayDTO {
    private long id;

    private String name;

    private Picture avatar;

    private List<Actor> actors;

    private PlayGenre playGenre;

    @DateTimeFormat (pattern="yyyy-MM-dd")
    private Timestamp date;

    private Town town;

    private String day;

    private TheatreHall theatreHall;

    private String month;

    private Theatre theatre;

    private Picture mainPic;

    private List<Picture> gallery;

    private String author;

    private String creative;

    private String description;

    private List<PlayComment> playComments;

    private String playPrices;
}
