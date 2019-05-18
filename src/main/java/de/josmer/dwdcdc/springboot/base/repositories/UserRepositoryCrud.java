package de.josmer.dwdcdc.springboot.base.repositories;

import de.josmer.dwdcdc.springboot.base.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
interface UserRepositoryCrud extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(@Param("username") String username);
}
