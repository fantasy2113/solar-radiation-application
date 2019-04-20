package de.josmer.app.library.interfaces;

import de.josmer.app.entities.Calculated;
import java.util.List;

public interface ICalculatedRepository {

    List<Calculated> calculateds(double[] eGlobHorMonthly, double lon, double lat, int ae, int ye, int year);
}
