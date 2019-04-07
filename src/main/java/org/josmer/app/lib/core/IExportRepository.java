package org.josmer.app.lib.core;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IExportRepository {

    List<Export> getAll(List<Radiation> radiations, double lon, double lat);

    List<String> getHeaders();

    String getProps();
}
