package de.josmer.app.exporter;

import de.josmer.app.entities.SolarRadiation;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Component;
import de.josmer.app.library.interfaces.IRadiationExport;

@Component
public class SolarRadiationExport extends Export<de.josmer.app.entities.SolarRadiationExport, SolarRadiation> implements IRadiationExport {

    @Override
    public List<de.josmer.app.entities.SolarRadiationExport> getItems(final List<SolarRadiation> radiations, final double lon, final double lat) {
        List<de.josmer.app.entities.SolarRadiationExport> exports = new LinkedList<>();
        try {
            double eGlobHorSum = 0.0;
            for (SolarRadiation radiation : radiations) {
                exports.add(mapToExport(lon, lat, radiation));
                eGlobHorSum += radiation.getRadiationValue();
                if (String.valueOf(radiation.getRadiationDate()).endsWith("12")) {
                    de.josmer.app.entities.SolarRadiationExport export = new de.josmer.app.entities.SolarRadiationExport();
                    export.setDate("Summe " + String.valueOf(radiation.getRadiationDate()).substring(0, 4));
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
                de.josmer.app.entities.SolarRadiationExport export = new de.josmer.app.entities.SolarRadiationExport();
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
            for (de.josmer.app.entities.SolarRadiationExport exportRadi : exports) {
                if (exportRadi.getDate().contains("Summe")) {
                    avgSum += exportRadi.getValue();
                    sumCnt++;
                }
            }

            de.josmer.app.entities.SolarRadiationExport export = new de.josmer.app.entities.SolarRadiationExport();
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
    protected de.josmer.app.entities.SolarRadiationExport mapToExport(double lon, double lat, SolarRadiation item) {
        de.josmer.app.entities.SolarRadiationExport export = new de.josmer.app.entities.SolarRadiationExport();
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
