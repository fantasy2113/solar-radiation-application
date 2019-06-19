package de.josmer.dwdcdc.library.interfaces;

import de.josmer.dwdcdc.library.enums.SolRadTypes;

public interface ISolRadCrawler {
    void insert(IBasicSolRad solRadRepository, IDataReader fileReader, int month, int year);

    SolRadTypes getSolRadType();
}
