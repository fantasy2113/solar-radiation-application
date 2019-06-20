package de.josmer.dwdcdc.app.base.interfaces;

import de.josmer.dwdcdc.app.base.entities.SolIrrExp;

import java.time.LocalDateTime;
import java.util.LinkedList;

public interface IIrradiationCache extends Identifiable {

    LinkedList<SolIrrExp> getMonths();

    LocalDateTime getCreated();
}
