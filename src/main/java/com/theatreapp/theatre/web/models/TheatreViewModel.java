package com.theatreapp.theatre.web.models;

import com.theatreapp.theatre.data.entities.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TheatreViewModel {
    private Long id;

    private Picture avatar;

    private TheatreType type;

    private TheatreGenre genre;

    private String name;

    private String description;

    private Town town;

    private List<Play> playDetails;

    private String contacts;

    private String workHours;

    private List<Picture> gallery;

    private List<Actor> actors;

    private List<Play> plays;
}