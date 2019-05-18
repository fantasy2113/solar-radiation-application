package de.josmer.springboot.dwdcdc.app.interfaces;

import java.util.List;

public interface ISave<TSave> {
    void save(List<TSave> items);
}
