package com.theatreapp.theatre.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class News extends BaseEntity {
    @Column
    private String title;

    @ManyToOne
    private Picture avatar;

    @Column (length = 8000)
    private String content;

    @Column
    Date date;

    @ManyToMany
    List<Play> plays;

    @ManyToMany
    List<Actor> actors;

    @ManyToMany
    List<Theatre> theatres;
}
