package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.PlayComment;
import com.theatreapp.theatre.data.entities.Play;
import com.theatreapp.theatre.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;
import java.util.List;

public interface PlayCommentDAO extends JpaRepository<PlayComment, Long> {
    List<PlayComment> findAllByPlay(Play play);

    @Transactional
    @Modifying
    void deleteAllByUser(User user);

    @Modifying
    @Transactional
    void removeAllByPlay(Play play);
}
