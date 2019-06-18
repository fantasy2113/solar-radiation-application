package de.josmer.dwdcdc.utils.handler;

import de.josmer.dwdcdc.app.interfaces.ISolRadRepository;
import de.josmer.dwdcdc.utils.interfaces.IDataReader;
import de.josmer.dwdcdc.utils.interfaces.ISolRadCrawler;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.IntStream;

public final class SolRadInsertAllHandler extends SolRadHandler {
    private final boolean parallel;

    public SolRadInsertAllHandler(ISolRadCrawler solRadCrawler, ISolRadRepository solRadRepository, IDataReader fileReader, boolean parallel) {
        super(solRadCrawler, solRadRepository, fileReader);
        this.parallel = parallel;
    }

    @Override
    protected void startHandler() {
        ScheduledExecutorService tokenService = Executors.newScheduledThreadPool(1);
        tokenService.execute(this);
        logStart();
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

    private void insertYear(int year) {
        try {
            for (int month = 1; month < 13; month++) {
                LOGGER.info(MessageFormat.format("try to runInsert: month: {0}, Year: {1} -> {2}", month, year, solRadCrawler.getSolRadType()));
                solRadCrawler.insert(solRadRepository, fileReader, month, year);
            }
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
    }
}
