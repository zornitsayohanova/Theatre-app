package com.theatreapp.theatre.data.entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PlaySeat extends BaseEntity {
    @ManyToOne
    private Play play;

    @Column
    private String seatIdentifier;

    @Column(name = "is_free")
    private boolean isFree;

    @Column
    private Timestamp date;

    @ManyToOne
    private PlayPrice price;

    @Column
    private boolean isActive;

    @Column
    private String day;
}

