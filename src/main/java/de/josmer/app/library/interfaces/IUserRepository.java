package de.josmer.app.library.interfaces;

import de.josmer.app.model.entities.User;

import java.util.Optional;

public interface IUserRepository {

    Optional<User> get(int id);

    Optional<User> get(String login);

    void saveUser(String login, String plainPassword);
}
