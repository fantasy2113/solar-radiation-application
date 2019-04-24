package de.josmer.app.library.interfaces;

import de.josmer.app.entities.SolRadiInc;
import java.util.List;

public interface ISolarRadiationInclinedRepository {

    List<SolRadiInc> getSolarRadiations(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year);
}
