package de.josmer.dwdcdc.utils.interfaces;

import de.josmer.dwdcdc.utils.enums.SolRadTypes;

public interface ISolRadCrawler {
    void insert(IBasicSolRad solRadRepository, IDataReader fileReader, int month, int year);

    SolRadTypes getSolRadType();
}
