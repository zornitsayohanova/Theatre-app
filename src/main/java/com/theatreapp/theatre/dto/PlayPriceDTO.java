package com.theatreapp.theatre.dto;

import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.Theatre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlayPriceDTO {
    private long id;

    private Theatre theatre;

    private Play play;

    private String fromRow;

    private String toRow;

    private Float price;
}
