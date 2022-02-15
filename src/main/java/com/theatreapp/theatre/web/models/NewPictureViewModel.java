package com.theatreapp.theatre.web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewPictureViewModel {
    private long id;

    @NotNull
    private MultipartFile file;

    @NotBlank
    private String name;

    @NotBlank
    private String folderName;

    @NotNull
    private String selectedCategory;

    private List<String> categories = Arrays.asList("Актьор", "Постановка", "Театър");
}
