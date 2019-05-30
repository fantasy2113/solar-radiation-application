package de.josmer.dwdcdc.app.interfaces;

import de.josmer.dwdcdc.app.controller.requests.IrrRequest;
import de.josmer.dwdcdc.app.entities.SolIrrExp;

import java.util.LinkedList;
import java.util.Optional;

public interface ISolIrrExpCache {
    void add(IrrRequest irrRequest, LinkedList<SolIrrExp> solIrrExps);

    Optional<LinkedList<SolIrrExp>> get(IrrRequest irrRequest);
}
