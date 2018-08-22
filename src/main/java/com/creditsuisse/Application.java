package com.creditsuisse;

import com.creditsuisse.service.LogEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.stream.Stream;

@SpringBootApplication
public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private LogEventProcessor logEventProcessor;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    public Application(@Qualifier("hashMapCacheLogEventProcessor") LogEventProcessor logEventProcessor) {
        this.logEventProcessor = logEventProcessor;
    }

    @Bean
    public CommandLineRunner run() {
        return (args) -> {
            if (args.length != 1) {
                throw new IllegalArgumentException("Path to log event file must be provided");
            }

            String logEventFilePath = args[0];
            log.info("Processing file [{}]", logEventFilePath);

            Path path = Paths.get(logEventFilePath);
            if (Files.notExists(path)) {
                throw new IllegalArgumentException(String.format("File path provided does not exist: [%s]", logEventFilePath));
            }

            processLogFile(path);

            log.info("...finished");
        };
    }

    private void processLogFile(Path logFilePath) throws IOException {
        long startTime = System.currentTimeMillis();
        try (Stream<String> stream = Files.lines(logFilePath)) {
            logEventProcessor.processFileLines(stream);
        }

        log.info(String.format("Processing took %ssecs for %s events",
                formatDecimal((System.currentTimeMillis() - startTime) / 1000f),
                formatDecimal(logEventProcessor.getTotalNumberOfEvents())));
    }

    private static final DecimalFormat decimalFormatter = new DecimalFormat("#,##0.##");
    private String formatDecimal(float value) {
        return decimalFormatter.format(value);
    }
}
