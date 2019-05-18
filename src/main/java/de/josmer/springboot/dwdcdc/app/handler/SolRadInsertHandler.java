package de.josmer.springboot.dwdcdc.app.handler;

import de.josmer.springboot.dwdcdc.app.crawler.SolRadCrawler;
import de.josmer.springboot.dwdcdc.app.enums.SolRadTypes;
import de.josmer.springboot.dwdcdc.app.interfaces.IFileReader;
import de.josmer.springboot.dwdcdc.app.interfaces.ISolRadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class SolRadInsertHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolRadInsertHandler.class.getName());
    private final SolRadTypes solRadType;
    private final ISolRadRepository solRadRepository;
    private final IFileReader fileReader;

    public SolRadInsertHandler(SolRadTypes solRadType, ISolRadRepository solRadRepository, IFileReader fileReader) {
        this.solRadType = solRadType;
        this.solRadRepository = solRadRepository;
        this.fileReader = fileReader;
    }

    @Override
    public void run() {
        if (System.getenv("INSERT_ALL") == null) {
            insert();
        } else {
            insertAll();
        }
        System.gc();
    }

    public void start() {
        ScheduledExecutorService tokenService = Executors.newScheduledThreadPool(1);
        long midnight = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES);
        if (System.getenv("INSERT_ALL") == null) {
            tokenService.scheduleAtFixedRate(this, midnight, 1440, TimeUnit.MINUTES);
        } else {
            tokenService.execute(this);
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
        LOGGER.info(MessageFormat.format("try to insert: month: {0}, Year: {1} -> {2}", localDate.getMonth().getValue(), localDate.getYear(), solRadType));
        new SolRadCrawler(solRadType, localDate.getMonth().getValue(), localDate.getYear()).insert(solRadRepository, fileReader);
    }

    private void insertAll() {
        LocalDate localDate = LocalDate.now();
        for (int year = 1991; year < localDate.getYear() + 1; year++) {
            for (int month = 1; month < 13; month++) {
                LOGGER.info(MessageFormat.format("try to insert: month: {0}, Year: {1} -> {2}", month, year, solRadType));
                new SolRadCrawler(solRadType, month, year).insert(solRadRepository, fileReader);
            }
        }
    }
}
