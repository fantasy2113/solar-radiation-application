package de.josmer.dwdcdc.app.interfaces;

import java.time.LocalDateTime;
import java.util.LinkedList;

import de.josmer.dwdcdc.app.entities.SolIrrExp;

public interface IIrradiationCache extends Identifiable {

	LinkedList<SolIrrExp> getMonths();

	LocalDateTime getCreated();
}
