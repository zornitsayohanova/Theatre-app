package com.theatreapp.theatre.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GameQuestion extends BaseEntity {
    @Column
    private String title;

    @Column
    private String correctAnswer;

    @Column
    private String firstAnswer;

    @Column
    private String secondAnswer;

    @Column
    private String thirdAnswer;
}
