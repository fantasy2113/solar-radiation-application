package de.josmer.application.interfaces;

import de.josmer.application.entities.Export;
import de.josmer.application.entities.Radiation;

import java.util.List;

public interface IExportRepository {

    List<Export> getAll(List<Radiation> radiations, double lon, double lat);

    List<String> getHeaders();

    String getProps();
}
