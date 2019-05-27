package de.josmer.dwdcdc.utils.solar;

public final class ComputedYear {
    private final double[] computedEHorMonths;
    private final double[] computedEIncMonths;


    ComputedYear(double[] computedEHorMonths, double[] computedEIncMonths) {
        this.computedEHorMonths = computedEHorMonths;
        this.computedEIncMonths = computedEIncMonths;
    }

    public double getMonthInc(int monthIndex) {
        if (isMonthIndex(monthIndex)) {
            return 0;
        }
        return computedEIncMonths[monthIndex];

    }

    public double getMonthHor(int monthIndex) {
        if (isMonthIndex(monthIndex)) {
            return 0;
        }
        return computedEHorMonths[monthIndex];
    }

    private boolean isMonthIndex(int monthIndex) {
        return monthIndex < 0 || monthIndex > 11;
    }
}