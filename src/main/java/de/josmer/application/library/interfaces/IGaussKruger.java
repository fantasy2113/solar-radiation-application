package de.josmer.application.library.interfaces;

public interface IGaussKruger {

    void transformFrom(double lon, double lat);

    double getRechtswert();

    double getHochwert();
}
