package org.josmer.app.core;

import java.util.List;
import org.josmer.app.entity.Export;
import org.josmer.app.entity.Radiation;
import org.springframework.stereotype.Component;

@Component
public interface IExportRepository {

    List<Export> getAll(List<Radiation> radiations, double lon, double lat);

    List<String> getHeaders();

    String getProps();
}
