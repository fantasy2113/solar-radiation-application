package org.josmer.interfaces;

import org.josmer.entities.Export;
import org.josmer.entities.Radiation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IExportRepository {
    List<Export> getAll(List<Radiation> radiations, double lon, double lat);

    List<String> getHeaders();

    String getProps();
}
