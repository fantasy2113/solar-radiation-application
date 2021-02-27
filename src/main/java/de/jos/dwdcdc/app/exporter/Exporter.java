package de.jos.dwdcdc.app.exporter;

import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

abstract class Exporter<TOut, TInput> {

  static final Logger LOGGER = LoggerFactory.getLogger(Exporter.class.getName());

  private String getDate(final String date) {
    try {
      return date.substring(0, 4) + "-" + date.substring(4);
    } catch (Exception e) {
      LOGGER.info(e.toString());
      return "undefined";
    }
  }

  double getValue(double val) {
    return Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", val));
  }

  protected abstract TOut mapToExport(double lon, double lat, TInput item);

  String parseDate(final int date) {
    return getDate(String.valueOf(date));
  }

  String roundToString(double value, int scale) {
    return String.format(Locale.ENGLISH, "%." + scale + "f", Precision.round(value, scale));
  }
}
