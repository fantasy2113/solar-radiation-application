package org.josmer.app.repository;

import org.josmer.app.core.IExportRepository;
import org.josmer.app.entity.Export;
import org.josmer.app.entity.Radiation;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

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
        return List.of("Datum", "Lat", "Lon", "Typ", "Wert");
    }

    @Override
    public String getProps() {
        return "Datum, Lat, Lon, Typ, Wert, ";
    }

    private Export mapToExport(double lon, double lat, Radiation radiation) {
        Export export = new Export();
        export.setDate(String.valueOf(radiation.getDate()));
        export.setLat(lat);
        export.setLon(lon);
        export.setTyp(radiation.getTyp());
        export.setValue(radiation.getValue());
        return export;
    }
}
