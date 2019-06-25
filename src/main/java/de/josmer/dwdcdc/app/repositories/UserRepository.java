package de.josmer.dwdcdc.app.repositories;

import de.josmer.dwdcdc.app.entities.User;
import de.josmer.dwdcdc.app.interfaces.IUserBCrypt;
import de.josmer.dwdcdc.app.interfaces.IUserRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class UserRepository implements IUserRepository {

    private final UserRepositoryCrud userRepositoryCrud;
    private final IUserBCrypt userBCrypt;

    @Autowired
    public UserRepository(UserRepositoryCrud userRepositoryCrud, IUserBCrypt userBCrypt) {
        this.userRepositoryCrud = userRepositoryCrud;
        this.userBCrypt = userBCrypt;
    }

    @Override
    public Optional<User> get(final Integer id) {
        Optional<User> optionalUser = userRepositoryCrud.findById(id);
        if (optionalUser.isPresent() && isValid(optionalUser.get())) {
            return optionalUser;
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> get(final String username) {
        Optional<User> optionalUser = userRepositoryCrud.findByUsername(username);
        if (optionalUser.isPresent() && isValid(optionalUser.get())) {
            return optionalUser;
        }
        return Optional.empty();
    }

    @Override
    public void createUser(final String username, final String plainPassword) {
        if (userRepositoryCrud.findByUsername(username).isEmpty()) {
            userRepositoryCrud.save(initUser(username, plainPassword));
        }
    }

    @Override
    public void updateLastLogin(User user) {
        user.setLastLogin(Timestamp.valueOf(LocalDateTime.now()));
        userRepositoryCrud.save(user);
    }

    @Override
    public void deactivateUser(User user) {
        user.setIsActive(false);
        userRepositoryCrud.save(user);
    }

    @Override
    public void activateUser(User user) {
        user.setIsActive(true);
        userRepositoryCrud.save(user);
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
