package de.josmer.springboot.dwdcdc.app.interfaces;

import de.josmer.springboot.dwdcdc.app.crawler.RadTypes;

public interface ISolRadCrawler {
    void insert(ISolRadRepository solRadRepository, IFileReader fileReader, int month, int year, RadTypes type);
}
