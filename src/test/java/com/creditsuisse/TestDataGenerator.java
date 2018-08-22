package com.creditsuisse;

import com.creditsuisse.model.LogEventLine;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestDataGenerator {
    private static final Logger log = LoggerFactory.getLogger(TestDataGenerator.class);
    private static final ObjectMapper objectMapper;
    static {
         objectMapper = new ObjectMapper()
                 .registerModule(new JavaTimeModule())
                 .setSerializationInclusion(JsonInclude.Include.NON_NULL) // no nulls output in JSON
                 .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false );
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            log.warn("Pass in the number of event (pairs) you want to generate");
            return;
        }

        long startTimeInMs = System.currentTimeMillis();
        int numberOfEvents = Integer.parseInt(args[0]);

        Path testFilePath = Files.createFile(Paths.get(
                String.format("./sample-events_%s.log",
                        new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date()))));

        try (BufferedWriter writer = Files.newBufferedWriter(testFilePath)) {
            for (int i = 0; i < numberOfEvents; i++) {
                List<LogEventLine> lines = randomLogEventLines();
                for (LogEventLine l : lines) {
                    writeLogEventLine(writer, l);
                }
            }
        }
    }

    private static List<LogEventLine> randomLogEventLines() {
        List<LogEventLine> result = new ArrayList<>();

        String randomId = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        Instant now = Instant.ofEpochMilli(System.currentTimeMillis());

        LogEventLine startLogEventLine;
        LogEventLine finishedLogEventLine;
        if (RandomUtils.nextBoolean()) {
            startLogEventLine = new LogEventLine(randomId, LogEventLine.STATE_STARTED, now);
            finishedLogEventLine = new LogEventLine(randomId, LogEventLine.STATE_FINISHED, now);
        } else {
            final String type = "APPLICATION_LOG";
            String host = RandomStringUtils.randomNumeric(8);
            startLogEventLine = new LogEventLine(randomId, LogEventLine.STATE_STARTED, now, type, host);
            finishedLogEventLine = new LogEventLine(randomId, LogEventLine.STATE_FINISHED, now.plusMillis(RandomUtils.nextInt(1,10)), type, host);
        }

        if (RandomUtils.nextBoolean()) {
            result.add(startLogEventLine);
            result.add(finishedLogEventLine);
        } else {
            result.add(finishedLogEventLine);
            result.add(startLogEventLine);
        }

        return result;
    }

    private static void writeLogEventLine(BufferedWriter writer, LogEventLine logEventLine) throws Exception {
        writer.write(objectMapper.writeValueAsString(logEventLine));
        writer.newLine();
    }
}
