package de.jos.dwdcdc.app.repositories;

import de.jos.dwdcdc.app.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
interface UserRepositoryCrud extends CrudRepository<User, Long> {

  Optional<User> findByUsername(@Param("username") String username);
}
