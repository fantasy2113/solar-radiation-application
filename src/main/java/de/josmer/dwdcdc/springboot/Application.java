package de.josmer.dwdcdc.springboot;

import de.josmer.dwdcdc.springboot.base.crawler.RadTypes;
import de.josmer.dwdcdc.springboot.base.handler.InsertHandler;
import de.josmer.dwdcdc.springboot.entities.SolRad;
import de.josmer.dwdcdc.springboot.repositories.SolRadRepository;
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
public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        startInsertHandler();
    }

    private static void startInsertHandler() {
        new InsertHandler<>(RadTypes.GLOBAL, new SolRadRepository(), SolRad.class).start();
        new InsertHandler<>(RadTypes.DIFFUSE, new SolRadRepository(), SolRad.class).start();
        new InsertHandler<>(RadTypes.DIRECT, new SolRadRepository(), SolRad.class).start();
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
