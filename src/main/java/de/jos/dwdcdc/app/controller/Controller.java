package de.jos.dwdcdc.app.controller;

import de.jos.dwdcdc.app.entities.User;
import de.jos.dwdcdc.app.interfaces.IJwtToken;
import de.jos.dwdcdc.app.interfaces.IUserBCrypt;
import de.jos.dwdcdc.app.interfaces.IUserRepository;
import de.jos.dwdcdc.library.enums.SolRadTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

abstract class Controller {

  protected static final Logger LOGGER = LoggerFactory.getLogger(AppController.class.getName());
  static final Long TTL_MILLIS = TimeUnit.DAYS.toMillis(5);
  final IJwtToken jwtToken;
  final IUserRepository userRep;
  IUserBCrypt userBCrypt;

  Controller(IUserRepository userRep, IJwtToken jwtToken, IUserBCrypt userBCrypt) {
    this.userRep = userRep;
    this.jwtToken = jwtToken;
    this.userBCrypt = userBCrypt;
  }

  final void executeTask(Runnable task) {
    Executors.newSingleThreadExecutor().execute(task);
  }

  final SolRadTypes getSolRadTypes(String type) {
    return SolRadTypes.valueOf(type.toUpperCase(Locale.ENGLISH));
  }

  final boolean isAccess(final String token) {
    try {
      Optional<User> userOptional = userRep.get(Long.valueOf(jwtToken.decode(token).getId()));
      return userOptional.isPresent() && userOptional.get().isActive();
    } catch (Exception e) {
      LOGGER.info(e.toString());
    }
    return false;
  }
}
