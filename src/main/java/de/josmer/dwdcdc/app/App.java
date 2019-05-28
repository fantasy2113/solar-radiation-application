package de.josmer.dwdcdc.app;

import de.josmer.dwdcdc.app.entities.SolRad;
import de.josmer.dwdcdc.app.repositories.SolRadRepository;
import de.josmer.dwdcdc.app.utils.AppContext;
import de.josmer.dwdcdc.app.utils.FileReader;
import de.josmer.dwdcdc.utils.enums.SolRadTypes;
import de.josmer.dwdcdc.utils.handler.SolRadInsertHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@SpringBootApplication
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        startInsertHandler();
    }

    private static void startInsertHandler() {
        new SolRadInsertHandler<>(SolRadTypes.GLOBAL, SolRad.class,
                AppContext.get(SolRadRepository.class),
                AppContext.get(FileReader.class))
                .start();

        new SolRadInsertHandler<>(SolRadTypes.DIFFUSE, SolRad.class,
                AppContext.get(SolRadRepository.class),
                AppContext.get(FileReader.class))
                .start();

        new SolRadInsertHandler<>(SolRadTypes.DIRECT, SolRad.class,
                AppContext.get(SolRadRepository.class),
                AppContext.get(FileReader.class))
                .start();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            LOGGER.info("Let's inspect the beans provided by Spring Boot:");
            List<String> beanNames = Arrays.asList(ctx.getBeanDefinitionNames());
            Collections.sort(beanNames);
            beanNames.stream().sequential().forEach(LOGGER::info);
        };
    }
}
