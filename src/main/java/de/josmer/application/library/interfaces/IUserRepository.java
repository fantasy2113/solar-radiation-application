package de.josmer.application.library.interfaces;

import de.josmer.application.entities.User;

import java.util.Optional;

public interface IUserRepository {

    Optional<User> get(int id);

    Optional<User> get(String login);

    void saveUser(String login, String plainPassword);
}
