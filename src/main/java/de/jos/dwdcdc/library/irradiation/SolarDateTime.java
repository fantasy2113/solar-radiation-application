package de.jos.dwdcdc.library.irradiation;

import java.time.LocalDateTime;

/**
 * Beware: Not Thread-Safety
 */
final class SolarDateTime {

  private final int dayInMonth;
  private final int hour;
  private final int minute;
  private final int month;
  private final int second;
  private final int year;

  SolarDateTime(int year, int month, int dayInMonth, int hour) {
    this.year = year;
    this.month = month;
    this.dayInMonth = dayInMonth;
    this.hour = hour;
    this.minute = 30;
    this.second = 0;
  }

  int getDayInMonth() {
    return dayInMonth;
  }

  int getDayOfMonth() {
    return LocalDateTime.of(year, month, dayInMonth, hour, minute).getDayOfMonth();
  }

  int getDayOfYear() {
    return LocalDateTime.of(year, month, dayInMonth, hour, minute).getDayOfYear();
  }

  int getHour() {
    return hour;
  }

  int getMinute() {
    return minute;
  }

  int getMonth() {
    return month;
  }

  int getSecond() {
    return second;
  }

  int getYear() {
    return year;
  }
}
