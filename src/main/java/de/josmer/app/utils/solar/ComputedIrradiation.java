package de.josmer.app.utils.solar;

public final class ComputedIrradiation {
    private final double[] computedEHorMonths;
    private final double[] computedEIncMonths;


    ComputedIrradiation(double[] computedEHorMonths, double[] computedEIncMonths) {
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
