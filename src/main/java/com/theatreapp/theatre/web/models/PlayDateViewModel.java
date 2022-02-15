package com.theatreapp.theatre.web.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.Theatre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayDateViewModel {
    private long id;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    private Theatre theatre;

    @NotNull
    private Play play;

    @NotBlank
    private String day;

    @NotBlank
    private String time;
}
