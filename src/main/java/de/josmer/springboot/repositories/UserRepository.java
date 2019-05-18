package de.josmer.springboot.repositories;

import de.josmer.springboot.base.UserBCrypt;
import de.josmer.springboot.base.interfaces.IUser;
import de.josmer.springboot.base.interfaces.IUserRepository;
import de.josmer.springboot.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public final class UserRepository implements IUserRepository {
    private final UserRepositoryCrud userRepositoryCrud;
    private final UserBCrypt userBCrypt;

    @Autowired
    public UserRepository(UserRepositoryCrud userRepositoryCrud, UserBCrypt userBCrypt) {
        this.userRepositoryCrud = userRepositoryCrud;
        this.userBCrypt = userBCrypt;
    }

    @Override
    public Optional<IUser> get(final Integer id) {
        Optional<IUser> optionalUser = userRepositoryCrud.findById(id);
        if (optionalUser.isPresent() && isValid(optionalUser.get())) {
            return optionalUser;
        }
        return Optional.empty();
    }

    @Override
    public Optional<IUser> get(final String username) {
        Optional<IUser> optionalUser = userRepositoryCrud.findByUsername(username);
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

    private IUser initUser(final String username, final String plainTextPassword) {
        LocalDateTime localDateTime = LocalDateTime.now();
        IUser user = new User();
        user.setPassword(userBCrypt.hashPassword(plainTextPassword));
        user.setUsername(username);
        user.setCreated(Timestamp.valueOf(localDateTime));
        user.setModified(Timestamp.valueOf(localDateTime));
        user.setLastLogin(Timestamp.valueOf(localDateTime));
        user.setIsActive(true);
        return user;
    }

    private boolean isValid(IUser user) {
        return user.isActive();
    }
}
