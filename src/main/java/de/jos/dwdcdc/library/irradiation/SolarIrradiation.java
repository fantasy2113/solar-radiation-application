package de.jos.dwdcdc.library.irradiation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * Beware: Not Thread-Safety
 */
public final class SolarIrradiation {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolarIrradiation.class.getName());
    private final double ae;
    private final double[] computedEHorMonths;
    private final double[] computedEIncMonths;
    private final double[] eHorMonths;
    private final double lat;
    private final int limit;
    private final double lon;
    private final double ye;
    private final int year;

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

    private void adjustHours(double adjuster, double[] extractedHours) {
        for (int h = 0; h < 24; h++) {
            extractedHours[h] = extractedHours[h] * adjuster;
        }
    }

    public void compute() {
        if (isLimit()) {
            monthInfo();
            return;
        }
        Instant start = Instant.now();
        try {
            IntStream.range(0, limit).mapToObj(this::computeMonth).collect(Collectors.toList())
                    .forEach(this::insertComputedMonth);
        } catch (Exception e) {
            LOGGER.info(e.toString());
        } finally {
            LOGGER.info("Sequential computing time=" + getComputingTime(start));
        }
    }

    private SolarMonth computeMonth(final int monthIndex) {
        MonthSum monthSumOfHor = new MonthSum();
        MonthSum monthSumOfInc = new MonthSum();
        SolarSynthesiser solarSynthesiser = new SolarSynthesiser();
        PerezSkyDiffModel perezSkyDiffModel = new PerezSkyDiffModel(ye, ae, lat, lon, 0.2);
        double[] extractedDays = solarSynthesiser.extractDays(getDay(monthIndex), eHorMonths[monthIndex], lat, lon);
        for (int dayIndex = 0; dayIndex < getDaysInMonth(monthIndex); dayIndex++) {
            double[] extractedHours = solarSynthesiser.extractHours(getDay(monthIndex, dayIndex),
                    extractedDays[dayIndex], lat, lon);
            adjustHours(getAdjuster(extractedDays[dayIndex], extractedHours), extractedHours);
            monthSumOfHor.add(sumOf(extractedHours));
            for (int hourIndex = 0; hourIndex < 24; hourIndex++) {
                perezSkyDiffModel.compute(extractedHours[hourIndex], getHour(monthIndex, dayIndex, hourIndex));
                monthSumOfInc.add(perezSkyDiffModel.getHourlyEnergy());
            }
        }
        return new SolarMonth(monthIndex, monthSumOfInc.get(), monthSumOfHor.get());
    }

    public void computeParallel() {
        if (isLimit()) {
            monthInfo();
            return;
        }
        Instant start = Instant.now();
        try {
            IntStream.range(0, limit).parallel().mapToObj(this::computeMonth).collect(Collectors.toList())
                    .forEach(this::insertComputedMonth);
        } catch (Exception e) {
            LOGGER.info(e.toString());
        } finally {
            LOGGER.info("Parallel computing time=" + getComputingTime(start));
        }
    }

    private double getAdjuster(double extractedDay, double[] extractedHours) {
        return ((extractedDay / sumOf(extractedHours)) * 100) / 100;
    }

    public ComputedYear getComputedYear() {
        return new ComputedYear(computedEHorMonths, computedEIncMonths);
    }

    private String getComputingTime(Instant start) {
        return Duration.between(start, Instant.now()).toString();
    }

    private SolarDateTime getDay(int monthIndex) {
        return new SolarDateTime(year, monthIndex + 1, 1, 0);
    }

    private SolarDateTime getDay(int monthIndex, int dayIndex) {
        return new SolarDateTime(year, monthIndex + 1, dayIndex + 1, 0);
    }

    private int getDaysInMonth(int monthIndex) {
        return Utils.getDaysInMonth(year, monthIndex + 1);
    }

    private SolarDateTime getHour(int monthIndex, int dayIndex, int hourIndex) {
        return new SolarDateTime(year, monthIndex + 1, dayIndex + 1, hourIndex);
    }

    private void insertComputedMonth(SolarMonth solarMonth) {
        this.computedEHorMonths[solarMonth.getMonthIndex()] = solarMonth.getEHor();
        this.computedEIncMonths[solarMonth.getMonthIndex()] = solarMonth.getEInc();
    }

    private boolean isLimit() {
        return limit > 12;
    }

    private void monthInfo() {
        LOGGER.info("max. 12 Months");
    }

    private double sumOf(double[] values) {
        return DoubleStream.of(values).sum();
    }
}
