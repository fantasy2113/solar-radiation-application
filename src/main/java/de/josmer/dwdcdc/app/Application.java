package de.josmer.dwdcdc.app;

import de.josmer.dwdcdc.app.repositories.SolRadRepository;
import de.josmer.dwdcdc.app.spring.AppBeans;
import de.josmer.dwdcdc.app.spring.AppContext;
import de.josmer.dwdcdc.app.utils.FileReader;
import de.josmer.dwdcdc.library.handler.SolRadInsertAllHandler;
import de.josmer.dwdcdc.library.handler.SolRadInsertAtMidnightHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getName());
    private static boolean test = false;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public static void setTest(boolean test) {
        Application.test = test;
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            LOGGER.info("Let's inspect the beans provided by Spring Boot:");
            Arrays.stream(ctx.getBeanDefinitionNames()).sorted().forEachOrdered(LOGGER::info);
        };
    }

    @Bean
    public void startInsertHandler() {
        if (Application.test) {
            return;
        }
        if (isInsertAll()) {
            startInsertAllHandler();
        } else {
            startInsertAtMidnight();
        }
    }

    private boolean isInsertAllParallel() {
        return isInsertAll() && System.getenv("INSERT_ALL").equals("parallel");
    }

    private boolean isInsertAll() {
        return System.getenv("INSERT_ALL") != null;
    }

    private void startInsertAtMidnight() {
        new SolRadInsertAtMidnightHandler(AppContext.getCrawler(AppBeans.CRAWLER_GLOBAL),
                AppContext.getBean(SolRadRepository.class),
                AppContext.getBean(FileReader.class)).start();

        new SolRadInsertAtMidnightHandler(AppContext.getCrawler(AppBeans.CRAWLER_DIRECT),
                AppContext.getBean(SolRadRepository.class),
                AppContext.getBean(FileReader.class)).start();

        new SolRadInsertAtMidnightHandler(AppContext.getCrawler(AppBeans.CRAWLER_DIFFUSE),
                AppContext.getBean(SolRadRepository.class),
                AppContext.getBean(FileReader.class)).start();
    }

    private void startInsertAllHandler() {
        final boolean parallel = isInsertAllParallel();
        new SolRadInsertAllHandler(AppContext.getCrawler(AppBeans.CRAWLER_GLOBAL),
                AppContext.getBean(SolRadRepository.class),
                AppContext.getBean(FileReader.class), parallel).start();

        new SolRadInsertAllHandler(AppContext.getCrawler(AppBeans.CRAWLER_DIRECT),
                AppContext.getBean(SolRadRepository.class),
                AppContext.getBean(FileReader.class), parallel).start();

        new SolRadInsertAllHandler(AppContext.getCrawler(AppBeans.CRAWLER_DIFFUSE),
                AppContext.getBean(SolRadRepository.class),
                AppContext.getBean(FileReader.class), parallel).start();
    }
}
