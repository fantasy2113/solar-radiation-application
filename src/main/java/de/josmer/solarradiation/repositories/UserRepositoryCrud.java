package de.josmer.solarradiation.repositories;

import de.josmer.solarradiation.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
interface UserRepositoryCrud extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(@Param("username") String username);
}
