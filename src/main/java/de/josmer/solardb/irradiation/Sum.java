package de.josmer.solardb.irradiation;

class Sum {
    private double value;

    Sum() {
        this.value = 0;
    }

    void add(double value) {
        this.value += value;
    }

    double get() {
        return value;
    }
}
