package de.josmer.dwdcdc.app.spring;

import de.josmer.dwdcdc.app.entities.SolRad;
import de.josmer.dwdcdc.utils.crawler.SolRadCrawler;
import de.josmer.dwdcdc.utils.enums.SolRadTypes;
import de.josmer.dwdcdc.utils.interfaces.ISolRadCrawler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    public static final String CRAWLER_GLOBAL = "crawlerGlobal";
    public static final String CRAWLER_DIRECT = "crawlerDirect";
    public static final String CRAWLER_DIFFUSE = "crawlerDiffuse";

    @Bean(CRAWLER_GLOBAL)
    public ISolRadCrawler getSolRadCrawlerGlobal() {
        return new SolRadCrawler<>(SolRadTypes.GLOBAL, SolRad.class);
    }

    @Bean(CRAWLER_DIRECT)
    public ISolRadCrawler getSolRadCrawlerDirect() {
        return new SolRadCrawler<>(SolRadTypes.DIRECT, SolRad.class);
    }

    @Bean(CRAWLER_DIFFUSE)
    public ISolRadCrawler getSolRadCrawlerDiffuse() {
        return new SolRadCrawler<>(SolRadTypes.DIFFUSE, SolRad.class);
    }
}