package org.josmer.application;

import org.josmer.application.handler.InsertHandler;
import org.josmer.application.repositories.RadiationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        openBrowser();
        if (false) {
            ExecutorService pool = Executors.newFixedThreadPool(1);
            pool.execute(new InsertHandler(new RadiationRepository()));
        }
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

    private static void openBrowser() {
        try {
            new ProcessBuilder(System.getenv("DEV_BROWSER"), System.getenv("DEV_URL")).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
