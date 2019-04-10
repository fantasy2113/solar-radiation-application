package de.josmer.application.handler;


import de.josmer.application.crawler.RadiationCrawler;
import de.josmer.application.enums.RadiationTypes;
import de.josmer.application.repositories.RadiationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class InsertHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsertHandler.class.getName());
    private final RadiationTypes radiationTypes;

    public InsertHandler(RadiationTypes radiationTypes) {
        this.radiationTypes = radiationTypes;
    }

    public void start() {
        ScheduledExecutorService tokenService = Executors.newScheduledThreadPool(1);
        long midnight = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES);
        tokenService.scheduleAtFixedRate(this, midnight, 1440, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        LocalDate localDate = LocalDate.now();
        if (localDate.getDayOfMonth() < 15) {
            localDate = localDate.minusMonths(2);
        } else {
            localDate = localDate.minusMonths(1);
        }
        LOGGER.info("try to insert: month: " + localDate.getMonth().getValue() + ", Year: " + localDate.getYear() + " -> " + radiationTypes);
        RadiationCrawler radiationCrawler = new RadiationCrawler(localDate.getMonth().getValue(), localDate.getYear(), radiationTypes);
        radiationCrawler.download();
        radiationCrawler.unzip();
        radiationCrawler.insert(new RadiationRepository());
        radiationCrawler.delete();
    }
}
