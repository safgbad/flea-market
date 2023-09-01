package ru.skypro.flea.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skypro.flea.model.Ad;
import ru.skypro.flea.model.User;

import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

    List<Ad> findAllByOrderByIdDesc();

    @Modifying
    @Query("UPDATE Ad a SET a.image = :image WHERE a.id = :id")
    void updateImageById(String image, Integer id);

    List<Ad> findAdsByUserOrderByIdDesc(User user);

}
