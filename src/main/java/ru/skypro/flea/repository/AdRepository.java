package ru.skypro.flea.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.flea.model.Ad;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {
}
