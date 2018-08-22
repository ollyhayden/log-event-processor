package com.creditsuisse.model.db;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class LogEventTest {
    @Test
    public void itSetsAlertIfDurationGreaterThanThreshold() {
        LogEvent logEvent = new LogEvent();
        assertNull(logEvent.getAlert());

        logEvent.setDuration(4l);
        assertFalse(logEvent.getAlert());

        logEvent.setDuration(5l);
        assertTrue(logEvent.getAlert());
    }

    @Test
    public void itDoesntTakeAlertFromSetter() {
        LogEvent logEvent = new LogEvent();
        assertNull(logEvent.getAlert());

        logEvent.setAlert(true);
        assertNull(logEvent.getAlert());
    }
}
