package com.creditsuisse.service;

import com.creditsuisse.model.LogEventLine;
import com.creditsuisse.model.LogEventPair;
import com.creditsuisse.repository.LogEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StreamingGroupByLogEventProcessor extends LogEventProcessor {
    private static final Logger log = LoggerFactory.getLogger(StreamingGroupByLogEventProcessor.class);

    @Autowired
    public StreamingGroupByLogEventProcessor(LogEventRepository logEventRepository) {
        super(logEventRepository);
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
    }
}
