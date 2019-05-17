package de.josmer.application.model.repositories;


import de.josmer.application.model.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
interface IUserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(@Param("username") String username);
}
