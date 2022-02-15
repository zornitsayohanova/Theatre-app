package com.theatreapp.theatre.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PictureDTO {
    private long id;

    private String path;

    private String name;

    private MultipartFile file;

    private String folderName;

    private String selectedCategory;

    private List<String> categories = Arrays.asList("Актьор", "Постановка", "Театър");
}
