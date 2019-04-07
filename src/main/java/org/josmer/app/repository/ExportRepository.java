package org.josmer.app.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.josmer.app.core.IExportRepository;
import org.josmer.app.entity.Export;
import org.josmer.app.entity.Radiation;
import org.springframework.stereotype.Component;

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
        return List.of("Datum", "Breitengrad", "Längengrad", "Art", "Wert", "Einheit");
    }

    @Override
    public String getProps() {
        return "date, lat, lon, type, value, unit";
    }

    private Export mapToExport(double lon, double lat, Radiation radiation) {
        Export export = new Export();
        export.setDate(parseDate(radiation.getRadiationDate()));
        export.setLat(lat);
        export.setLon(lon);
        export.setType(radiation.getRadiationType());
        export.setValue(Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", radiation.getRadiationValue())));
        export.setUnit("kWh/m2");
        return export;
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
