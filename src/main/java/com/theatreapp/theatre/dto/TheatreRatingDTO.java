package com.theatreapp.theatre.dto;

import com.theatreapp.theatre.data.entities.Theatre;
import com.theatreapp.theatre.data.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TheatreRatingDTO {
    private long id;

    private User user;

    private Theatre theatre;

    private float rating;
}
