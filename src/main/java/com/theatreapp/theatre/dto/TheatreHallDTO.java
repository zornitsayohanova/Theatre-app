package com.theatreapp.theatre.dto;

import com.theatreapp.theatre.data.entities.Theatre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TheatreHallDTO {
    private long id;

    private Theatre theatre;

    private String hallName;

    private Integer hallRows;

    private Integer hallColumns;

    private String disabledSeats;

    private String enabledSeats;
}
