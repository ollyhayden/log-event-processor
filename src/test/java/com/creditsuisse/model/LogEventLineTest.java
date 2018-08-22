package com.creditsuisse.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LogEventLineTest {
    @Test
    public void itDetectsAnyStateAsNotFinished() {
        LogEventLine logEventLine = new LogEventLine();

        logEventLine.setState("anything");
        assertFalse(logEventLine.isFinishedEvent());

        logEventLine.setState("STARTED");
        assertFalse(logEventLine.isFinishedEvent());
    }

    @Test
    public void itDetectsStateFinished() {
        LogEventLine logEventLine = new LogEventLine();
        logEventLine.setState("FINISHED");

        assertTrue(logEventLine.isFinishedEvent());
    }
}
