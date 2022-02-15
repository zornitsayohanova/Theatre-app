package com.theatreapp.theatre.data.entities;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TheatreHall extends BaseEntity {
    @OneToOne
    private Theatre theatre;

    @Column
    private String hallName;

    @Column
    private Integer hallRows;

    @Column
    private Integer hallColumns;

    @Column(length = 8000)
    private String disabledSeats;

    @Column(length = 8000)
    private String enabledSeats;
}
