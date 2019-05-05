package de.josmer.app.library.interfaces;

public interface IGaussKruger {

    void transformFrom(double lon, double lat);

    double getRechtswert();

    double getHochwert();
}
