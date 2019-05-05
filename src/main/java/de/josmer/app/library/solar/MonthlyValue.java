package de.josmer.app.library.solar;

class MonthlyValue {
    private final int month;
    private final double value;

    MonthlyValue(int month, double value) {
        this.month = month;
        this.value = value;
    }

    int getMonth() {
        return month;
    }

    double getValue() {
        return value;
    }
}
