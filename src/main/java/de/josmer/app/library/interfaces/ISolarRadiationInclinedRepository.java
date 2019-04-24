package de.josmer.app.library.interfaces;

import de.josmer.app.entities.SolRadiInc;
import java.util.List;

public interface ISolarRadiationInclinedRepository {

    List<SolRadiInc> getSolarRadiationsInclined(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year);
}
