package com.creditsuisse.service;

import com.creditsuisse.model.db.LogEvent;
import com.creditsuisse.repository.LogEventRepository;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

// TODO write unit test
public class StreamingGroupByLogEventProcessorTest {
    private LogEventRepository logEventRepository;
    private LogEventProcessor logEventProcessor;

    @Before
    public void setup() {
        logEventRepository = mock(LogEventRepository.class);
        logEventProcessor = new StreamingGroupByLogEventProcessor(logEventRepository);
    }

    @Test
    public void itProcessesEventLinesSuccessfully() throws Exception {
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
