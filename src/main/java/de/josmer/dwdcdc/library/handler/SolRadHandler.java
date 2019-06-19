package de.josmer.dwdcdc.library.handler;

import de.josmer.dwdcdc.library.enums.SolRadTypes;
import de.josmer.dwdcdc.library.interfaces.IBasicSolRad;
import de.josmer.dwdcdc.library.interfaces.IDataReader;
import de.josmer.dwdcdc.library.interfaces.IHandler;
import de.josmer.dwdcdc.library.interfaces.ISolRadCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

abstract class SolRadHandler implements IHandler {
    static final Logger LOGGER = LoggerFactory.getLogger(SolRadHandler.class.getName());
    final IBasicSolRad solRadRepository;
    final IDataReader fileReader;
    final ISolRadCrawler solRadCrawler;
    private boolean started;

    SolRadHandler(ISolRadCrawler solRadCrawler, IBasicSolRad solRadRepository, IDataReader fileReader) {
        this.solRadCrawler = solRadCrawler;
        this.solRadRepository = solRadRepository;
        this.fileReader = fileReader;
        this.started = false;
    }

    @Override
    public void run() {
        try {
            runInsert();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
    }

    @Override
    public void start() {
        if (isStarted()) {
            return;
        }
        try {
            handlerStarted();
            startHandler();
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
    }

    protected abstract void runInsert();

    protected abstract void startHandler();

    LocalDate getLocalDate() {
        LocalDate localDate = LocalDate.now();
        if (localDate.getDayOfMonth() < 15) {
            return localDate.minusMonths(2);
        }
        return localDate.minusMonths(1);
    }

    int getEndYear(LocalDate localDate) {
        return localDate.getYear() + 1;
    }

    int getStartYear() {
        if (solRadCrawler.getSolRadType() == SolRadTypes.GLOBAL) {
            return 1991;
        }
        return 2015;
    }

    void logStart() {
        LOGGER.info(solRadCrawler.getSolRadType() + " - started");
    }

    boolean isStarted() {
        return started;
    }

    private void handlerStarted() {
        this.started = true;
    }
}
