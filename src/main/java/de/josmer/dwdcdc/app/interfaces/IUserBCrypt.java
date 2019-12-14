package de.josmer.dwdcdc.app.interfaces;

public interface IUserBCrypt {

    String hashPassword(String plainTextPassword);

	boolean isPassword(String plainPassword, String hashedPassword);
}
