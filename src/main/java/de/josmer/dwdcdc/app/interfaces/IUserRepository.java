package de.josmer.dwdcdc.app.interfaces;

import java.util.Optional;

import de.josmer.dwdcdc.app.entities.User;

public interface IUserRepository {

	Optional<User> get(Integer id);

	Optional<User> get(String username);

	void createUser(String username, String plainPassword);

	void updateLastLogin(User user);

	void deactivateUser(User user);

	void activateUser(User user);
}
