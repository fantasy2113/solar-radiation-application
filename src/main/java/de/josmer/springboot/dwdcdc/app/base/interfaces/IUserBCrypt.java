package de.josmer.springboot.dwdcdc.app.base.interfaces;

public interface IUserBCrypt {
    String hashPassword(String plainTextPassword);

    boolean isPassword(String plainPassword, String hashedPassword);
}
