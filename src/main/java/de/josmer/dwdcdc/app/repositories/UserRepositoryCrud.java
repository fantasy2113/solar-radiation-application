package de.josmer.dwdcdc.app.repositories;

import de.josmer.dwdcdc.app.entities.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
interface UserRepositoryCrud extends CrudRepository<User, Integer> {

    Optional<User> findByUsername(@Param("username") String username);
}
