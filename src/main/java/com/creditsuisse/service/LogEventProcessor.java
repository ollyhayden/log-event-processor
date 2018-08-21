package com.creditsuisse.service;

import com.creditsuisse.model.LogEventLine;
import com.creditsuisse.model.LogEventPair;
import com.creditsuisse.repository.LogEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LogEventProcessor {
    private static final Logger log = LoggerFactory.getLogger(LogEventProcessor.class);

    private LogEventRepository logEventRepository;
    private final static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule()); // add module to manage long -> Instant
    }

    @Autowired
    public LogEventProcessor(LogEventRepository logEventRepository) {
        this.logEventRepository = logEventRepository;
    }

    public void processFileLines(Stream<String> logEventStringStream) {
        Stream<LogEventLine> logEventStream =
                logEventStringStream
                        .map(this::jsonToLogEvent)
                        .filter(Optional::isPresent)
                        .map(Optional::get);

        processLogEvents(logEventStream);
    }

    private void processLogEvents(Stream<LogEventLine> logEventPairStream) {
        logEventPairStream
                .collect(Collectors.groupingBy(LogEventLine::getId))
                .forEach((key, events) -> {
                    if (events.size()==2) {
                        logEventRepository.save(LogEventPair
                                .createLogEventPair(events)
                                .asLogEvent());
                    } else {
                        log.warn(String.format("Expected 2 events for %s. Found %s, Skipping.", key, events.size()));
                    }
                });

        System.out.println(logEventRepository.findAll());
    }

    private Optional<LogEventLine> jsonToLogEvent(String logEventJson) {
        try {
            return Optional.ofNullable(objectMapper.readValue(logEventJson, LogEventLine.class));
        } catch (Exception ex) {
            log.warn(String.format("Skipping invalid log event string. Could not parse [%s]", logEventJson), ex);
            return Optional.empty();
        }
    }
}
