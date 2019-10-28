package de.josmer.dwdcdc.app.security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import de.josmer.dwdcdc.app.interfaces.IUserBCrypt;

@Component
public final class UserBCrypt implements IUserBCrypt {

	@Override
	public String hashPassword(String plainTextPassword) {
		return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
	}

	@Override
	public boolean isPassword(final String plainPassword, final String hashedPassword) {
		if (plainPassword == null || hashedPassword == null || hashedPassword.length() != 60) {
			return false;
		}
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}

}
