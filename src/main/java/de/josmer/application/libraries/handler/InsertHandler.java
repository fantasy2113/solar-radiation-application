package de.josmer.application.libraries.handler;

import de.josmer.application.libraries.crawler.RadiationCrawler;
import de.josmer.application.libraries.enums.RadTypes;
import de.josmer.application.libraries.utils.FileReader;
import de.josmer.application.repositories.SolRadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class InsertHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertHandler.class.getName());
    private final RadTypes radType;

    public InsertHandler(RadTypes radType) {
        this.radType = radType;
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

    private void insert() {
        LocalDate localDate = LocalDate.now();
        if (localDate.getDayOfMonth() < 15) {
            localDate = localDate.minusMonths(2);
        } else {
            localDate = localDate.minusMonths(1);
        }
        LOGGER.info(MessageFormat.format("try to insert: month: {0}, Year: {1} -> {2}", localDate.getMonth().getValue(), localDate.getYear(), radType)); // NOSONAR
        RadiationCrawler radiationCrawler = new RadiationCrawler(localDate.getMonth().getValue(), localDate.getYear(), radType);
        radiationCrawler.insert(new SolRadRepository(), new FileReader());
    }

    public void start() {
        ScheduledExecutorService tokenService = Executors.newScheduledThreadPool(1);
        long midnight = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES);
        tokenService.scheduleAtFixedRate(this, midnight, 1440, TimeUnit.MINUTES);
    }

    private void insertAll() {
        LocalDate localDate = LocalDate.now();
        for (int year = 1991; year < localDate.getYear(); year++) {
            for (int month = 1; month < 13; month++) {
                LOGGER.info(">>> Month: " + month + ", Year: " + year);
                RadiationCrawler radiationCrawler = new RadiationCrawler(month, year, radType);
                radiationCrawler.insert(new SolRadRepository(), new FileReader());
            }
        }
    }
}
