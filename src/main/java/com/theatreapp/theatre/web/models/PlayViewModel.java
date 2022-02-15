package com.theatreapp.theatre.web.models;

import com.sun.istack.Nullable;
import com.theatreapp.theatre.data.entities.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayViewModel {
    private Long id;

    private String name;

    private Picture avatar;

    private List<Actor> actors;

    private PlayGenre playGenre;

    @DateTimeFormat (pattern="yyyy-MM-dd")
    private Date date;

    private Town town;

    private String day;

    private String month;

    private Theatre theatre;

    private Picture mainPic;

    private TheatreHall theatreHall;

    private List<Picture> gallery;

    private String author;

    private String creative;

    private String description;

    private List<PlaySeat> activeSeats;

    private List<PlayComment> playComments;
}
