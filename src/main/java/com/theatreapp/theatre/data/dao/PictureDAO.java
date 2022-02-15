package com.theatreapp.theatre.data.dao;
import com.sun.istack.Nullable;
import com.theatreapp.theatre.data.entities.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface PictureDAO extends JpaRepository<Picture, Long> {
    Picture findByName(String name);

    @Query(value = "select case when exists " +
            "(select * from play_gallery where gallery_id=:picture) " +
            "then 'true' else 'false' end" ,
            nativeQuery = true)
    Boolean findIfPlayPictureGalleryExists(Picture picture);

    @Query(value = "select case when exists " +
            "(select * from actor_gallery where gallery_id=:picture) " +
            "then 'true' else 'false' end" ,
            nativeQuery = true)
    Boolean findIfActorPictureGalleryExists(Picture picture);

    @Query(value = "select case when exists " +
            "(select * from theatre_gallery where gallery_id=:picture) " +
            "then 'true' else 'false' end" ,
            nativeQuery = true)
    Boolean findIfTheatrePictureGalleryExists(Picture picture);

    @Modifying
    @Transactional
    @Query(value = "delete from picture " +
            "where id=:picture " ,
            nativeQuery = true)
    void deleteAllByPicture(Picture picture);
}
