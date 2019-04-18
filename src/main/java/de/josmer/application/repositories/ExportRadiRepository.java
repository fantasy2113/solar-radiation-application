package de.josmer.application.repositories;

import de.josmer.application.entities.ExportRadi;
import de.josmer.application.entities.Radiation;
import de.josmer.application.library.interfaces.IExportRadiRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ExportRadiRepository extends AExportRepository<ExportRadi, Radiation> implements IExportRadiRepository {

    @Override
    public List<ExportRadi> getAll(final List<Radiation> radiations, final double lon, final double lat) {
        List<ExportRadi> exports = new LinkedList<>();
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

    @Override
    protected ExportRadi mapToExport(double lon, double lat, Radiation item) {
        ExportRadi export = new ExportRadi();
        export.setDate(parseDate(item.getRadiationDate()));
        export.setLat(round(lat, 3));
        export.setLon(round(lon, 3));
        export.setType(item.getRadiationType());
        export.setValue(getValue(item.getRadiationValue()));
        export.setUnit("kWh/m2");
        export.setDim("1 km2");
        export.setSource("DWD CDC");
        return export;
    }


}
