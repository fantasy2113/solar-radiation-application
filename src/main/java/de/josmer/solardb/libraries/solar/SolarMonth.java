package de.josmer.solardb.libraries.solar;

class SolarMonth {
    private final int monthIndex;
    private final double eInc;
    private final double eHor;

    SolarMonth(int monthIndex, double eInc, double eHor) {
        this.monthIndex = monthIndex;
        this.eInc = eInc;
        this.eHor = eHor;
    }

    int getMonthIndex() {
        return monthIndex;
    }

    double getEInc() {
        return eInc;
    }

    double getEHor() {
        return eHor;
    }
}
