package org.josmer.app.core;

import org.josmer.app.entity.Export;
import org.josmer.app.entity.Radiation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IExportRepository {
    List<Export> getAll(List<Radiation> radiations, double lon, double lat);

    List<String> getHeaders();

    String getProps();
}
