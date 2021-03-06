package de.jos.dwdcdc.app.spring;

import com.google.gson.Gson;
import de.jos.dwdcdc.app.entities.SolRad;
import de.jos.dwdcdc.library.crawler.SolRadCrawler;
import de.jos.dwdcdc.library.enums.SolRadTypes;
import de.jos.dwdcdc.shared.ISolRadCrawler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBeans {

  public static final String CRAWLER_DIFFUSE = "crawlerDiffuse";
  public static final String CRAWLER_DIRECT = "crawlerDirect";
  public static final String CRAWLER_GLOBAL = "crawlerGlobal";
  public static final String IrradiationCaching = "irradiationCaching";

  @Bean
  public Gson getGson() {
    return new Gson();
  }

  @Bean(CRAWLER_DIFFUSE)
  public ISolRadCrawler getSolRadCrawlerDiffuse() {
    return new SolRadCrawler<>(SolRadTypes.DIFFUSE, SolRad.class);
  }

  @Bean(CRAWLER_DIRECT)
  public ISolRadCrawler getSolRadCrawlerDirect() {
    return new SolRadCrawler<>(SolRadTypes.DIRECT, SolRad.class);
  }

  @Bean(CRAWLER_GLOBAL)
  public ISolRadCrawler getSolRadCrawlerGlobal() {
    return new SolRadCrawler<>(SolRadTypes.GLOBAL, SolRad.class);
  }
}
