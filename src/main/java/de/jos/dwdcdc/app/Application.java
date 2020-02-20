package de.jos.dwdcdc.app;

import de.jos.dwdcdc.app.repositories.SolRadRepository;
import de.jos.dwdcdc.app.spring.AppBeans;
import de.jos.dwdcdc.app.spring.AppContext;
import de.jos.dwdcdc.app.utils.FileReader;
import de.jos.dwdcdc.library.handler.SolRadInsertAllHandler;
import de.jos.dwdcdc.library.handler.SolRadInsertAtMidnightHandler;
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
    private static boolean junitTest = false;
    private static boolean demoMode = false;
    private static final String test = Application.isDemoMode() ? "IrradiationRamCaching" : "IrradiationRamCaching";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public static void setJunitTest(boolean junitTest) {
        Application.junitTest = junitTest;
    }

    //@Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            LOGGER.info("Let's inspect the beans provided by Spring Boot:");
            Arrays.stream(ctx.getBeanDefinitionNames()).sorted().forEachOrdered(LOGGER::info);
        };
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

    private void startInsertAllHandler() {
        initInsertAllHandler(isInsertAllParallel(), AppBeans.CRAWLER_GLOBAL);
        initInsertAllHandler(isInsertAllParallel(), AppBeans.CRAWLER_DIRECT);
        initInsertAllHandler(isInsertAllParallel(), AppBeans.CRAWLER_DIFFUSE);
    }

    private void startInsertAtMidnight() {
        initInsertAtMidnight(AppBeans.CRAWLER_GLOBAL);
        initInsertAtMidnight(AppBeans.CRAWLER_DIRECT);
        initInsertAtMidnight(AppBeans.CRAWLER_DIFFUSE);
    }

    public static boolean isDemoMode() {
        return demoMode;
    }

    @Bean
    public void startInsertHandler() {
        if (junitTest || demoMode) {
            return;
        }
        if (isInsertAll()) {
            startInsertAllHandler();
        } else {
            startInsertAtMidnight();
        }
    }
}
