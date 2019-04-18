package de.josmer.application.library.interfaces;

public interface IGaussKrueger {
    void convertFrom(double lon, double lat);

    double getRechtswert();

    double getHochwert();
}
