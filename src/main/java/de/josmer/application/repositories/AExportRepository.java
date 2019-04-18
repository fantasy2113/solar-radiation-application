package de.josmer.application.repositories;

import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

abstract class AExportRepository<R, I> {
    static final Logger LOGGER = LoggerFactory.getLogger(AExportRepository.class.getName());

    String round(double geo) {
        return String.format(Locale.ENGLISH, "%.3f", Precision.round(geo, 3));
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
