package de.josmer.solarradiation.repositories;

import de.josmer.solarradiation.entities.User;
import de.josmer.solarradiation.libraries.utils.UserBCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public final class UserRepository {
    private final UserRepositoryCrud userRepositoryCrud;
    private final UserBCrypt userBCrypt;

    @Autowired
    public UserRepository(UserRepositoryCrud userRepositoryCrud, UserBCrypt userBCrypt) {
        this.userRepositoryCrud = userRepositoryCrud;
        this.userBCrypt = userBCrypt;
    }

    public Optional<User> get(final Integer id) {
        Optional<User> optionalUser = userRepositoryCrud.findById(id);
        if (optionalUser.isPresent() && isValid(optionalUser.get())) {
            return optionalUser;
        }
        return Optional.empty();
    }

    public Optional<User> get(final String username) {
        Optional<User> optionalUser = userRepositoryCrud.findByUsername(username);
        if (optionalUser.isPresent() && isValid(optionalUser.get())) {
            return optionalUser;
        }
        return Optional.empty();
    }

    public void createUser(final String username, final String plainPassword) {
        if (userRepositoryCrud.findByUsername(username).isEmpty()) {
            userRepositoryCrud.save(initUser(username, plainPassword));
        }
    }

    private User initUser(final String username, final String plainTextPassword) {
        LocalDateTime localDateTime = LocalDateTime.now();
        User user = new User();
        user.setPassword(userBCrypt.hashPassword(plainTextPassword));
        user.setUsername(username);
        user.setCreated(Timestamp.valueOf(localDateTime));
        user.setModified(Timestamp.valueOf(localDateTime));
        user.setLastLogin(Timestamp.valueOf(localDateTime));
        user.setIsActive(true);
        return user;
    }

    private boolean isValid(User user) {
        return user.isActive();
    }
}
