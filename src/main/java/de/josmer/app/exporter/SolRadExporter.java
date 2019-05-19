package de.josmer.app.exporter;

import de.josmer.app.entities.SolRadExp;
import de.josmer.app.interfaces.ISolRadExporter;
import de.josmer.libs.entities.SolRad;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public final class SolRadExporter extends Exporter<SolRadExp, SolRad> implements ISolRadExporter {

    @Override
    public LinkedList<SolRadExp> getItems(final LinkedList<SolRad> items, final double lon, final double lat) {
        LinkedList<SolRadExp> solRadExps = new LinkedList<>();
        try {
            double eGlobHorSum = 0.0;
            for (SolRad solRad : items) {
                solRadExps.add(mapToExport(lon, lat, solRad));
                eGlobHorSum += solRad.getRadiationValue();
                if (String.valueOf(solRad.getRadiationDate()).endsWith("12")) {
                    SolRadExp export = new SolRadExp();
                    export.setDate("Summe " + String.valueOf(solRad.getRadiationDate()).substring(0, 4));
                    addSum(solRadExps, eGlobHorSum, export);
                    eGlobHorSum = 0.0;
                }
            }
            if (solRadExps.size() >= 1 && !solRadExps.get(solRadExps.size() - 1).getDate().contains("Summe")) {
                SolRadExp export = new SolRadExp();
                export.setDate("Summe " + solRadExps.get(solRadExps.size() - 1).getDate().substring(0, 4));
                addSum(solRadExps, eGlobHorSum, export);
            }

            int sumCnt = 0;
            double avgSum = 0.0;
            for (SolRadExp solRadExp : solRadExps) {
                if (solRadExp.getDate().contains("Summe")) {
                    avgSum += solRadExp.getValue();
                    sumCnt++;
                }
            }

            SolRadExp export = new SolRadExp();
            export.setDate("Summe Mittel");
            addSum(solRadExps, avgSum / sumCnt, export);

        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return solRadExps;
    }

    private void addSum(List<SolRadExp> solRadExps, double eGlobHorSum, SolRadExp solRadExp) {
        solRadExp.setLat("");
        solRadExp.setLon("");
        solRadExp.setType("");
        solRadExp.setValue(Double.valueOf(roundToString(eGlobHorSum, 2)));
        solRadExp.setUnit("kWh/m2");
        solRadExp.setDim("1 km2");
        solRadExp.setSource("DWD CDC");

        if (Double.isNaN(solRadExp.getValue())) {
            solRadExp.setValue(0);
        }

        solRadExps.add(solRadExp);
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
