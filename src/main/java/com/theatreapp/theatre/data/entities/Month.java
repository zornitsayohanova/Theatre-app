package com.theatreapp.theatre.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class Month {
    private List<String> days;
    private List<String> months;

    public Month() {
        this.months = Arrays.asList(
                "Януари", "Февруари", "Март", "Април",
                "Май", "Юни", "Юли", "Август", "Септември", "Октомври", "Ноември", "Декември"
        );
        this.days = Arrays.asList(
                "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24",
                "25", "26", "27", "28","29","30","31"
        );
    }

}
