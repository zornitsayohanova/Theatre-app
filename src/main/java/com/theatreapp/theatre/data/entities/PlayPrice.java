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
public class PlayPrice extends BaseEntity{
    @ManyToOne
    private Theatre theatre;

    @ManyToOne
    private Play play;

    @Column
    private String fromRow;

    @Column
    private String toRow;

    @Column
    private Float price;
}
