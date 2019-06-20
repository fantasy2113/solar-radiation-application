package de.josmer.dwdcdc.app.exporter;

import de.josmer.dwdcdc.app.entities.SolIrr;
import de.josmer.dwdcdc.app.entities.SolIrrExp;
import de.josmer.dwdcdc.app.interfaces.ISolIrrExporter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public final class SolIrrExporter extends Exporter<SolIrrExp, SolIrr> implements ISolIrrExporter {
    @Override
    public LinkedList<SolIrrExp> getItems(LinkedList<SolIrr> items, double lon, double lat) {
        LinkedList<SolIrrExp> exports = new LinkedList<>();
        try {
            double eGlobHor = 0.0;
            double eGlobGen = 0.0;

            for (SolIrr calculated : items) {
                eGlobHor += calculated.geteGlobHor();
                eGlobGen += calculated.geteGlobGen();
                exports.add(mapToExport(lon, lat, calculated));
            }

            SolIrrExp exportCalc = new SolIrrExp();
            exportCalc.seteGlobGen(Double.valueOf(roundToString(eGlobGen, 2)));
            exportCalc.seteGlobHor(Double.valueOf(roundToString(eGlobHor, 2)));
            exportCalc.setLat(roundToString(lat, 3));
            exportCalc.setLon(roundToString(lon, 3));
            exportCalc.setUnit("kWh/m2");
            exportCalc.setDim("1 km2");
            exportCalc.setSource("DWD CDC");
            exportCalc.setDate("Summe");

            if (!items.isEmpty()) {
                exportCalc.setYe(String.valueOf((int) items.get(0).getYe()));
                exportCalc.setAe(String.valueOf((int) items.get(0).getAe()));
            }

            exports.add(exportCalc);

            exportCalc = new SolIrrExp();
            exportCalc.seteGlobHor(Double.valueOf(roundToString(eGlobGen - eGlobHor, 2)));
            exportCalc.seteGlobGen(Double.valueOf(roundToString(((eGlobGen / eGlobHor) * 100) - 100, 2)));
            exportCalc.setLat("");
            exportCalc.setLon("");
            exportCalc.setUnit("");
            exportCalc.setDim("");
            exportCalc.setSource("");
            exportCalc.setDate("G/V");

            if (Double.isNaN(exportCalc.geteGlobGen())) {
                exportCalc.seteGlobGen(0);
            }

            exports.add(exportCalc);
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        return exports;
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
    protected SolIrrExp mapToExport(double lon, double lat, SolIrr item) {
        SolIrrExp solIrrExp = new SolIrrExp();
        solIrrExp.seteGlobGen(Double.valueOf(roundToString(item.geteGlobGen(), 2)));
        solIrrExp.seteGlobHor(Double.valueOf(roundToString(item.geteGlobHor(), 2)));
        solIrrExp.setLat(roundToString(lat, 3));
        solIrrExp.setLon(roundToString(lon, 3));
        solIrrExp.setUnit("kWh/m2");
        solIrrExp.setDim("1 km2");
        solIrrExp.setSource("DWD CDC");
        solIrrExp.setDate(item.getCalculatedDate());
        solIrrExp.setYe(String.valueOf((int) item.getYe()));
        solIrrExp.setAe(String.valueOf((int) item.getAe()));
        return solIrrExp;
    }
}
