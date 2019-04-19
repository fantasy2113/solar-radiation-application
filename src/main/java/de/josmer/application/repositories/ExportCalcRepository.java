package de.josmer.application.repositories;

import de.josmer.application.entities.Calculated;
import de.josmer.application.entities.ExportCalc;
import de.josmer.application.library.interfaces.IExportCalcRepository;
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
                eGlobHor += calculated.getEGlobHor();
                eGlobGen += calculated.getEGlobGen();
                exportCalcs.add(mapToExport(lon, lat, calculated));
            }
            ExportCalc exportCalc = new ExportCalc();
            exportCalc.setEGlobGen(Double.valueOf(round(eGlobGen, 2)));
            exportCalc.setEGlobHor(Double.valueOf(round(eGlobHor, 2)));
            exportCalc.setLat(round(lat, 3));
            exportCalc.setLon(round(lon, 3));
            exportCalc.setUnit("kWh/m2");
            exportCalc.setDim("1 km2");
            exportCalc.setSource("DWD CDC");
            exportCalc.setDate("Summe");
            exportCalcs.add(exportCalc);
            exportCalc = new ExportCalc();
            exportCalc.setEGlobGen(Double.valueOf(round((eGlobGen / eGlobHor) * 100, 0)));
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
        return null;
    }

    @Override
    public String getProps() {
        return null;
    }

    @Override
    protected ExportCalc mapToExport(double lon, double lat, Calculated item) {
        ExportCalc exportCalc = new ExportCalc();
        exportCalc.setEGlobGen(item.getEGlobGen());
        exportCalc.setEGlobHor(item.getEGlobHor());
        exportCalc.setLat(round(lat, 3));
        exportCalc.setLon(round(lon, 3));
        exportCalc.setUnit("kWh/m2");
        exportCalc.setDim("1 km2");
        exportCalc.setSource("DWD CDC");
        exportCalc.setDate(item.getCalculatedDate());
        return exportCalc;
    }
}
