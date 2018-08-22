package com.creditsuisse;
import com.creditsuisse.service.LogEventProcessor;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ApplicationTest {
    private LogEventProcessor logEventProcessor;

    private Application application;
    private File tempFile;

    @Before
    public void setup() throws Exception {
        logEventProcessor = mock(LogEventProcessor.class);
        application = new Application(logEventProcessor);
        tempFile = File.createTempFile(RandomStringUtils.randomAlphabetic(16), ".tmp");
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldErrorOnNoArg() throws Exception {
        application.run().run();

        verify(logEventProcessor, never()).processFileLines(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldErrorIfPathDoesNotExist() throws Exception {
        application.run().run("bad path");

        verify(logEventProcessor, never()).processFileLines(any());
    }

    @Test
    public void itShouldProcessFileLinesIfPathExists() throws Exception {
        application.run().run(tempFile.getAbsolutePath());

        verify(logEventProcessor, times(1)).processFileLines(any());
    }
}
