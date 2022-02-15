package com.theatreapp.theatre.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Game extends BaseEntity {
    @Column
    private String titleOfCondition;

//    @Column
//    private int weekCondition;

    @Column
    private boolean isPointsCondition;

    @Column
    private boolean isTheatreCondition;

    @Column
    private boolean isGenreCondition;

    @Column
    private Timestamp timeOfCreation;
}
