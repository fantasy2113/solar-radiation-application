package de.jos.dwdcdc.library.handler;

import de.jos.dwdcdc.share.IBasicSolRad;
import de.jos.dwdcdc.share.IDataReader;
import de.jos.dwdcdc.share.ISolRadCrawler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class SolRadInsertAtMidnightHandler extends SolRadHandler {

  public SolRadInsertAtMidnightHandler(ISolRadCrawler solRadCrawler, IBasicSolRad solRadRepository,
                                       IDataReader fileReader) {
    super(solRadCrawler, solRadRepository, fileReader);
  }

  @Override
  protected void runInsert() {
    LocalDate date = getLocalDate();
    logInser(date.getYear(), date.getMonth().getValue());
    solRadCrawler.insert(solRadRepository, fileReader, date.getMonth().getValue(), date.getYear());
  }

  @Override
  protected void startHandler() {
    ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    long midnight = LocalDateTime.now().until(LocalDate.now().plusDays(1).atStartOfDay(), ChronoUnit.MINUTES);
    service.scheduleAtFixedRate(this, midnight, 1440, TimeUnit.MINUTES);
    logStart();
  }
}
