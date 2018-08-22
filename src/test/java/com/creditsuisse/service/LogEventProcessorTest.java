package com.creditsuisse.service;

import com.creditsuisse.model.LogEventLine;
import com.creditsuisse.repository.LogEventRepository;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LogEventProcessorTest {
    private LogEventRepository logEventRepository;
    private LogEventProcessor logEventProcessor;

    @Before
    public void setup() {
        this.logEventRepository = mock(LogEventRepository.class);
        logEventProcessor = new LogEventProcessor(logEventRepository) {
            @Override
            public void processFileLines(Stream<String> logEventStringStream) {
                // dummy
            }
        };
    }

    @Test
    public void itReturnsCountOfEvents() {
        when(logEventRepository.count()).thenReturn(123l);

        long result = logEventProcessor.getTotalNumberOfEvents();
        assertEquals(123l, result);
        verify(logEventRepository, times(1)).count();
    }

    @Test
    public void itReturnsOptionalEmptyForNullJson() {
        Optional<LogEventLine> logEventLineOptional = logEventProcessor.jsonToLogEvent(null);

        assertEquals(Optional.empty(), logEventLineOptional);
    }

    @Test
    public void itReturnsOptionalEmptyForBadJson() {
        Optional<LogEventLine> logEventLineOptional = logEventProcessor.jsonToLogEvent("eh?");

        assertEquals(Optional.empty(), logEventLineOptional);
    }

    @Test
    public void itReturnsLogEventForGoodJson() {
        Optional<LogEventLine> logEventLineOptional =
                logEventProcessor.jsonToLogEvent("{\"id\":\"scsmbstgrb\", \"state\":\"STARTED\", \"timestamp\":1491377495213}");

        assertEquals(Optional.of(new LogEventLine("scsmbstgrb", "STARTED", Instant.ofEpochMilli(1491377495213l))), logEventLineOptional);
    }

    @Test
    public void itReturnsApplicationLogEventForGoodJson() {
        Optional<LogEventLine> logEventLineOptional =
                logEventProcessor.jsonToLogEvent("{\"id\":\"scsmbstgra\", \"state\":\"FINISHED\", \"type\":\"APPLICATION_LOG\",\"host\":\"12345\", \"timestamp\":1491377495217}");

        assertEquals(Optional.of(new LogEventLine("scsmbstgra", "FINISHED",
                Instant.ofEpochMilli(1491377495217l), "APPLICATION_LOG", "12345")), logEventLineOptional);
    }
}
