package com.theatreapp.theatre.dto;

import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayRatingDTO {
    private long id;

    private User user;

    private Play play;

    private float rating;
}
