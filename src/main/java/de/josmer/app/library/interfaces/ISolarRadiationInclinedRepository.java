package de.josmer.app.library.interfaces;

import de.josmer.app.entities.SolarRadiationInclined;
import java.util.List;

public interface ISolarRadiationInclinedRepository {

    List<SolarRadiationInclined> getSolarEnergies(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year);
}
