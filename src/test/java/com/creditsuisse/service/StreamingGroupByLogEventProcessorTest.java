package com.creditsuisse.service;

import com.creditsuisse.repository.LogEventRepository;
import org.junit.Before;

import static org.mockito.Mockito.mock;

public class StreamingGroupByLogEventProcessorTest extends LogEventProcessorImplTest {
    private LogEventRepository logEventRepository;
    private LogEventProcessor logEventProcessor;

    @Before
    public void setup() {
        logEventRepository = mock(LogEventRepository.class);
        logEventProcessor = new StreamingGroupByLogEventProcessor(logEventRepository);
    }

    @Override
    LogEventProcessor getLogEventProcessor() {
        return logEventProcessor;
    }

    @Override
    LogEventRepository getLogEventRepository() {
        return logEventRepository;
    }
}
