package com.theatreapp.theatre.web.models;

import com.theatreapp.theatre.data.entities.Actor;
import com.theatreapp.theatre.data.entities.Picture;
import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.Theatre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewNewsViewModel {
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private Picture avatar;

    @NotBlank
    private String content;

    Date date;

    List<Play> plays;

    List<Actor> actors;

    List<Theatre> theatres;
}
