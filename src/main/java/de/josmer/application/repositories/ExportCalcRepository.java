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
        for (Calculated calculated : items) {
            exportCalcs.add(mapToExport(lon, lat, calculated));
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
        exportCalc.setLat(round(lat));
        exportCalc.setLon(round(lon));
        exportCalc.setUnit("kWh/m2");
        exportCalc.setDim("1 km2");
        exportCalc.setSource("DWD CDC");
        exportCalc.setDate(item.getCalculatedDate());
        return exportCalc;
    }
}
