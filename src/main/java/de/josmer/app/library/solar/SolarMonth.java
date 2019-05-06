package de.josmer.app.library.solar;

class SolarMonth {
    private final int monthIndex;
    private final double eInc;
    private final double eHor;

    SolarMonth(int monthIndex, double eInc, double eHor) {
        this.monthIndex = monthIndex;
        this.eInc = eInc;
        this.eHor = eHor;
    }

    public int getMonthIndex() {
        return monthIndex;
    }

    public double getEInc() {
        return eInc;
    }

    public double getEHor() {
        return eHor;
    }
}
