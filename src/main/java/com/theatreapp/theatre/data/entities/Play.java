package com.theatreapp.theatre.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Play extends BaseEntity {

    @Column
    private String name;

    @Column (columnDefinition = "TINYTEXT")
    private String author;

    @ManyToOne
    private PlayGenre playGenre;

    @ManyToOne
    private Theatre theatre;

    @ManyToOne
    private Picture avatar;

    @OneToMany
    private List<Picture> gallery;

    @Column (length = 8000)
    private String creative;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne
    private TheatreHall theatreHall;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "play_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private List<Actor> actors;
}
