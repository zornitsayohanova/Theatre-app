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
public class Theatre extends BaseEntity {
    @ManyToOne
    private TheatreType type;

    @ManyToOne
    private TheatreGenre genre;

    @Column (columnDefinition = "TINYTEXT")
    private String name;

    @ManyToOne
    private Picture avatar;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String workHours;

    @ManyToOne
    private Town town;

    @Column(columnDefinition = "TEXT")
    private String contacts;

    @OneToMany
    private List<Picture> gallery;

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "theatre_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private List<Actor> actors;
}
