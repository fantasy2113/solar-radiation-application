package de.jos.dwdcdc.library.irradiation;

final class SolarMonth {

  private final double eHor;
  private final double eInc;
  private final int monthIndex;

  SolarMonth(int monthIndex, double eInc, double eHor) {
    this.monthIndex = monthIndex;
    this.eInc = eInc;
    this.eHor = eHor;
  }

  double getEHor() {
    return eHor;
  }

  double getEInc() {
    return eInc;
  }

  int getMonthIndex() {
    return monthIndex;
  }
}
