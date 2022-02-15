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
public class PlayComment extends BaseEntity {
    @ManyToOne
    private User user;

    @ManyToOne
    private Play play;

    @Column(length = 8000)
    private String positivePart;

    @Column(length = 8000)
    private String negativePart;
}
