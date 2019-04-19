package de.josmer.app.repositories;

import de.josmer.app.entities.Calculated;
import de.josmer.app.entities.ExportCalc;
import de.josmer.app.library.interfaces.IExportCalcRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ExportCalcRepository extends AExportRepository<ExportCalc, Calculated> implements IExportCalcRepository {

    @Override
    public List<ExportCalc> getAll(List<Calculated> items, double lon, double lat) {
        List<ExportCalc> exportCalcs = new LinkedList<>();
        try {
            double eGlobHor = 0.0;
            double eGlobGen = 0.0;
            for (Calculated calculated : items) {
                eGlobHor += Double.valueOf(roundToString(calculated.getEGlobHor(), 0));
                eGlobGen += Double.valueOf(roundToString(calculated.getEGlobGen(), 0));
                exportCalcs.add(mapToExport(lon, lat, calculated));
            }
            ExportCalc exportCalc = new ExportCalc();
            exportCalc.setEGlobGen(Double.valueOf(roundToString(eGlobGen, 0)));
            exportCalc.setEGlobHor(Double.valueOf(roundToString(eGlobHor, 0)));
            exportCalc.setLat(roundToString(lat, 3));
            exportCalc.setLon(roundToString(lon, 3));
            exportCalc.setUnit("kWh/m2");
            exportCalc.setDim("1 km2");
            exportCalc.setSource("DWD CDC");
            exportCalc.setDate("Summe");
            exportCalcs.add(exportCalc);
            exportCalc = new ExportCalc();
            exportCalc.setEGlobGen(Double.valueOf(roundToString((eGlobGen / eGlobHor) * 100, 1)));
            exportCalc.setEGlobHor(100.0);
            exportCalc.setLat("");
            exportCalc.setLon("");
            exportCalc.setUnit("");
            exportCalc.setDim("");
            exportCalc.setSource("");
            exportCalc.setDate("");
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
    protected ExportCalc mapToExport(double lon, double lat, Calculated item) {
        ExportCalc exportCalc = new ExportCalc();
        exportCalc.setEGlobGen(Double.valueOf(roundToString(item.getEGlobGen(), 0)));
        exportCalc.setEGlobHor(Double.valueOf(roundToString(item.getEGlobHor(), 0)));
        exportCalc.setLat(roundToString(lat, 3));
        exportCalc.setLon(roundToString(lon, 3));
        exportCalc.setUnit("kWh/m2");
        exportCalc.setDim("1 km2");
        exportCalc.setSource("DWD CDC");
        exportCalc.setDate(item.getCalculatedDate());
        return exportCalc;
    }
}
