package de.josmer.app.exporter;

import de.josmer.app.library.interfaces.ISolRadiExporter;
import de.josmer.app.model.entities.SolRadi;
import de.josmer.app.model.entities.SolRadiExp;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class SolRadiExporter extends Export<SolRadiExp, SolRadi> implements ISolRadiExporter {

    @Override
    public List<SolRadiExp> getItems(final List<SolRadi> solarRadiations, final double lon, final double lat) {
        List<SolRadiExp> exports = new LinkedList<>();
        try {
            double eGlobHorSum = 0.0;
            for (SolRadi solRad : solarRadiations) {
                exports.add(mapToExport(lon, lat, solRad));
                eGlobHorSum += solRad.getRadiationValue();
                if (String.valueOf(solRad.getRadiationDate()).endsWith("12")) {
                    de.josmer.app.model.entities.SolRadiExp export = new de.josmer.app.model.entities.SolRadiExp();
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
                de.josmer.app.model.entities.SolRadiExp export = new de.josmer.app.model.entities.SolRadiExp();
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
            for (de.josmer.app.model.entities.SolRadiExp exportRadi : exports) {
                if (exportRadi.getDate().contains("Summe")) {
                    avgSum += exportRadi.getValue();
                    sumCnt++;
                }
            }

            de.josmer.app.model.entities.SolRadiExp export = new de.josmer.app.model.entities.SolRadiExp();
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

    @Override
    public List<String> getHeaders() {
        return List.of("Datum", "Lat", "Lon", "Art", "EGlobHor", "Einheit", "Dim", "Quelle");
    }

    @Override
    public String getProps() {
        return "date, lat, lon, type, value, unit, dim, source, ";
    }

    @Override
    protected de.josmer.app.model.entities.SolRadiExp mapToExport(double lon, double lat, SolRadi item) {
        de.josmer.app.model.entities.SolRadiExp export = new de.josmer.app.model.entities.SolRadiExp();
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
