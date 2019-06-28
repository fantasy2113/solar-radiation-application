package de.josmer.dwdcdc.app.interfaces;

import java.util.LinkedList;
import java.util.List;

public interface ISolExporter<TOut, TIn> {

    LinkedList<TOut> getItems(LinkedList<TIn> items, double lon, double lat);

    List<String> getHeaders();

    String getProps();
}
