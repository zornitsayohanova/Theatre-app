package com.theatreapp.theatre.web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameAnswerViewModel {
    private Long id;

    private String title;

    private String correctAnswer;

    private String firstAnswer;

    private String secondAnswer;

    private String thirdAnswer;

    private String givenAnswer;
}
