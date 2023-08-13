package ru.skypro.flea.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skypro.flea.model.Ad;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

    @Modifying
    @Query("UPDATE Ad a SET a.image = :image WHERE a.id = :id")
    void updateImageById(String image, Integer id);

}
