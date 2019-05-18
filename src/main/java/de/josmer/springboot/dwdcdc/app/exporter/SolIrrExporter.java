package de.josmer.springboot.dwdcdc.app.exporter;

import de.josmer.springboot.dwdcdc.app.entities.SolIrr;
import de.josmer.springboot.dwdcdc.app.entities.SolIrrExp;
import de.josmer.springboot.dwdcdc.app.interfaces.ISolIrrExporter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public final class SolIrrExporter extends Exporter<SolIrrExp, SolIrr> implements ISolIrrExporter {
    @Override
    public List<SolIrrExp> getItems(List<SolIrr> items, double lon, double lat) {
        List<SolIrrExp> exports = new LinkedList<>();
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
            exportCalc.setYe(String.valueOf((int) items.get(0).getYe()));
            exportCalc.setAe(String.valueOf((int) items.get(0).getAe()));

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

            exports.add(exportCalc);
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
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
        SolIrrExp exportCalc = new SolIrrExp();
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
