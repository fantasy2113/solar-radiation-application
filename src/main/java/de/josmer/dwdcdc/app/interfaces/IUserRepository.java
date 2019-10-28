package de.josmer.dwdcdc.app.interfaces;

import java.util.Optional;

import de.josmer.dwdcdc.app.entities.User;

public interface IUserRepository {

	void activateUser(User user);

	void createUser(String username, String plainPassword);

	void deactivateUser(User user);

	Optional<User> get(Integer id);

	Optional<User> get(String username);

	void updateLastLogin(User user);
}
