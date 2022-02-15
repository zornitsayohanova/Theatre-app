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
public class Actor extends BaseEntity {
    @Column
    private String name;

    @ManyToOne
    private ActorGender actorGender;

    @ManyToOne
    private Picture avatar;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @OneToMany
    private List<Picture> gallery;
}
