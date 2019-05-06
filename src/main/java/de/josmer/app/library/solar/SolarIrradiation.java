package de.josmer.app.library.solar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class SolarIrradiation {

    private static final Logger LOGGER = LoggerFactory.getLogger(SolarIrradiation.class.getName());
    private final double lat;
    private final double lon;
    private final double[] eGlobHorMonthly;
    private final LocalDateTime dt;
    private final double ye;
    private final double ae;
    private final double[] eGlobHorMonthlySynth;

    public SolarIrradiation(double lat, double lon, double[] eGlobHorMonthly, LocalDateTime dt, double ye, double ae) {
        this.lat = lat;
        this.lon = lon;
        this.eGlobHorMonthly = eGlobHorMonthly;
        this.dt = dt;
        this.ye = ye;
        this.ae = ae;
        this.eGlobHorMonthlySynth = new double[12];
    }

    public double[] getEGlobGenMonthly() {
        double[] eGlobGenMonths = new double[12];
        try {
            SolarSynthesiser solarSynthesiser = new SolarSynthesiser();
            PerezSkyDiffModel perezSkyDiffModel = new PerezSkyDiffModel(ye, ae, lat, lon, 0.2);
            for (int month = getMonthVal(); month < 12; month++) {
                double eGlobHorSumSynth = 0;
                double eGlobGenMonthly = 0.0;
                final double[] days = solarSynthesiser.extractDays(getDtDays(month), eGlobHorMonthly[month], lat, lon);
                for (int day = 0; day < getDaysInMonth(month); day++) {
                    final LocalDateTime dtDay = getDtDay(month, day);
                    final double[] eGlobalHorArr = solarSynthesiser.extractHours(dtDay, days[day], lat, lon);
                    adjustHours(((days[day] / getSum(eGlobalHorArr)) * 100) / 100, eGlobalHorArr);
                    eGlobHorSumSynth += getSum(eGlobalHorArr);
                    for (int hour = 0; hour < 24; hour++) {
                        eGlobGenMonthly += perezSkyDiffModel.getIncValue(eGlobalHorArr[hour], getDtHour(month, day, hour));
                    }
                }
                eGlobHorMonthlySynth[month] = eGlobHorSumSynth;
                eGlobGenMonths[month] = eGlobGenMonthly;
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return eGlobGenMonths;
    }

    private CompletableFuture<MonthlyValue> computeMonthAsync(final int m) {
        return CompletableFuture.supplyAsync(() -> calculateMonth(m));
    }

    private MonthlyValue calculateMonth(final int m) {
        double sumHorMonth = 0;
        double sumIncMonth = 0;
        SolarSynthesiser solarSynthesiser = new SolarSynthesiser();
        PerezSkyDiffModel perezSkyDiffModel = new PerezSkyDiffModel(ye, ae, lat, lon, 0.2);
        double[] extractedDays = solarSynthesiser.extractDays(getDtDays(m), eGlobHorMonthly[m], lat, lon);
        for (int d = 0; d < getDaysInMonth(m); d++) {
            LocalDateTime dtDay = getDtDay(m, d);
            double[] extractedHours = solarSynthesiser.extractHours(dtDay, extractedDays[d], lat, lon);
            adjustHours(((extractedDays[d] / getSum(extractedHours)) * 100) / 100, extractedHours);
            sumHorMonth += getSum(extractedHours);
            for (int h = 0; h < 24; h++) {
                sumIncMonth += perezSkyDiffModel.getIncValue(extractedHours[h], getDtHour(m, d, h));
            }
        }
        return new MonthlyValue(m, sumIncMonth, sumHorMonth);
    }

    public void compute() {

        List<MonthlyValue> months = IntStream.range(0, 12)
                .parallel()
                .mapToObj(this::computeMonthAsync)
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        //months.parallelStream().forEach();
    }

    private double getSum(double[] eGlobalHorArr) {
        return DoubleStream.of(eGlobalHorArr).sum();
    }

    private int getDaysInMonth(int month) {
        return Utils.getDaysInMonth(dt.getYear(), month + 1);
    }

    private LocalDateTime getDtDays(int month) {
        return LocalDateTime.of(dt.getYear(), month + 1, 1, 0, 30);
    }

    private LocalDateTime getDtHour(int month, int day, int hour) {
        return LocalDateTime.of(dt.getYear(), month + 1, day + 1, hour, 30);
    }

    private LocalDateTime getDtDay(int month, int day) {
        return LocalDateTime.of(dt.getYear(), month + 1, day + 1, 0, 30);
    }

    private int getMonthVal() {
        return dt.getMonthValue() - 1;
    }

    public double[] getEGlobHorMonthlySynth() {
        return eGlobHorMonthlySynth;
    }

    private void adjustHours(double multi, double[] dailyHours) {
        for (int h = 0; h < 24; h++) {
            dailyHours[h] = dailyHours[h] * multi;
        }
    }
}
