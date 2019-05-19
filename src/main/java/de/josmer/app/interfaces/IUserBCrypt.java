package de.josmer.app.interfaces;

public interface IUserBCrypt {
    String hashPassword(String plainTextPassword);

    boolean isPassword(String plainPassword, String hashedPassword);
}
