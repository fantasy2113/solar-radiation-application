package de.josmer.dwdcdc.app;

import de.josmer.dwdcdc.app.repositories.SolRadRepository;
import de.josmer.dwdcdc.app.spring.Beans;
import de.josmer.dwdcdc.app.spring.Context;
import de.josmer.dwdcdc.app.utils.FileReader;
import de.josmer.dwdcdc.utils.handler.SolRadInsertHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public void startInsertHandler() {
        new SolRadInsertHandler(
                Context.getCrawler(Beans.CRAWLER_GLOBAL),
                Context.getBean(SolRadRepository.class),
                Context.getBean(FileReader.class)
        ).start();

        new SolRadInsertHandler(
                Context.getCrawler(Beans.CRAWLER_DIRECT),
                Context.getBean(SolRadRepository.class),
                Context.getBean(FileReader.class)
        ).start();

        new SolRadInsertHandler(
                Context.getCrawler(Beans.CRAWLER_DIFFUSE),
                Context.getBean(SolRadRepository.class),
                Context.getBean(FileReader.class)
        ).start();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            LOGGER.info("Let's inspect the beans provided by Spring Boot:");
            Arrays.stream(ctx.getBeanDefinitionNames()).sorted().forEachOrdered(LOGGER::info);
        };
    }
}
