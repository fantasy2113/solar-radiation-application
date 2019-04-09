package org.josmer.application.interfaces;

import org.josmer.application.entities.Export;
import org.josmer.application.entities.Radiation;

import java.util.List;

public interface IExportRepository {

    List<Export> getAll(List<Radiation> radiations, double lon, double lat);

    List<String> getHeaders();

    String getProps();
}
