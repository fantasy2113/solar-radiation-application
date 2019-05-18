package de.josmer.solardb.repositories;

import de.josmer.solardb.base.interfaces.IUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
interface UserRepositoryCrud extends CrudRepository<IUser, Integer> {
    Optional<IUser> findByUsername(@Param("username") String username);
}
