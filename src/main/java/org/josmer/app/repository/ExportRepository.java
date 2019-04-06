package org.josmer.app.repository;

import org.josmer.app.entity.Export;
import org.josmer.app.entity.Radiation;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import org.josmer.app.core.IExportRepository;

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
        return List.of("Date", "Lat", "Lon", "Type", "Value", "Unit");
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
        export.setValue(radiation.getRadiationValue());
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
