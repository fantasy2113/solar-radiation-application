package de.josmer.app.lib.interfaces;

import de.josmer.app.entities.User;

import java.util.Optional;

public interface IUserRepository {

    Optional<User> get(int id);

    Optional<User> get(String login);

    void saveUser(String login, String plainPassword);
}