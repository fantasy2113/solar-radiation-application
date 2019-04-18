package de.josmer.application.repositories;

import org.apache.commons.math3.util.Precision;

import java.util.Locale;

abstract class AExportRepository<R, I> {

    protected String round(double geo) {
        return String.format(Locale.ENGLISH, "%.3f", Precision.round(geo, 3));
    }

    protected double getValue(double val) {
        return Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", val));
    }

    protected String parseDate(final int date) {
        return getDate(String.valueOf(date));
    }

    protected String getDate(final String date) {
        try {
            return date.substring(0, 4) + "-" + date.substring(4);
        } catch (Exception e) {
            return "undefined";
        }
    }

    protected abstract R mapToExport(double lon, double lat, I item);
}
