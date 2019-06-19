package de.josmer.dwdcdc.app.base.interfaces;

public interface IUserBCrypt {
    String hashPassword(String plainTextPassword);

    boolean isPassword(String plainPassword, String hashedPassword);
}
