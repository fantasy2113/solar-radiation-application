package de.josmer.springboot.dwdcdc.app.interfaces;

public interface ISolRadCrawler {
    void insert(ISolRadRepository solRadRepository, IFileReader fileReader);
}
