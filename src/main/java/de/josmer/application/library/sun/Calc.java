package de.josmer.application.library.sun;

import java.time.YearMonth;

class Calc {
    static final double K = 1.041;
    static final double EO = 1360.8;
    static final double EO_TAG = 1367;
    static final double RAD = Math.PI / 180.0;

    static double getRad(double deg) {
        return deg * Math.PI / 180.0;
    }

    static double getDeg(double rad) {
        return rad * 180.0 / Math.PI;
    }

    static double tan(double val) {
        return Math.tan(getRad(val));
    }

    static double cos(double val) {
        return Math.cos(getRad(val));
    }

    static double sin(double val) {
        return Math.sin(getRad(val));
    }

    static double cosh(double val) {
        return Math.cos(getRad(val));
    }

    static double sinh(double val) {
        return Math.sin(getRad(val));
    }

    static double acos(double val) {
        return Math.acos(getRad(val));
    }

    static double asin(double val) {
        return Math.asin(getRad(val));
    }

    static int getDaysInMonth(int year, int month) {
        return YearMonth.of(year, month).lengthOfMonth();
    }

    static double getKilowattToWatt(double kW) {
        return kW * 1000;
    }

    static double getWattToKilowatt(double watt) {
        return watt / 1000;
    }
}
