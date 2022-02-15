package com.theatreapp.theatre.dto;

import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayCommentDTO {
    private long id;

    private User user;

    private Play play;

    private String positivePart;

    private String negativePart;
}
