package com.theatreapp.theatre.web.models;

import com.theatreapp.theatre.data.entities.*;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewPlayViewModel {
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private List<Actor> actors;

    @NotNull
    private PlayGenre playGenre;

    @NotNull
    private Theatre theatre;

    @NotNull
    private TheatreHall theatreHall;

    @NotNull
    private List<Picture> gallery;

    @NotBlank
    private String author;

    @NotBlank
    private String creative;

    @NotBlank
    private String description;

    @NotBlank
    private String playPrices;
}
