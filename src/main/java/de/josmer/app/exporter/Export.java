package de.josmer.app.exporter;

import java.util.Locale;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Export<R, I> {

    static final Logger LOGGER = LoggerFactory.getLogger(Export.class.getName());

    String roundToString(double value, int scale) {
        return String.format(Locale.ENGLISH, "%." + scale + "f", Precision.round(value, scale));
    }

    double getValue(double val) {
        return Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", val));
    }

    String parseDate(final int date) {
        return getDate(String.valueOf(date));
    }

    String getDate(final String date) {
        try {
            return date.substring(0, 4) + "-" + date.substring(4);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            return "undefined";
        }
    }

    protected abstract R mapToExport(double lon, double lat, I item);
}
