package com.theatreapp.theatre.web.models;

import com.theatreapp.theatre.data.entities.Theatre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewTheatreHallViewModel {
    private Long id;

    @NotNull
    private Theatre theatre;

    @NotBlank
    private String hallName;

    @Min(1)
    private Integer hallRows;

    @Min(1)
    private Integer hallColumns;

    @NotBlank
    private String disabledSeats;

    @NotBlank
    private String enabledSeats;
}
