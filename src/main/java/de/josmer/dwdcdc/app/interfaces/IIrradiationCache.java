package de.josmer.dwdcdc.app.interfaces;

import de.josmer.dwdcdc.app.entities.SolIrrExp;

import java.time.LocalDateTime;
import java.util.LinkedList;

public interface IIrradiationCache extends Identifiable {

    LinkedList<SolIrrExp> getMonths();

    LocalDateTime getCreated();
}
