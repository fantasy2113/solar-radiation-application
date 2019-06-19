package de.josmer.dwdcdc.app.base.interfaces;

import de.josmer.dwdcdc.app.base.entities.User;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> get(Integer id);

    Optional<User> get(String username);

    void createUser(String username, String plainPassword);

    void updateLastLogin(User user);

    void deactivateUser(User user);

    void activateUser(User user);
}
