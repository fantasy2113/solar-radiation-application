package de.josmer.dwdcdc.springboot.base.interfaces;

import de.josmer.dwdcdc.springboot.base.entities.User;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> get(Integer id);

    Optional<User> get(String username);

    void createUser(String username, String plainPassword);
}
