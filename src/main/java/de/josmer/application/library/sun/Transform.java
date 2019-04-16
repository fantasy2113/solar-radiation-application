package de.josmer.application.library.sun;

import java.time.LocalDateTime;

public class Transform {

    private final double lat;
    private final double lon;
    private final double[] months;
    private final LocalDateTime startDate;
    private final double ye;
    private final double ae;

    public Transform(double lat, double lon, double[] months, LocalDateTime startDate, double ye, double ae) {
        this.lat = lat;
        this.lon = lon;
        this.months = months;
        this.startDate = startDate;
        this.ye = ye;
        this.ae = ae;
    }

    public double[] getEGlobGen() {
        double[] eGlobGenMonths = new double[12];
        for (int month = getMonthVal(); month < 12; month++) {
            double eGlobGenMonthly = 0.0;
            final TagModel tagModel = new TagModel();
            final double[] days = tagModel.getDays(getDtDays(month), months[month], lat, lon);
            for (int day = 0; day < getDaysInMonth(month); day++) {
                final LocalDateTime dtDay = getDtDay(month, day);
                final double[] eGlobalHorArr = tagModel.getHours(dtDay, days[day], lat, lon);
                for (int hour = 0; hour < 24; hour++) {
                    final Radiation radiation = new Radiation(ye, ae, lat, lon, 0.2);
                    radiation.calculateHour(eGlobalHorArr[hour], getDtHour(month, day, hour));
                    eGlobGenMonthly += radiation.getEGlobalGen();
                }
            }
            eGlobGenMonths[month] = eGlobGenMonthly;
        }
        return eGlobGenMonths;
    }

    private int getDaysInMonth(int month) {
        return Calc.getDaysInMonth(startDate.getYear(), month);
    }

    private LocalDateTime getDtDays(int month) {
        return LocalDateTime.of(startDate.getYear(), parseDate(month), 1, 0, 30);
    }

    private LocalDateTime getDtHour(int month, int day, int hour) {
        return LocalDateTime.of(startDate.getYear(), parseDate(month), parseDate(day), parseDate(hour), 30);
    }

    private LocalDateTime getDtDay(int month, int day) {
        return LocalDateTime.of(startDate.getYear(), parseDate(month), parseDate(day), 0, 30);
    }

    private int getMonthVal() {
        return startDate.getMonthValue() - 1;
    }

    private int parseDate(int value) {
        return value + 1;
    }
}
