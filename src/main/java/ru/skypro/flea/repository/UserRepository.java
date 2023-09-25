package ru.skypro.flea.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.flea.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
