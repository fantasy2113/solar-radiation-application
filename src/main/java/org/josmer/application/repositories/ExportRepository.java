package org.josmer.application.repositories;

import org.josmer.application.entities.Export;
import org.josmer.application.entities.Radiation;
import org.josmer.application.interfaces.IExportRepository;
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
        return List.of("Datum", "Breitengrad (WGS84)", "Längengrad (WGS84)", "Art", "Wert", "Einheit", "Quelle");
    }

    @Override
    public String getProps() {
        return "date, lat, lon, type, value, unit, resolution, source, ";
    }

    private Export mapToExport(double lon, double lat, Radiation radiation) {
        Export export = new Export();
        export.setDate(parseDate(radiation.getRadiationDate()));
        export.setLat(lat);
        export.setLon(lon);
        export.setType(radiation.getRadiationType());
        export.setValue(Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", radiation.getRadiationValue())));
        export.setUnit("kWh/m2");
        export.setResolution("1 km2");
        export.setSource("DWD/KU1HA");
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
