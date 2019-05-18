package de.josmer.springboot.dwdcdc.app.irradiation;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public final class SolarIrradiation {
    private final double lat;
    private final double lon;
    private final double ye;
    private final double ae;
    private final LocalDateTime dt;
    private final double[] eHorMonths;
    private final double[] computedEHorMonths;
    private final double[] computedEIncMonths;

    public SolarIrradiation(double lat, double lon, double[] eHorMonths, LocalDateTime dt, double ye, double ae) {
        this.lat = lat;
        this.lon = lon;
        this.dt = dt;
        this.ye = ye;
        this.ae = ae;
        this.eHorMonths = eHorMonths;
        this.computedEHorMonths = new double[12];
        this.computedEIncMonths = new double[12];
    }

    public void compute() {
        for (int monthIndex = getStarMonth(); monthIndex < 12; monthIndex++) {
            insertComputedMonth(computeMonth(monthIndex));
        }
    }

    public void computeParallel() {
        IntStream.range(0, 12).parallel().mapToObj(this::computeMonth)
                .collect(Collectors.toList()).forEach(this::insertComputedMonth);
    }

    private SolarMonth computeMonth(final int monthIndex) {
        Sum sumOfHor = new Sum();
        Sum sumOfInc = new Sum();
        SolarSynthesiser solarSynthesiser = new SolarSynthesiser();
        PerezSkyDiffModel perezSkyDiffModel = new PerezSkyDiffModel(ye, ae, lat, lon, 0.2);
        double[] extractedDays = solarSynthesiser.extractDays(getDay(monthIndex), eHorMonths[monthIndex], lat, lon);
        for (int dayIndex = 0; dayIndex < getDaysInMonth(monthIndex); dayIndex++) {
            double[] extractedHours = solarSynthesiser.extractHours(getDay(monthIndex, dayIndex), extractedDays[dayIndex], lat, lon);
            adjustHours(getAdjuster(extractedDays[dayIndex], extractedHours), extractedHours);
            sumOfHor.add(sumOf(extractedHours));
            for (int hourIndex = 0; hourIndex < 24; hourIndex++) {
                perezSkyDiffModel.compute(extractedHours[hourIndex], getHour(monthIndex, dayIndex, hourIndex));
                sumOfInc.add(perezSkyDiffModel.getHourlyEnergy());
            }
        }
        return new SolarMonth(monthIndex, sumOfInc.get(), sumOfHor.get());
    }

    private double sumOf(double[] values) {
        return DoubleStream.of(values).sum();
    }

    private void insertComputedMonth(SolarMonth solarMonth) {
        this.computedEHorMonths[solarMonth.getMonthIndex()] = solarMonth.getEHor();
        this.computedEIncMonths[solarMonth.getMonthIndex()] = solarMonth.getEInc();
    }

    private double getAdjuster(double extractedDay, double[] extractedHours) {
        return ((extractedDay / sumOf(extractedHours)) * 100) / 100;
    }

    private void adjustHours(double adjuster, double[] extractedHours) {
        for (int h = 0; h < 24; h++) {
            extractedHours[h] = extractedHours[h] * adjuster;
        }
    }

    private int getDaysInMonth(int monthIndex) {
        return Utils.getDaysInMonth(dt.getYear(), monthIndex + 1);
    }

    private SolarDateTime getDay(int monthIndex) {
        return new SolarDateTime(dt.getYear(), monthIndex + 1, 1, 0);
    }

    private SolarDateTime getHour(int monthIndex, int dayIndex, int hourIndex) {
        return new SolarDateTime(dt.getYear(), monthIndex + 1, dayIndex + 1, hourIndex);
    }

    private SolarDateTime getDay(int monthIndex, int dayIndex) {
        return new SolarDateTime(dt.getYear(), monthIndex + 1, dayIndex + 1, 0);
    }

    private int getStarMonth() {
        return dt.getMonthValue() - 1;
    }

    public ComputedIrradiation getComputedIrradiation() {
        return new ComputedIrradiation(computedEHorMonths, computedEIncMonths);
    }
}
