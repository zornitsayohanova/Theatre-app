package com.theatreapp.theatre.web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewGameAnswerViewModel {
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String correctAnswer;

    @NotBlank
    private String firstAnswer;

    @NotBlank
    private String secondAnswer;

    @NotBlank
    private String thirdAnswer;

    private String givenAnswer;
}
