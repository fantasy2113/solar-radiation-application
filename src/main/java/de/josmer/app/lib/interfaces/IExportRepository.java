package de.josmer.app.lib.interfaces;

import de.josmer.app.entities.Export;
import de.josmer.app.entities.Radiation;

import java.util.List;

public interface IExportRepository {

    List<Export> getAll(List<Radiation> radiations, double lon, double lat);

    List<String> getHeaders();

    String getProps();
}
