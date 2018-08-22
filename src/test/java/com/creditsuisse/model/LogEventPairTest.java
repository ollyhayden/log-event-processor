package com.creditsuisse.model;

import com.creditsuisse.model.db.LogEvent;
import org.junit.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LogEventPairTest {
    @Test
    public void itSuccessfullyDetectsStartAndFinishLogEventLines() {
        List<LogEventLine> logEventLines = new ArrayList<>();
        LogEventLine startLogEventLine = new LogEventLine("dummyId", "STARTED", Instant.now());
        logEventLines.add(startLogEventLine);

        LogEventLine finishLogEventLine = new LogEventLine("dummyId", "FINISHED", Instant.now());
        logEventLines.add(finishLogEventLine);

        LogEventPair logEventPair = LogEventPair.createLogEventPair(logEventLines);
        assertEquals(startLogEventLine, logEventPair.getStartLogEvent());
        assertEquals(finishLogEventLine, logEventPair.getFinishLogEvent());
    }

    @Test
    public void itSuccessfullyDetectsStartAndFinishLogEventLinesEvenIfWrongOrder() {
        List<LogEventLine> logEventLines = new ArrayList<>();

        LogEventLine finishLogEventLine = new LogEventLine("dummyId", "FINISHED", Instant.now());
        logEventLines.add(finishLogEventLine);

        LogEventLine startLogEventLine = new LogEventLine("dummyId", "STARTED", Instant.now());
        logEventLines.add(startLogEventLine);

        LogEventPair logEventPair = LogEventPair.createLogEventPair(logEventLines);
        assertEquals(startLogEventLine, logEventPair.getStartLogEvent());
        assertEquals(finishLogEventLine, logEventPair.getFinishLogEvent());
    }

    @Test(expected = RuntimeException.class)
    public void itThrowsErrorIfTooFewLines() {
        List<LogEventLine> logEventLines = new ArrayList<>();

        logEventLines.add(new LogEventLine("dummyId", "STARTED", Instant.now()));

        LogEventPair.createLogEventPair(logEventLines);
    }

    @Test(expected = RuntimeException.class)
    public void itThrowsErrorIfTooManyLines() {
        List<LogEventLine> logEventLines = new ArrayList<>();

        logEventLines.add(new LogEventLine("dummyId", "STARTED", Instant.now()));
        logEventLines.add(new LogEventLine("dummyId", "FINISHED", Instant.now()));
        logEventLines.add(new LogEventLine("dummyId", "EXTRA", Instant.now()));

        LogEventPair.createLogEventPair(logEventLines);
    }

    @Test(expected = RuntimeException.class)
    public void itThrowsErrorIfNoFinished() {
        List<LogEventLine> logEventLines = new ArrayList<>();

        logEventLines.add(new LogEventLine("dummyId", "STARTED", Instant.now()));
        logEventLines.add(new LogEventLine("dummyId", "STARTED", Instant.now()));

        LogEventPair.createLogEventPair(logEventLines);
    }

    @Test(expected = RuntimeException.class)
    public void itThrowsErrorIfBothFinished() {
        List<LogEventLine> logEventLines = new ArrayList<>();

        logEventLines.add(new LogEventLine("dummyId", "FINISHED", Instant.now()));
        logEventLines.add(new LogEventLine("dummyId", "FINISHED", Instant.now()));

        LogEventPair.createLogEventPair(logEventLines);
    }

    @Test
    public void itCreatesLogEventWithCorrectDuration() {
        List<LogEventLine> logEventLines = new ArrayList<>();

        logEventLines.add(new LogEventLine("dummyId", "STARTED", Instant.ofEpochMilli(100)));
        logEventLines.add(new LogEventLine("dummyId", "FINISHED", Instant.ofEpochMilli(109)));

        assertEquals(LogEventPair.createLogEventPair(logEventLines).asLogEvent().getDuration().longValue(), 9l);
    }
}
