package de.josmer.springboot.repositories;

import de.josmer.springboot.base.interfaces.IUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
interface UserRepositoryCrud extends CrudRepository<IUser, Integer> {
    Optional<IUser> findByUsername(@Param("username") String username);
}
