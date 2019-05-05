package de.josmer.app.library.solar;

class MonthlyValue {
    private final int month;
    private final double energy;
    private final double energySynth;

    MonthlyValue(int month, double energy, double energySynth) {
        this.month = month;
        this.energy = energy;
        this.energySynth = energySynth;
    }

    public int getMonth() {
        return month;
    }

    public double getEnergy() {
        return energy;
    }

    public double getEnergySynth() {
        return energySynth;
    }
}
