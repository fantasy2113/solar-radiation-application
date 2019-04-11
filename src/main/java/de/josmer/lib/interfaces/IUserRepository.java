package de.josmer.lib.interfaces;

import de.josmer.entities.User;

import java.util.Optional;

public interface IUserRepository {

    Optional<User> get(int id);

    Optional<User> get(String login);
}
