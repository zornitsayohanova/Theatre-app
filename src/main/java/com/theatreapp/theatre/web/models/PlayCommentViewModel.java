package com.theatreapp.theatre.web.models;

import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayCommentViewModel {
    private long id;

    private User user;

    private Play play;

    @NotBlank
    private String positivePart;

    private Integer positivePartLikes;

    private Integer positivePartDislikes;

    @NotBlank
    private String negativePart;

    private Integer negativePartLikes;

    private Integer negativePartDislikes;
}
