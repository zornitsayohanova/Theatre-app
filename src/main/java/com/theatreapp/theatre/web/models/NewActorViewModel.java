package com.theatreapp.theatre.web.models;

import com.theatreapp.theatre.data.entities.ActorGender;
import com.theatreapp.theatre.data.entities.Picture;
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
public class NewActorViewModel {
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private ActorGender actorGender;

    @NotBlank
    private String bio;

    @NotNull
    private List<Picture> gallery;
}
