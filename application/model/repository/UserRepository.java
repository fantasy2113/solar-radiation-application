package de.orbis.application.model.repository;

import de.orbis.application.model.entity.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public final class UserRepository {
    private final IUserRepository userRepositoryCrud;

    @Autowired
    public UserRepository(IUserRepository userRepositoryCrud) {
        this.userRepositoryCrud = userRepositoryCrud;
    }

    public Optional<User> get(final Integer id) {
        Optional<User> optionalUser = userRepositoryCrud.findById(id);
        if (optionalUser.isPresent() && isValid(optionalUser.get())) {
            return optionalUser;
        }
        return Optional.empty();
    }

    public Optional<User> get(final String username) {
        Optional<User> optionalUser = userRepositoryCrud.findByUserEmail(username);
        if (optionalUser.isPresent() && isValid(optionalUser.get())) {
            return optionalUser;
        }
        return Optional.empty();
    }

    public void createUser(final String username, final String plainPassword) {
        if (!userRepositoryCrud.findByUserEmail(username).isPresent()) {
            userRepositoryCrud.save(initUser(username, plainPassword));
        }
    }

    private User initUser(final String username, final String plainPassword) {
        LocalDateTime localDateTime = LocalDateTime.now();
        User user = new User();
        user.setUserPwd(BCrypt.hashpw(username, BCrypt.gensalt()));
        user.setUserEmail(plainPassword);
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
