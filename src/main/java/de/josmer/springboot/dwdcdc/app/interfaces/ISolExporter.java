package de.josmer.springboot.dwdcdc.app.interfaces;

import java.util.List;

public interface ISolExporter<TOut, TIn> {
    List<TOut> getItems(List<TIn> items, double lon, double lat);

    List<String> getHeaders();

    String getProps();
}
