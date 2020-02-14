package de.jos.dwdcdc.app.interfaces;

import java.util.List;

public interface ISolExporter<TOut, TIn> {

    List<String> getHeaders();

    List<TOut> getItems(List<TIn> items, double lon, double lat);

    String getProps();
}
