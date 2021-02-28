package de.jos.dwdcdc.library.handler;

import de.jos.dwdcdc.library.enums.SolRadTypes;
import de.jos.dwdcdc.shared.IBasicSolRad;
import de.jos.dwdcdc.shared.IDataReader;
import de.jos.dwdcdc.shared.IHandler;
import de.jos.dwdcdc.shared.ISolRadCrawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.time.LocalDate;

abstract class SolRadHandler implements IHandler {

  static final Logger LOGGER = LoggerFactory.getLogger(SolRadHandler.class.getName());
  final IDataReader fileReader;
  final ISolRadCrawler solRadCrawler;
  final IBasicSolRad solRadRepository;
  private boolean started;

  SolRadHandler(ISolRadCrawler solRadCrawler, IBasicSolRad solRadRepository, IDataReader fileReader) {
    this.solRadCrawler = solRadCrawler;
    this.solRadRepository = solRadRepository;
    this.fileReader = fileReader;
    this.started = false;
  }

  int getEndYear(LocalDate localDate) {
    return localDate.getYear() + 1;
  }

  LocalDate getLocalDate() {
    LocalDate localDate = LocalDate.now();
    return localDate.getDayOfMonth() < 15 ? localDate.minusMonths(2) : localDate.minusMonths(1);
  }

  int getStartYear() {
    return solRadCrawler.getSolRadType() == SolRadTypes.GLOBAL ? 1991 : 2015;
  }

  private void handlerStarted() {
    this.started = true;
  }

  boolean isStarted() {
    return started;
  }

  void logInser(int year, int month) {
    LOGGER.info(MessageFormat.format("Try to insert: month={0}, year={1}, type={2}", month, year,
        solRadCrawler.getSolRadType()));
  }

  void logStart() {
    LOGGER.info(solRadCrawler.getSolRadType() + " - started");
  }

  @Override
  public void run() {
    try {
      runInsert();
    } catch (Exception e) {
      LOGGER.info(e.toString());
    }
  }

  protected abstract void runInsert();

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

  protected abstract void startHandler();
}
