package de.jos.dwdcdc.app.exporter;

import de.jos.dwdcdc.app.entities.SolRad;
import de.jos.dwdcdc.app.entities.SolRadExp;
import de.jos.dwdcdc.app.interfaces.ISolRadExporter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class SolRadExporter extends Exporter<SolRadExp, SolRad> implements ISolRadExporter {

    private void addSolRadExp(List<SolRadExp> solRadExps, double eGlobHorSum, SolRadExp solRadExp) {
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

    private void addSumEnd(List<SolRadExp> solRadExps) {
        int sumCnt = 0;
        double avgSum = 0.0;
        for (SolRadExp solRadExp : solRadExps) {
            if (solRadExp.getDate().contains("Summe")) {
                avgSum += solRadExp.getValue();
                sumCnt++;
            }
        }
        SolRadExp solRadExp = new SolRadExp();
        solRadExp.setDate("Summe Mittel");
        addSolRadExp(solRadExps, avgSum / sumCnt, solRadExp);
    }

    private void addValues(final List<SolRad> items, final double lon, final double lat,
                           List<SolRadExp> solRadExps) {
        double eGlobHorSum = 0.0;
        for (SolRad solRad : items) {
            solRadExps.add(mapToExport(lon, lat, solRad));
            eGlobHorSum += solRad.getRadiationValue();
            if (String.valueOf(solRad.getRadiationDate()).endsWith("12")) {
                SolRadExp export = new SolRadExp();
                export.setDate("Summe " + String.valueOf(solRad.getRadiationDate()).substring(0, 4));
                addSolRadExp(solRadExps, eGlobHorSum, export);
                eGlobHorSum = 0.0;
            }
        }
        if (!solRadExps.isEmpty() && !solRadExps.get(solRadExps.size() - 1).getDate().contains("Summe")) {
            SolRadExp export = new SolRadExp();
            export.setDate("Summe " + solRadExps.get(solRadExps.size() - 1).getDate().substring(0, 4));
            addSolRadExp(solRadExps, eGlobHorSum, export);
        }
    }

    @Override
    public List<String> getHeaders() {
        return List.of("Datum", "Lat", "Lon", "Art", "EGlobHor", "Einheit", "Dim", "Quelle");
    }

    @Override
    public List<SolRadExp> getItems(final List<SolRad> items, final double lon, final double lat) {
        List<SolRadExp> solRadExps = new ArrayList<>();
        try {
            addValues(items, lon, lat, solRadExps);
            addSumEnd(solRadExps);
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        return solRadExps;
    }

    @Override
    public String getProps() {
        return "date, lat, lon, type, value, unit, dim, source, ";
    }

    @Override
    protected SolRadExp mapToExport(double lon, double lat, SolRad item) {
        SolRadExp solRadExp = new SolRadExp();
        solRadExp.setDate(parseDate(item.getRadiationDate()));
        solRadExp.setLat(roundToString(lat, 3));
        solRadExp.setLon(roundToString(lon, 3));
        solRadExp.setType(item.getRadiationType());
        solRadExp.setValue(getValue(item.getRadiationValue()));
        solRadExp.setUnit("kWh/m2");
        solRadExp.setDim("1 km2");
        solRadExp.setSource("DWD CDC");
        return solRadExp;
    }
}
