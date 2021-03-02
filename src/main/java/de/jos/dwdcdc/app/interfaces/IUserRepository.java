package de.jos.dwdcdc.app.interfaces;

import de.jos.dwdcdc.app.entities.User;

import java.util.Optional;

public interface IUserRepository {

  void activateUser(User user);

  void createUser(String username, String plainPassword);

  void deactivateUser(User user);

  Optional<User> get(Long id);

  Optional<User> get(String username);

  void updateLastLogin(User user);
}
