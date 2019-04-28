package de.josmer.app.exporter;

import de.josmer.app.library.interfaces.ISolarRadiationInclinedExport;
import de.josmer.app.model.entities.SolRadiInc;
import de.josmer.app.model.entities.SolRadiIncExp;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class SolRadiIncExporter extends Export<SolRadiIncExp, SolRadiInc> implements ISolarRadiationInclinedExport {

    @Override
    public List<SolRadiIncExp> getItems(List<SolRadiInc> items, double lon, double lat) {
        List<SolRadiIncExp> exportCalcs = new LinkedList<>();
        try {
            double eGlobHor = 0.0;
            double eGlobGen = 0.0;

            for (SolRadiInc calculated : items) {
                eGlobHor += calculated.geteGlobHor();
                eGlobGen += calculated.geteGlobGen();
                exportCalcs.add(mapToExport(lon, lat, calculated));
            }

            SolRadiIncExp exportCalc = new SolRadiIncExp();
            exportCalc.seteGlobGen(Double.valueOf(roundToString(eGlobGen, 2)));
            exportCalc.seteGlobHor(Double.valueOf(roundToString(eGlobHor, 2)));
            exportCalc.setLat(roundToString(lat, 3));
            exportCalc.setLon(roundToString(lon, 3));
            exportCalc.setUnit("kWh/m2");
            exportCalc.setDim("1 km2");
            exportCalc.setSource("DWD CDC");
            exportCalc.setDate("Summe");
            exportCalc.setYe(String.valueOf((int) items.get(0).getYe()));
            exportCalc.setAe(String.valueOf((int) items.get(0).getAe()));

            exportCalcs.add(exportCalc);

            exportCalc = new SolRadiIncExp();
            exportCalc.seteGlobHor(Double.valueOf(roundToString(eGlobGen - eGlobHor, 2)));
            exportCalc.seteGlobGen(Double.valueOf(roundToString(((eGlobGen / eGlobHor) * 100) - 100, 2)));
            exportCalc.setLat("");
            exportCalc.setLon("");
            exportCalc.setUnit("");
            exportCalc.setDim("");
            exportCalc.setSource("");
            exportCalc.setDate("G/V");

            exportCalcs.add(exportCalc);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
        return exportCalcs;
    }

    @Override
    public List<String> getHeaders() {
        return List.of("Datum", "Lat", "Lon", "Ausrichtung", "Neigung", "EGlobHor", "EGlobGen", "Einheit", "Dim", "Quelle");
    }

    @Override
    public String getProps() {
        return "date, lat, lon, ae, ye, eGlobHor, eGlobGen, unit, dim, source, ";
    }

    @Override
    protected SolRadiIncExp mapToExport(double lon, double lat, SolRadiInc item) {
        SolRadiIncExp exportCalc = new SolRadiIncExp();
        exportCalc.seteGlobGen(Double.valueOf(roundToString(item.geteGlobGen(), 2)));
        exportCalc.seteGlobHor(Double.valueOf(roundToString(item.geteGlobHor(), 2)));
        exportCalc.setLat(roundToString(lat, 3));
        exportCalc.setLon(roundToString(lon, 3));
        exportCalc.setUnit("kWh/m2");
        exportCalc.setDim("1 km2");
        exportCalc.setSource("DWD CDC");
        exportCalc.setDate(item.getCalculatedDate());
        exportCalc.setYe(String.valueOf((int) item.getYe()));
        exportCalc.setAe(String.valueOf((int) item.getAe()));
        return exportCalc;
    }
}
