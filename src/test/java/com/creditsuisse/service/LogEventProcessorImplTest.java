package com.creditsuisse.service;

import com.creditsuisse.model.db.LogEvent;
import com.creditsuisse.repository.LogEventRepository;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public abstract class LogEventProcessorImplTest {
    abstract LogEventProcessor getLogEventProcessor();
    abstract LogEventRepository getLogEventRepository();

    @Test
    public void itProcessesEventLinesSuccessfully() throws Exception {
        LogEventProcessor logEventProcessor = getLogEventProcessor();
        LogEventRepository logEventRepository = getLogEventRepository();

        ClassLoader classLoader = getClass().getClassLoader();
        try (Stream<String> stream = Files.lines(Paths.get(classLoader.getResource("sample-small.log").toURI()))) {
            logEventProcessor.processFileLines(stream);
        }

        verify(logEventRepository, times(1))
                .save(new LogEvent("scsmbstgra", 5l, "APPLICATION_LOG", "12345"));

        verify(logEventRepository, times(1))
                .save(new LogEvent("scsmbstgrb", 3l, null, null));

        verify(logEventRepository, times(1))
                .save(new LogEvent("scsmbstgrc", 8l, null, null));
    }
}
