package de.jos.dwdcdc.library.geotrans;

import de.jos.dwdcdc.share.IGaussKruger;

import java.util.OptionalInt;

public class GkConverter {

  private final IGaussKruger gaussKruger;

  public GkConverter(IGaussKruger gaussKruger) {
    this.gaussKruger = gaussKruger;
    this.gaussKruger.compute();
  }

  private int getGkValues(final double gk) {
    Integer gkMin = (int) gk;
    while (isIncrement(gkMin)) {
      gkMin--;
    }
    return gkMin;
  }

  public int getHochwert() {
    return getGkValues(gaussKruger.getHochwert());
  }

  public OptionalInt getRechtswert() {
    switch (gaussKruger.getMeridianstreifen()) {
      case 5:
        return OptionalInt.of(getGkValues(gaussKruger.getRechtswert() - 1600000));
      case 4:
        return OptionalInt.of(getGkValues(gaussKruger.getRechtswert() - 800000));
      case 3:
        return OptionalInt.of(getGkValues(gaussKruger.getRechtswert()));
      case 2:
        return OptionalInt.of(getGkValues(gaussKruger.getRechtswert() + 800000));
      default:
        return OptionalInt.empty();
    }
  }

  private boolean isIncrement(final Integer gkMin) {
    return !gkMin.toString().endsWith("500");
  }
}
