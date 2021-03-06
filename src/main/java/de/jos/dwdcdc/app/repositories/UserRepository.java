package de.jos.dwdcdc.app.repositories;

import de.jos.dwdcdc.app.entities.User;
import de.jos.dwdcdc.app.interfaces.IUserBCrypt;
import de.jos.dwdcdc.app.interfaces.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public final class UserRepository implements IUserRepository {

  private final IUserBCrypt userBCrypt;
  private final UserRepositoryCrud userRepositoryCrud;

  @Autowired
  public UserRepository(UserRepositoryCrud userRepositoryCrud, IUserBCrypt userBCrypt) {
    this.userRepositoryCrud = userRepositoryCrud;
    this.userBCrypt = userBCrypt;
  }

  @Override
  public void activateUser(User user) {
    user.setIsActive(true);
    userRepositoryCrud.save(user);
  }

  @Override
  public void createUser(final String username, final String plainPassword) {
    if (userRepositoryCrud.findByUsername(username).isEmpty()) {
      userRepositoryCrud.save(initUser(username, plainPassword));
    }
  }

  @Override
  public void deactivateUser(User user) {
    user.setIsActive(false);
    userRepositoryCrud.save(user);
  }

  @Override
  public Optional<User> get(final Long id) {
    Optional<User> optionalUser = userRepositoryCrud.findById(id);
    if (optionalUser.isPresent() && isValid(optionalUser.get())) {
      return optionalUser;
    }
    return Optional.empty();
  }

  @Override
  public Optional<User> get(final String username) {
    Optional<User> optionalUser = userRepositoryCrud.findByUsername(username);
    if (optionalUser.isPresent() && isValid(optionalUser.get())) {
      return optionalUser;
    }
    return Optional.empty();
  }

  private User initUser(final String username, final String plainTextPassword) {
    LocalDateTime now = LocalDateTime.now();
    User user = new User();
    user.setPassword(userBCrypt.hashPassword(plainTextPassword));
    user.setUsername(username);
    user.setCreated(Timestamp.valueOf(now));
    user.setModified(Timestamp.valueOf(now));
    user.setLastLogin(Timestamp.valueOf(now));
    user.setIsActive(true);
    return user;
  }

  private boolean isValid(User user) {
    return user.isActive();
  }

  @Override
  public void updateLastLogin(User user) {
    user.setLastLogin(Timestamp.valueOf(LocalDateTime.now()));
    userRepositoryCrud.save(user);
  }
}
