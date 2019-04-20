package de.josmer.app.library.sun;

import java.time.LocalDateTime;
import java.util.stream.DoubleStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalcRadiation {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalcRadiation.class.getName());
    private final double lat;
    private final double lon;
    private final double[] eGlobHorMonthly;
    private final LocalDateTime dt;
    private final double ye;
    private final double ae;
    private double[] eGlobHorMonthlySynth;

    public CalcRadiation(double lat, double lon, double[] eGlobHorMonthly, LocalDateTime dt, double ye, double ae) {
        this.lat = lat;
        this.lon = lon;
        this.eGlobHorMonthly = eGlobHorMonthly;
        this.dt = dt;
        this.ye = ye;
        this.ae = ae;
        this.eGlobHorMonthlySynth = new double[12];
    }

    public double[] getEGlobGenMonthly() {
        Converter.initFTabelle();
        double[] eGlobGenMonths = new double[12];
        try {
            TagModel tagModel = new TagModel();
            for (int month = getMonthVal(); month < 12; month++) {
                double eGlobHorSumSynth = 0;
                double eGlobGenMonthly = 0.0;
                final double[] days = tagModel.getDays(getDtDays(month), eGlobHorMonthly[month], lat, lon);
                for (int day = 0; day < getDaysInMonth(month); day++) {
                    final LocalDateTime dtDay = getDtDay(month, day);
                    final double[] eGlobalHorArr = tagModel.getHours(dtDay, days[day], lat, lon);
                    eGlobHorSumSynth += DoubleStream.of(eGlobalHorArr).sum();
                    for (int hour = 0; hour < 24; hour++) {
                        final Converter radiation = new Converter(ye, ae, lat, lon, 0.2);
                        radiation.calculateHour(eGlobalHorArr[hour], getDtHour(month, day, hour));
                        eGlobGenMonthly += radiation.getEGlobalGen();
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

    private int getDaysInMonth(int month) {
        return CalcUtils.getDaysInMonth(dt.getYear(), month + 1);
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
}
