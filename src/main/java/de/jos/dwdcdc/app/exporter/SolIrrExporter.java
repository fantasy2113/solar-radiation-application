package de.jos.dwdcdc.app.exporter;

import de.jos.dwdcdc.app.entities.SolIrr;
import de.jos.dwdcdc.app.entities.SolIrrExp;
import de.jos.dwdcdc.app.interfaces.ISolIrrExporter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class SolIrrExporter extends Exporter<SolIrrExp, SolIrr> implements ISolIrrExporter {

    private SolIrrExp extracted(double eGlobHor, double eGlobGen) {
        SolIrrExp exportCalc = new SolIrrExp();
        exportCalc.seteGlobHor(Double.parseDouble(roundToString(eGlobGen - eGlobHor, 2)));
        exportCalc.seteGlobGen(Double.parseDouble(roundToString(((eGlobGen / eGlobHor) * 100) - 100, 2)));
        exportCalc.setLat("");
        exportCalc.setLon("");
        exportCalc.setUnit("");
        exportCalc.setDim("");
        exportCalc.setSource("");
        exportCalc.setDate("G/V");
        if (Double.isNaN(exportCalc.geteGlobGen())) {
            exportCalc.seteGlobGen(0);
        }
        return exportCalc;
    }

    @Override
    public List<String> getHeaders() {
        return List.of("Datum", "Lat", "Lon", "Ausrichtung", "Neigung", "EGlobHor", "EGlobGen", "Einheit", "Dim",
                "Quelle");
    }

    @Override
    public List<SolIrrExp> getItems(List<SolIrr> items, double lon, double lat) {
        List<SolIrrExp> solIrrExps = new ArrayList<>();
        try {
            double eGlobHor = 0.0;
            double eGlobGen = 0.0;
            for (SolIrr solIrr : items) {
                eGlobHor += solIrr.geteGlobHor();
                eGlobGen += solIrr.geteGlobGen();
                solIrrExps.add(mapToExport(lon, lat, solIrr));
            }
            solIrrExps.add(getSolIrrExpSum(items, lon, lat, eGlobHor, eGlobGen));
            solIrrExps.add(extracted(eGlobHor, eGlobGen));
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
        return solIrrExps;
    }

    @Override
    public String getProps() {
        return "date, lat, lon, ae, ye, eGlobHor, eGlobGen, unit, dim, source, ";
    }

    private SolIrrExp getSolIrrExpSum(List<SolIrr> items, double lon, double lat, double eGlobHor, double eGlobGen) {
        SolIrrExp exportCalc = new SolIrrExp();
        exportCalc.seteGlobGen(Double.parseDouble(roundToString(eGlobGen, 2)));
        exportCalc.seteGlobHor(Double.parseDouble(roundToString(eGlobHor, 2)));
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
        return exportCalc;
    }

    @Override
    protected SolIrrExp mapToExport(double lon, double lat, SolIrr item) {
        SolIrrExp solIrrExp = new SolIrrExp();
        solIrrExp.seteGlobGen(Double.parseDouble(roundToString(item.geteGlobGen(), 2)));
        solIrrExp.seteGlobHor(Double.parseDouble(roundToString(item.geteGlobHor(), 2)));
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
