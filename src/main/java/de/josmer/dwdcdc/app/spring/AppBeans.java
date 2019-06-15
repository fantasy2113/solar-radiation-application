package de.josmer.dwdcdc.app.spring;

import de.josmer.dwdcdc.app.entities.SolRad;
import de.josmer.dwdcdc.utils.crawler.SolRadCrawler;
import de.josmer.dwdcdc.utils.enums.SolRadTypes;
import de.josmer.dwdcdc.utils.interfaces.ISolRadCrawler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBeans {

    @Bean("crawlerGlobal")
    public ISolRadCrawler getSolRadCrawlerGlobal() {
        return new SolRadCrawler<>(SolRadTypes.GLOBAL, SolRad.class);
    }

    @Bean("crawlerDirect")
    public ISolRadCrawler getSolRadCrawlerDirect() {
        return new SolRadCrawler<>(SolRadTypes.DIRECT, SolRad.class);
    }

    @Bean("crawlerDiffuse")
    public ISolRadCrawler getSolRadCrawlerDiffuse() {
        return new SolRadCrawler<>(SolRadTypes.DIFFUSE, SolRad.class);
    }
}
