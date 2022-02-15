package com.theatreapp.theatre.dto;

import com.theatreapp.theatre.data.entities.ActorGender;
import com.theatreapp.theatre.data.entities.Picture;
import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.Theatre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActorDTO {
    private long id;

    private String name;

    private ActorGender actorGender;

    private Picture avatar;

    private String bio;

    private double rating;

    private Theatre theatre;

    private List<Picture> gallery;

    private List<Play> plays;
}