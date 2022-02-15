package com.theatreapp.theatre.dto;

import com.theatreapp.theatre.data.entities.Actor;
import com.theatreapp.theatre.data.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ActorRatingDTO {
    private long id;

    private User user;

    private Actor actor;

    private float rating;
}
