package de.jos.dwdcdc.app.spring;

import de.jos.dwdcdc.app.repositories.SolRadRepository;
import de.jos.dwdcdc.app.utils.FileReader;
import de.jos.dwdcdc.library.handler.SolRadInsertAllHandler;
import de.jos.dwdcdc.library.handler.SolRadInsertAtMidnightHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadData {

  public LoadData() {
    runDataLoading();
  }

  private void initInsertAllHandler(final boolean parallel, final String crawler) {
    new SolRadInsertAllHandler(AppContext.getCrawler(crawler), AppContext.getBean(SolRadRepository.class),
        AppContext.getBean(FileReader.class), parallel).start();
  }

  private void initInsertAtMidnight(final String crawler) {
    new SolRadInsertAtMidnightHandler(AppContext.getCrawler(crawler), AppContext.getBean(SolRadRepository.class),
        AppContext.getBean(FileReader.class)).start();
  }

  private boolean isInsertAll() {
    return System.getenv("INSERT_ALL") != null;
  }

  private boolean isInsertAllParallel() {
    return isInsertAll() && System.getenv("INSERT_ALL").equals("parallel");
  }

  private void runInsertAllHandler() {
    initInsertAllHandler(isInsertAllParallel(), AppBeans.CRAWLER_GLOBAL);
    initInsertAllHandler(isInsertAllParallel(), AppBeans.CRAWLER_DIRECT);
    initInsertAllHandler(isInsertAllParallel(), AppBeans.CRAWLER_DIFFUSE);
  }

  private void rundInsertAtMidnight() {
    initInsertAtMidnight(AppBeans.CRAWLER_GLOBAL);
    initInsertAtMidnight(AppBeans.CRAWLER_DIRECT);
    initInsertAtMidnight(AppBeans.CRAWLER_DIFFUSE);
  }

  private void runDataLoading() {
    if (isInsertAll()) {
      runInsertAllHandler();
    } else {
      rundInsertAtMidnight();
    }
  }
}
