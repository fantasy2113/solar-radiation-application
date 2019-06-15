package de.josmer.dwdcdc.utils.handler;

import de.josmer.dwdcdc.app.interfaces.ISolRadRepository;
import de.josmer.dwdcdc.utils.enums.SolRadTypes;
import de.josmer.dwdcdc.utils.interfaces.IDataReader;
import de.josmer.dwdcdc.utils.interfaces.IHandler;
import de.josmer.dwdcdc.utils.interfaces.ISolRadCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public final class SolRadInsertHandler implements IHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolRadInsertHandler.class.getName());
    private final ISolRadRepository solRadRepository;
    private final IDataReader fileReader;
    private final ISolRadCrawler solRadCrawler;
    private boolean isStart;

    public SolRadInsertHandler(ISolRadCrawler solRadCrawler, ISolRadRepository solRadRepository, IDataReader fileReader) {
        this.solRadCrawler = solRadCrawler;
        this.solRadRepository = solRadRepository;
        this.fileReader = fileReader;
        this.isStart = false;
    }

    @Override
    public void run() {
        try {
            if (System.getenv("INSERT_ALL") == null) {
                insert();
            } else {
                insertAll();
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    @Override
    public void start() {
        if (isStart) {
            return;
        }

        try {
            isStart = true;
            ScheduledExecutorService tokenService = Executors.newScheduledThreadPool(1);
            long midnight = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES);
            if (System.getenv("INSERT_ALL") == null) {
                tokenService.scheduleAtFixedRate(this, midnight, 1440, TimeUnit.MINUTES);
            } else {
                tokenService.execute(this);
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private LocalDate getLocalDate() {
        LocalDate localDate = LocalDate.now();
        if (localDate.getDayOfMonth() < 15) {
            return localDate.minusMonths(2);
        }
        return localDate.minusMonths(1);
    }

    private void insert() {
        LocalDate localDate = getLocalDate();
        LOGGER.info(MessageFormat.format("try to insert: month: {0}, Year: {1} -> {2}", localDate.getMonth().getValue(), localDate.getYear(), solRadCrawler.getSolRadType()));
        solRadCrawler.insert(solRadRepository, fileReader, localDate.getMonth().getValue(), localDate.getYear());
    }

    private void insertAll() {
        LocalDate localDate = LocalDate.now();
        if (System.getenv("INSERT_ALL").equals("parallel")) {
            IntStream.range(getStartYear(), getEndYear(localDate)).parallel().forEach(this::insertYear);
        } else {
            IntStream.range(getStartYear(), getEndYear(localDate)).sequential().forEach(this::insertYear);
        }
    }

    private void insertYear(int year) {
        try {
            for (int month = 1; month < 13; month++) {
                LOGGER.info(MessageFormat.format("try to insert: month: {0}, Year: {1} -> {2}", month, year, solRadCrawler.getSolRadType()));
                solRadCrawler.insert(solRadRepository, fileReader, month, year);
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
        }
    }

    private int getEndYear(LocalDate localDate) {
        return localDate.getYear() + 1;
    }

    private int getStartYear() {
        if (solRadCrawler.getSolRadType() == SolRadTypes.GLOBAL) {
            return 1991;
        }
        return 2015;
    }
}
