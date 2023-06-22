package com.poc;

import com.poc.model.Transaction;
import com.poc.service.CategorizationService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class MainApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(MainApplication.class);
        springApplication.run(args);
    }

    public void run(ApplicationArguments args) throws Exception {
        System.out.println("############################   RUN   ############################");

        System.out.println();

        long start = System.currentTimeMillis();
        var categories = Stream.of(
                        new Transaction("1", "description 1"),
                        new Transaction("2", "description 2"),
                        new Transaction("3", "description 3"))
                .map(CategorizationService::categorizeTransaction)
                .toList();

        long end = System.currentTimeMillis();

        System.out.printf("----- The operation took %s ms%n", end - start);
        System.out.println("----- Categories are : " + categories);

        System.out.println();

        // Parallel Stream Implementation
        System.out.println("****** Parallel Stream Implementation ******");

        long startParallel = System.currentTimeMillis();
        var categoriesParallel = Stream.of(
                        new Transaction("1", "description 1"),
                        new Transaction("2", "description 2"),
                        new Transaction("3", "description 3"))
                .parallel()
                .map(CategorizationService::categorizeTransaction)
                .toList();

        long endParallel = System.currentTimeMillis();

        System.out.printf("----- The operation took %s ms%n", endParallel - startParallel);
        System.out.println("----- Categories are : " + categoriesParallel);

        System.out.println();

        // Increasing Performance Using CompletableFuture
        System.out.println("****** Increasing Performance Using CompletableFuture ******");

        Executor executor = Executors.newFixedThreadPool(20);
        long startExecutor = System.currentTimeMillis();
        var futureCategories = Stream.of(
                        new Transaction("1", "description 1"),
                        new Transaction("2", "description 2"),
                        new Transaction("3", "description 3"),
                        new Transaction("4", "description 4"),
                        new Transaction("5", "description 5"),
                        new Transaction("6", "description 6"),
                        new Transaction("7", "description 7"),
                        new Transaction("8", "description 8"),
                        new Transaction("9", "description 9"),
                        new Transaction("10", "description 10")
                )
                .map(transaction -> CompletableFuture.supplyAsync(
                        () -> CategorizationService.categorizeTransaction(transaction), executor)
                )
                .toList();

        var categoriesExecutor = futureCategories.stream()
                .map(CompletableFuture::join)
                .toList();

        long endExecutor = System.currentTimeMillis();

        System.out.printf("----- The operation took %s ms%n", endExecutor - startExecutor);
        System.out.println("----- Categories are : " + categoriesExecutor);

    }
}
