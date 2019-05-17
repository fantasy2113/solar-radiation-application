package de.josmer.application.exporter;

import de.josmer.application.model.entities.SolRad;
import de.josmer.application.model.entities.SolRadExp;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class SolRadExporter extends Export<SolRadExp, SolRad> {

    public List<SolRadExp> getItems(final List<SolRad> solarRadiations, final double lon, final double lat) {
        List<SolRadExp> exports = new LinkedList<>();
        try {
            double eGlobHorSum = 0.0;
            for (SolRad solRad : solarRadiations) {
                exports.add(mapToExport(lon, lat, solRad));
                eGlobHorSum += solRad.getRadiationValue();
                if (String.valueOf(solRad.getRadiationDate()).endsWith("12")) {
                    SolRadExp export = new SolRadExp();
                    export.setDate("Summe " + String.valueOf(solRad.getRadiationDate()).substring(0, 4));
                    export.setLat("");
                    export.setLon("");
                    export.setType("");
                    export.setValue(Double.valueOf(roundToString(eGlobHorSum, 2)));
                    export.setUnit("kWh/m2");
                    export.setDim("1 km2");
                    export.setSource("DWD CDC");
                    exports.add(export);
                    eGlobHorSum = 0.0;
                }
            }
            if (!exports.get(exports.size() - 1).getDate().contains("Summe")) {
                SolRadExp export = new SolRadExp();
                export.setDate("Summe " + exports.get(exports.size() - 1).getDate().substring(0, 4));
                export.setLat("");
                export.setLon("");
                export.setType("");
                export.setValue(Double.valueOf(roundToString(eGlobHorSum, 2)));
                export.setUnit("kWh/m2");
                export.setDim("1 km2");
                export.setSource("DWD CDC");
                exports.add(export);
            }

            int sumCnt = 0;
            double avgSum = 0.0;
            for (SolRadExp exportRadi : exports) {
                if (exportRadi.getDate().contains("Summe")) {
                    avgSum += exportRadi.getValue();
                    sumCnt++;
                }
            }

            SolRadExp export = new SolRadExp();
            export.setDate("Summe Mittel");
            export.setLat("");
            export.setLon("");
            export.setType("");
            export.setValue(Double.valueOf(roundToString(avgSum / sumCnt, 2)));
            export.setUnit("kWh/m2");
            export.setDim("1 km2");
            export.setSource("DWD CDC");
            exports.add(export);

        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return exports;
    }

    public List<String> getHeaders() {
        return List.of("Datum", "Lat", "Lon", "Art", "EGlobHor", "Einheit", "Dim", "Quelle");
    }

    public String getProps() {
        return "date, lat, lon, type, value, unit, dim, source, ";
    }

    @Override
    protected SolRadExp mapToExport(double lon, double lat, SolRad item) {
        SolRadExp export = new SolRadExp();
        export.setDate(parseDate(item.getRadiationDate()));
        export.setLat(roundToString(lat, 3));
        export.setLon(roundToString(lon, 3));
        export.setType(item.getRadiationType());
        export.setValue(getValue(item.getRadiationValue()));
        export.setUnit("kWh/m2");
        export.setDim("1 km2");
        export.setSource("DWD CDC");
        return export;
    }
}
