package de.jos.dwdcdc.library.handler;

import de.jos.dwdcdc.library.interfaces.IBasicSolRad;
import de.jos.dwdcdc.library.interfaces.IDataReader;
import de.jos.dwdcdc.library.interfaces.ISolRadCrawler;

import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.IntStream;

public final class SolRadInsertAllHandler extends SolRadHandler {

    private final boolean parallel;

    public SolRadInsertAllHandler(ISolRadCrawler solRadCrawler, IBasicSolRad solRadRepository, IDataReader fileReader,
                                  boolean parallel) {
        super(solRadCrawler, solRadRepository, fileReader);
        this.parallel = parallel;
    }

    private void insertYear(int year) {
        try {
            for (int month = 1; month < 13; month++) {
                logInser(year, month);
                solRadCrawler.insert(solRadRepository, fileReader, month, year);
            }
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
    }

    @Override
    protected void runInsert() {
        LocalDate localDate = LocalDate.now();
        if (parallel) {
            IntStream.range(getStartYear(), getEndYear(localDate)).parallel().forEach(this::insertYear);
        } else {
            IntStream.range(getStartYear(), getEndYear(localDate)).sequential().forEach(this::insertYear);
        }
    }

    @Override
    protected void startHandler() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.execute(this);
        logStart();
    }
}
