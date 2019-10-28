package de.josmer.dwdcdc.app.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;

import de.josmer.dwdcdc.app.entities.SolRad;
import de.josmer.dwdcdc.library.crawler.SolRadCrawler;
import de.josmer.dwdcdc.library.enums.SolRadTypes;
import de.josmer.dwdcdc.library.interfaces.ISolRadCrawler;

@Configuration
public class AppBeans {

	public static final String CRAWLER_DIFFUSE = "crawlerDiffuse";
	public static final String CRAWLER_DIRECT = "crawlerDirect";
	public static final String CRAWLER_GLOBAL = "crawlerGlobal";

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
