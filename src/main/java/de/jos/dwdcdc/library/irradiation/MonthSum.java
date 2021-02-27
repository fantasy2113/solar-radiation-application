package de.jos.dwdcdc.library.irradiation;

/**
 * Beware: Not Thread-Safety
 */
final class MonthSum {

  private double value;

  MonthSum() {
    this.value = 0;
  }

  void add(double value) {
    this.value += value;
  }

  double get() {
    return value;
  }
}
