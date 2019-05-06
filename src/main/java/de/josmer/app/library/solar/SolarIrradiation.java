package de.josmer.app.library.solar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final double[] eGlobIncMonthlySynth;
    private final double[] eGlobHorMonthlySynth;

    public SolarIrradiation(double lat, double lon, double[] eGlobHorMonthly, LocalDateTime dt, double ye, double ae) {
        this.lat = lat;
        this.lon = lon;
        this.dt = dt;
        this.ye = ye;
        this.ae = ae;
        this.eGlobHorMonthlySynth = new double[12];
        this.eGlobIncMonthlySynth = new double[12];
        this.eGlobHorMonthly = eGlobHorMonthly;
    }


    public void computeSlow() {
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
                    adjustHours(((days[day] / computeSumOf(eGlobalHorArr)) * 100) / 100, eGlobalHorArr);
                    eGlobHorSumSynth += computeSumOf(eGlobalHorArr);
                    for (int hour = 0; hour < 24; hour++) {
                        eGlobGenMonthly += perezSkyDiffModel.getSolarEnergyInc(eGlobalHorArr[hour], getDtHour(month, day, hour));
                    }
                }
                this.eGlobHorMonthlySynth[month] = eGlobHorSumSynth;
                this.eGlobIncMonthlySynth[month] = eGlobGenMonthly;
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }


    public void computeFast() {
        try {
            IntStream.range(0, 12).parallel().mapToObj(this::computeAsync).map(CompletableFuture::join)
                    .collect(Collectors.toList()).parallelStream().forEach(this::setSolarEnergy);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private CompletableFuture<MonthlyValue> computeAsync(final int m) {
        return CompletableFuture.supplyAsync(() -> computeMonth(m));
    }

    private MonthlyValue computeMonth(final int m) {
        final SolarSynthesiser solarSynthesiser = new SolarSynthesiser();
        final PerezSkyDiffModel perezSkyDiffModel = new PerezSkyDiffModel(ye, ae, lat, lon, 0.2);
        final double[] extractedDays = solarSynthesiser.extractDays(getDtDays(m), eGlobHorMonthly[m], lat, lon);
        final List<Double> sumHor = new ArrayList<>();
        final List<Double> sumInc = new ArrayList<>();
        IntStream.range(0, getDaysInMonth(m)).parallel().forEach(d -> {
            final LocalDateTime dtDay = getDtDay(m, d);
            final double[] extractedHours = solarSynthesiser.extractHours(dtDay, extractedDays[d], lat, lon);
            adjustHours(getAdjuster(extractedDays[d], extractedHours), extractedHours);
            sumHor.add(computeSumOf(extractedHours));
            IntStream.range(0, 24).parallel().forEach(h ->
                    sumInc.add(perezSkyDiffModel.getSolarEnergyInc(extractedHours[h], getDtHour(m, d, h)))
            );
        });
        return new MonthlyValue(m, computeSumOf(sumInc), computeSumOf(sumHor));
    }

    private void setSolarEnergy(MonthlyValue m) {
        this.eGlobIncMonthlySynth[m.getMonth()] = m.getEnergy();
        this.eGlobHorMonthlySynth[m.getMonth()] = m.getEnergySynth();
    }

    private void adjustHours(double multi, double[] dailyHours) {
        IntStream.range(0, 24)
                .parallel()
                .forEach(h -> dailyHours[h] = dailyHours[h] * multi);
    }

    private double getAdjuster(double extractedDay, double[] extractedHours) {
        return ((extractedDay / computeSumOf(extractedHours)) * 100) / 100;
    }

    private double computeSumOf(double[] eGlobalHorArr) {
        return DoubleStream.of(eGlobalHorArr).parallel().sum();
    }

    private double computeSumOf(List<Double> values) {
        return values.stream().parallel().flatMapToDouble(DoubleStream::of).sum();
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

    public double[] getEGlobIncMonthlySynth() {
        return eGlobIncMonthlySynth;
    }
}
