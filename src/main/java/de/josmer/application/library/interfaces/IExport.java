package de.josmer.application.library.interfaces;

import java.util.List;

public interface IExport<R, I> {

    List<R> getItems(List<I> items, double lon, double lat);

    List<String> getHeaders();

    String getProps();
}
