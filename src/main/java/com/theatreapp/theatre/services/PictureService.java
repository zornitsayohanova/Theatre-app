package com.theatreapp.theatre.services;

import com.theatreapp.theatre.data.dao.*;
import com.theatreapp.theatre.data.entities.Picture;
import com.theatreapp.theatre.dto.DTO;
import com.theatreapp.theatre.dto.PictureDTO;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PictureService extends DTO {
    private final PictureDAO pictureDAO;

    public void addPicture(PictureDTO pictureDTO) throws Exception {
        if(pictureDAO.findByName(pictureDTO.getName()) != null) {
            throw new Exception("Тази снимка вече съществува!");
        }
        String picName = pictureDTO.getFile().getOriginalFilename();
        String category = this.setCategory(pictureDTO);

        String path = this.checkPath(category, pictureDTO, picName);
        Picture picture = new Picture();
        picture.setName(pictureDTO.getName());
        picture.setPath(path);

        pictureDAO.save(picture);
    }

    public String setCategory(PictureDTO pictureDTO) {
        String category = "";
        if(pictureDTO.getSelectedCategory().equals("Актьор")) {
            category = "актьори";
        } else if (pictureDTO.getSelectedCategory().equals("Постановка")) {
            category = "постановки";
        } else {
            category = "театри";
        }
        return category;
    }

    public String checkPath(String category, PictureDTO pictureDTO, String picName) throws Exception {
        String path = "C:\\Users\\Owner\\Desktop\\theatre\\src\\main\\resources\\static\\pics\\";
        String folderPath = path + "\\" + category + "\\" + pictureDTO.getFolderName() + "\\";
        String picPath = path + category + "\\" + pictureDTO.getFolderName() + "\\" + picName;

        if(!Files.isDirectory(Paths.get(folderPath))) {
            throw new Exception("Тази папка не съществува!");
        }

        if(!Files.exists(Paths.get(picPath))) {
            throw new Exception("Тази снимка не съществува!");
        }

        return "/pics/" + category + "/" + pictureDTO.getFolderName() + "/" + picName;
    }

    public List<PictureDTO> getAllPictures() {
        return pictureDAO.findAll()
                .stream()
                .map(this::convertToPictureDTO)
                .collect(Collectors.toList());
    }
}
