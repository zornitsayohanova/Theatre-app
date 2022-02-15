package com.theatreapp.theatre.web.models;

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
public class PlayPriceViewModel {
    private long id;

    private Theatre theatre;

    private Play play;

    private String fromRow;

    private String toRow;

    private Float price;
}
