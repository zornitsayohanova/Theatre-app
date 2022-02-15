package com.theatreapp.theatre.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameAnswerDTO {
    private Long id;

    private String title;

    private String correctAnswer;

    private String firstAnswer;

    private String secondAnswer;

    private String thirdAnswer;

    private String givenAnswer;
}
