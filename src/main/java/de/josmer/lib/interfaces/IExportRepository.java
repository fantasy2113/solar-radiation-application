package de.josmer.lib.interfaces;

import de.josmer.entities.Export;
import de.josmer.entities.Radiation;

import java.util.List;

public interface IExportRepository {

    List<Export> getAll(List<Radiation> radiations, double lon, double lat);

    List<String> getHeaders();

    String getProps();
}
