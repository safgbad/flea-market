package ru.skypro.flea.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.skypro.flea.model.Ad;

import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

    @Override
    @NonNull
    Optional<Ad> findById(@NonNull Integer id);

    @Override
    boolean existsById(@NonNull Integer integer);

    @Override
    void deleteById(@NonNull Integer integer);

}
