package com.theatreapp.theatre.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserReservedSeat extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private PlaySeat playSeat;

    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private Timestamp timeOfReservation;

    @Column (length = 10)
    private String remainingTime;

    @Column
    private Boolean isWon;
}
