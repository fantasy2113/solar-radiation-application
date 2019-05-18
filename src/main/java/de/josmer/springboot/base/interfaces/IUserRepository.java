package de.josmer.springboot.base.interfaces;

import java.util.Optional;

public interface IUserRepository {
    Optional<IUser> get(Integer id);

    Optional<IUser> get(String username);

    void createUser(String username, String plainPassword);
}
