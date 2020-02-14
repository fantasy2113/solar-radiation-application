package de.jos.dwdcdc.library.irradiation;

public final class ComputedYear {

    private final double[] computedEHorMonths;
    private final double[] computedEIncMonths;

    ComputedYear(double[] computedEHorMonths, double[] computedEIncMonths) {
        this.computedEHorMonths = computedEHorMonths;
        this.computedEIncMonths = computedEIncMonths;
    }

    public double getMonthHor(int monthIndex) {
        return isMonthIndex(monthIndex) ? 0 : computedEHorMonths[monthIndex];
    }

    public double getMonthInc(int monthIndex) {
        return isMonthIndex(monthIndex) ? 0 : computedEIncMonths[monthIndex];
    }

    private boolean isMonthIndex(int monthIndex) {
        return monthIndex < 0 || monthIndex > 11;
    }
}
