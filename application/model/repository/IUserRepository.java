package de.orbis.application.model.repository;

import de.orbis.application.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
interface IUserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUserEmail(@Param("user_email") String userEmail);
}
