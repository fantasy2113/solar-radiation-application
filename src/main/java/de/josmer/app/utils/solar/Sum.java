package de.josmer.app.utils.solar;

final class Sum {
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
