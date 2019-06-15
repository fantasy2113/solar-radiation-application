package de.josmer.dwdcdc.utils.solar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public final class SolarIrradiation {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolarIrradiation.class.getName());
    private final int limit;
    private final double lat;
    private final double lon;
    private final double ye;
    private final double ae;
    private final int year;
    private final double[] eHorMonths;
    private final double[] computedEHorMonths;
    private final double[] computedEIncMonths;

    public SolarIrradiation(double lat, double lon, double[] eHorMonths, int year, double ye, double ae) {
        this.lat = lat;
        this.lon = lon;
        this.year = year;
        this.ye = ye;
        this.ae = ae;
        this.eHorMonths = eHorMonths;
        this.computedEHorMonths = new double[12];
        this.computedEIncMonths = new double[12];
        this.limit = this.eHorMonths.length;
    }

    public void compute() {
        if (isLimit()) {
            monthInfo();
            return;
        }

        try {
            IntStream.range(0, limit).sequential().mapToObj(this::computeMonth).collect(Collectors.toList()).forEach(this::insertComputedMonth);
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
    }

    public void computeParallel() {
        if (isLimit()) {
            monthInfo();
            return;
        }

        try {
            IntStream.range(0, limit).parallel().mapToObj(this::computeMonth).collect(Collectors.toList()).forEach(this::insertComputedMonth);
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
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
        return Utils.getDaysInMonth(year, monthIndex + 1);
    }

    private SolarDateTime getDay(int monthIndex) {
        return new SolarDateTime(year, monthIndex + 1, 1, 0);
    }

    private SolarDateTime getHour(int monthIndex, int dayIndex, int hourIndex) {
        return new SolarDateTime(year, monthIndex + 1, dayIndex + 1, hourIndex);
    }

    private SolarDateTime getDay(int monthIndex, int dayIndex) {
        return new SolarDateTime(year, monthIndex + 1, dayIndex + 1, 0);
    }

    public ComputedYear getComputedYear() {
        return new ComputedYear(computedEHorMonths, computedEIncMonths);
    }

    private void monthInfo() {
        LOGGER.info("max. 12 Months");
    }

    private boolean isLimit() {
        return limit > 12;
    }
}
