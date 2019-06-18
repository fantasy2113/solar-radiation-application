package de.josmer.dwdcdc.utils.handler;

import de.josmer.dwdcdc.app.interfaces.ISolRadRepository;
import de.josmer.dwdcdc.utils.interfaces.IDataReader;
import de.josmer.dwdcdc.utils.interfaces.ISolRadCrawler;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class SolRadInsertAtMidnightHandler extends SolRadHandler {

    public SolRadInsertAtMidnightHandler(ISolRadCrawler solRadCrawler, ISolRadRepository solRadRepository, IDataReader fileReader) {
        super(solRadCrawler, solRadRepository, fileReader);
        super.started = false;
    }

    @Override
    protected void startHandler() {
        try {
            started = true;
            ScheduledExecutorService tokenService = Executors.newScheduledThreadPool(1);
            long midnight = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES);
            tokenService.scheduleAtFixedRate(this, midnight, 1440, TimeUnit.MINUTES);
            LOGGER.info(solRadCrawler.getSolRadType() + " - start");
        } catch (Exception e) {
            LOGGER.info(e.toString());
        }
    }

    @Override
    protected void insert() {
        LocalDate localDate = getLocalDate();
        LOGGER.info(MessageFormat.format("try to insert: month: {0}, Year: {1} -> {2}", localDate.getMonth().getValue(), localDate.getYear(), solRadCrawler.getSolRadType()));
        solRadCrawler.insert(solRadRepository, fileReader, localDate.getMonth().getValue(), localDate.getYear());
    }
}
