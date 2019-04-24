package de.josmer.app.library.interfaces;

import de.josmer.app.entities.SolarRadiation;
import java.util.List;

public interface ISolarRadiationRepository {

    double[] findGlobal(final IGaussKrueger gaussKrueger, int startDate, int endDate, double lon, double lat);

    List<SolarRadiation> find(IGaussKrueger gaussKrueger, int startDate, int endDate, String type, double lon, double lat);

    void save(List<SolarRadiation> radiations);

    long count();
}
