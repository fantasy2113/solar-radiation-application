package de.josmer.springboot.base.irradiation;

import java.time.LocalDateTime;

final class SolarDateTime {
    private final int year;
    private final int month;
    private final int dayInMonth;
    private final int hour;
    private final int minute;
    private final int second;

    SolarDateTime(int year, int month, int dayInMonth, int hour) {
        this.year = year;
        this.month = month;
        this.dayInMonth = dayInMonth;
        this.hour = hour;
        this.minute = 30;
        this.second = 0;
    }

    int getYear() {
        return year;
    }

    int getMonth() {
        return month;
    }

    int getDayInMonth() {
        return dayInMonth;
    }

    int getHour() {
        return hour;
    }

    int getMinute() {
        return minute;
    }

    int getSecond() {
        return second;
    }

    int getDayOfYear() {
        return LocalDateTime.of(year, month, dayInMonth, hour, minute).getDayOfYear();
    }

    int getDayOfMonth() {
        return LocalDateTime.of(year, month, dayInMonth, hour, minute).getDayOfMonth();
    }
}
