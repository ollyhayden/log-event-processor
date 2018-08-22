package com.creditsuisse.service;

import com.creditsuisse.model.LogEventLine;
import com.creditsuisse.repository.LogEventRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.stream.Stream;

public abstract class LogEventProcessor {
    private static final Logger log = LoggerFactory.getLogger(LogEventProcessor.class);

    LogEventRepository logEventRepository;
    private final static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()) // add module to manage long -> Instant
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false ); // support MS timestamps
    }

    @Autowired
    public LogEventProcessor(LogEventRepository logEventRepository) {
        this.logEventRepository = logEventRepository;
    }

    abstract public void processFileLines(Stream<String> logEventStringStream);

    public long getTotalNumberOfEvents() {
        return logEventRepository.count();
    }

    Optional<LogEventLine> jsonToLogEvent(String logEventJson) {
        try {
            return Optional.ofNullable(objectMapper.readValue(logEventJson, LogEventLine.class));
        } catch (Exception ex) {
            log.warn(String.format("Skipping invalid log event string. Could not parse [%s]", logEventJson), ex);
            return Optional.empty();
        }
    }
}
