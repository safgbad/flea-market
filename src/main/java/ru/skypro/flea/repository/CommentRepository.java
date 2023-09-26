package ru.skypro.flea.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.skypro.flea.model.Ad;
import ru.skypro.flea.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByAdOrderByIdDesc(@NonNull Ad ad);

    Optional<Comment> findByIdAndAd(@NonNull Integer id, @NonNull Ad ad);

}
