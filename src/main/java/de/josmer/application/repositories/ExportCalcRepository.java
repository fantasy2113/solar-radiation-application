package de.josmer.application.repositories;

import de.josmer.application.entities.Calculated;
import de.josmer.application.entities.ExportCalc;
import de.josmer.application.library.interfaces.IExportCalcRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExportCalcRepository extends AExportRepository<ExportCalc, Calculated> implements IExportCalcRepository {

    @Override
    public List<ExportCalc> getAll(List<Calculated> items, double lon, double lat) {
        return null;
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
        return null;
    }
}
