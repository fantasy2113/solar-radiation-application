package de.jos.dwdcdc.library.interfaces;

import de.jos.dwdcdc.library.enums.SolRadTypes;

public interface ISolRadCrawler {

    SolRadTypes getSolRadType();

    void insert(IBasicSolRad solRadRepository, IDataReader fileReader, int month, int year);
}
