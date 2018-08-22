package com.creditsuisse.service;

import com.creditsuisse.model.LogEventLine;
import com.creditsuisse.model.LogEventPair;
import com.creditsuisse.model.db.LogEvent;
import com.creditsuisse.repository.LogEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HashMapCacheLogEventProcessor extends LogEventProcessor {
    private static final Logger log = LoggerFactory.getLogger(HashMapCacheLogEventProcessor.class);

    @Autowired
    public HashMapCacheLogEventProcessor(LogEventRepository logEventRepository) {
        super(logEventRepository);
    }

    public void processFileLines(Stream<String> logEventStringStream) {
        log.info("Processing file lines using HashMapCacheLogEventProcessor");
        Stream<LogEventLine> logEventStream =
                logEventStringStream
                        .map(this::jsonToLogEvent)
                        .filter(Optional::isPresent)
                        .map(Optional::get);

        processLogEvents(logEventStream);
    }

    private void processLogEvents(Stream<LogEventLine> logEventPairStream) {
        Map<String, LogEventLine> logEventLineCache = new HashMap<>();

        // store list for id - once we have two, write it to DB and remove from hashmap
        logEventPairStream.forEach((l) -> {
            // if we have a key - this must be the other event in the pair
            if (logEventLineCache.containsKey(l.getId())) {
                LogEvent logEvent = LogEventPair
                        .createLogEventPair(logEventLineCache.get(l.getId()), l) // add cached event +
                        .asLogEvent();
                logEventRepository.save(logEvent);

                // clear from the cache now that we've added the pair
                logEventLineCache.remove(l.getId());
            } else {
                logEventLineCache.put(l.getId(), l);
            }
        });
    }
}
