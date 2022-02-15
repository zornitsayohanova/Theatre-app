package com.theatreapp.theatre.web.models;

import com.theatreapp.theatre.data.entities.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewTheatreViewModel {
    private Long id;

    @NotNull
    private TheatreType type;

    @NotNull
    private TheatreGenre genre;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Town town;

    @NotBlank
    private String contacts;

    @NotBlank
    private String workHours;

    @NotNull
    private List<Picture> gallery;

    @NotNull
    private List<Actor> actors;
}
