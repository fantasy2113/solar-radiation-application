package de.josmer.springboot.dwdcdc.app.interfaces;

import de.josmer.springboot.dwdcdc.app.entities.User;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> get(Integer id);

    Optional<User> get(String username);

    void createUser(String username, String plainPassword);
}
