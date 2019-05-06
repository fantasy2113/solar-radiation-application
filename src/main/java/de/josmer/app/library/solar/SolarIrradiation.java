package de.josmer.app.library.solar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class SolarIrradiation {
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
        List<SolarMonth> solarMonths = new ArrayList<>();
        for (int m = getStarMonth(); m < 12; m++) {
            solarMonths.add(calculate(m));
        }
        for (SolarMonth solarMonth : solarMonths) {
            setMonths(solarMonth);
        }
    }

    public void computeParallel() {
        IntStream.range(0, 12).parallel().mapToObj(this::calculate)
                .collect(Collectors.toList()).parallelStream().forEach(this::setMonths);
    }

    private SolarMonth calculate(final int m) {
        double sumHorMonth = 0;
        double sumIncMonth = 0;
        SolarSynthesiser solarSynthesiser = new SolarSynthesiser();
        PerezSkyDiffModel perezSkyDiffModel = new PerezSkyDiffModel(ye, ae, lat, lon, 0.2);
        double[] extractedDays = solarSynthesiser.extractDays(getDay(m), eHorMonths[m], lat, lon);
        for (int d = 0; d < getDaysInMonth(m); d++) {
            double[] extractedHours = solarSynthesiser.extractHours(getDay(m, d), extractedDays[d], lat, lon);
            adjustHours(getAdjuster(extractedDays[d], extractedHours), extractedHours);
            sumHorMonth += sumOf(extractedHours);
            for (int h = 0; h < 24; h++) {
                sumIncMonth += perezSkyDiffModel.getHourInc(extractedHours[h], getHour(m, d, h));
            }
        }
        return new SolarMonth(m, sumIncMonth, sumHorMonth);
    }

    private double sumOf(double[] eGlobalHorArr) {
        return DoubleStream.of(eGlobalHorArr).sum();
    }

    private void setMonths(SolarMonth solarMonth) {
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

    private int getDaysInMonth(int month) {
        return Utils.getDaysInMonth(dt.getYear(), month + 1);
    }

    private LocalDateTime getDay(int month) {
        return LocalDateTime.of(dt.getYear(), month + 1, 1, 0, 30);
    }

    private LocalDateTime getHour(int month, int day, int hour) {
        return LocalDateTime.of(dt.getYear(), month + 1, day + 1, hour, 30);
    }

    private LocalDateTime getDay(int month, int day) {
        return LocalDateTime.of(dt.getYear(), month + 1, day + 1, 0, 30);
    }

    private int getStarMonth() {
        return dt.getMonthValue() - 1;
    }

    public double[] getEHorMonths() {
        return computedEHorMonths;
    }

    public double[] getEIncMonths() {
        return computedEIncMonths;
    }
}
