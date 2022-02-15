package com.theatreapp.theatre.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GameParticipant extends BaseEntity {
    @ManyToOne
    private User user;

    @Column
    private int correctAnswersCount;

    @Column
    private String startTime;

    @Column
    private float timeOfCompletionInMinutes;
}
