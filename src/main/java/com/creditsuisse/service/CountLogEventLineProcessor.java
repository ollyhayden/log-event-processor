package com.creditsuisse.service;

import com.creditsuisse.repository.LogEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CountLogEventLineProcessor extends LogEventProcessor {
    private static final Logger log = LoggerFactory.getLogger(CountLogEventLineProcessor.class);

    @Autowired
    public CountLogEventLineProcessor(LogEventRepository logEventRepository) {
        super(logEventRepository);
    }

    public void processFileLines(Stream<String> logEventStringStream) {
        log.info("Processing file lines using StdoutLogEventProcessor");

        log.info(logEventStringStream
                .map(this::jsonToLogEvent)
                .filter(Optional::isPresent)
                .map(Optional::get).count() + " events converted to LogEventLine");
    }
}
