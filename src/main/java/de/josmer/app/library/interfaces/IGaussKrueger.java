package de.josmer.app.library.interfaces;

public interface IGaussKrueger {

    void transformFrom(double lon, double lat);

    double getRechtswert();

    double getHochwert();
}
