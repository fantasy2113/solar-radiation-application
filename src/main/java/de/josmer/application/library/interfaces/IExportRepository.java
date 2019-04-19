package de.josmer.application.library.interfaces;

import java.util.List;

public interface IExportRepository<R, I> {
    List<R> getAll(List<I> items, double lon, double lat);

    List<String> getHeaders();

    String getProps();
}
