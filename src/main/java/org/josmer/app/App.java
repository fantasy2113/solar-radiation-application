package org.josmer.app;

import org.josmer.app.core.RadiationTypes;
import org.josmer.app.crawler.RadiationCrawler;
import org.josmer.app.repository.RadiationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@ComponentScan
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        if (!new RadiationRepository().isConnected()) {
            System.err.println("no db connection");
            return;
        }
        insertData(true);
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

    private static void insertData(boolean isInsert) {
        if (!isInsert) {
            return;
        }
        for (int year = 2018; year < 2019; year++) {
            for (int month = 1; month < 13; month++) {
                System.out.println(">>> Month: " + month + ", Year: " + year);
                RadiationCrawler radiationCrawler = new RadiationCrawler(month, year, RadiationTypes.GLOBAL);
                radiationCrawler.download();
                radiationCrawler.unzip();
                radiationCrawler.insert();
                radiationCrawler.delete();
                System.out.println();
            }
        }
    }

}
