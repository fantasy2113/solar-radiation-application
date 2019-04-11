package de.josmer.app.repositories;

import de.josmer.app.entities.Export;
import de.josmer.app.entities.Radiation;
import de.josmer.app.lib.interfaces.IExportRepository;
import org.apache.commons.math3.util.Precision;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@Component
public class ExportRepository implements IExportRepository {

    @Override
    public List<Export> getAll(final List<Radiation> radiations, final double lon, final double lat) {
        List<Export> exports = new LinkedList<>();
        for (Radiation radiation : radiations) {
            exports.add(mapToExport(lon, lat, radiation));
        }
        return exports;
    }

    @Override
    public List<String> getHeaders() {
        return List.of("Datum", "Lat", "Lon", "Art", "Wert", "Einheit", "Dim", "Quelle");
    }

    @Override
    public String getProps() {
        return "date, lat, lon, type, value, unit, dim, source, ";
    }

    private Export mapToExport(double lon, double lat, Radiation radiation) {
        Export export = new Export();
        export.setDate(parseDate(radiation.getRadiationDate()));
        export.setLat(round(lat));
        export.setLon(round(lon));
        export.setType(radiation.getRadiationType());
        export.setValue(getValue(radiation));
        export.setUnit("kWh/m2");
        export.setDim("1 km2");
        export.setSource("DWD CDC");
        return export;
    }

    private String round(double lat) {
        return String.format(Locale.ENGLISH, "%.3f", Precision.round(lat, 3));
    }

    private double getValue(Radiation radiation) {
        return Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", radiation.getRadiationValue()));
    }

    private String parseDate(final int date) {
        return getDate(String.valueOf(date));
    }

    public String getDate(final String date) {
        try {
            return date.substring(0, 4) + "-" + date.substring(4);
        } catch (Exception e) {
            return "undefined";
        }
    }
}
