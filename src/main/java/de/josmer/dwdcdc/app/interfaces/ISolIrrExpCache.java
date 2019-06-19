package de.josmer.dwdcdc.app.interfaces;

import de.josmer.dwdcdc.app.entities.SolIrrExp;

import java.util.LinkedList;
import java.util.Optional;

public interface ISolIrrExpCache<T extends IJsonb> {
    void add(T irrRequest, LinkedList<SolIrrExp> solIrrExps);

    Optional<LinkedList<SolIrrExp>> get(T irrRequest);
}
