package de.josmer.springboot.dwdcdc.app;

import de.josmer.springboot.dwdcdc.app.crawler.RadTypes;
import de.josmer.springboot.dwdcdc.app.crawler.SolRadCrawler;
import de.josmer.springboot.dwdcdc.app.handler.SolRadInsertHandler;
import de.josmer.springboot.dwdcdc.app.repositories.SolRadRepository;
import de.josmer.springboot.dwdcdc.app.utils.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@SpringBootApplication
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        startInsertHandler();
    }

    private static void startInsertHandler() {
        new SolRadInsertHandler(RadTypes.GLOBAL, new SolRadRepository(), new FileReader(), new SolRadCrawler()).start();
        new SolRadInsertHandler(RadTypes.DIFFUSE, new SolRadRepository(), new FileReader(), new SolRadCrawler()).start();
        new SolRadInsertHandler(RadTypes.DIRECT, new SolRadRepository(), new FileReader(), new SolRadCrawler()).start();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            LOGGER.info("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                LOGGER.info(beanName);
            }
        };
    }
}
