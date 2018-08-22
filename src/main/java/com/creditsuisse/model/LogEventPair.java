package com.creditsuisse.model;

import com.creditsuisse.model.db.LogEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public class LogEventPair {
    private LogEventLine startLogEvent;
    private LogEventLine finishLogEvent;

    public static LogEventPair createLogEventPair(LogEventLine... logEvents) {
        return createLogEventPair(Arrays.asList(logEvents));
    }

    public static LogEventPair createLogEventPair(List<LogEventLine> logEvents) {
        if (logEvents != null && logEvents.size() != 2) {
            throw new RuntimeException(String.format("Expected 2 events but found %s.", logEvents.size()));
        }

        LogEventLine logEvent1 = logEvents.get(0);
        LogEventLine logEvent2 = logEvents.get(1);
        if (logEvent1.isFinishedEvent()==logEvent2.isFinishedEvent()) {
            throw new RuntimeException("Unexpected - need one finished event in " + logEvents);
        }

        return new LogEventPair(
                logEvent1.isFinishedEvent() ? logEvent2 : logEvent1,
                logEvent1.isFinishedEvent() ? logEvent1 : logEvent2);
    }

    public LogEvent asLogEvent() {
        return new LogEvent(finishLogEvent.getId(), getTimeTaken(), finishLogEvent.getType(), finishLogEvent.getHost());
    }

    private Long getTimeTaken() {
        if (startLogEvent == null || finishLogEvent == null) { return null; }

        return Duration.between(startLogEvent.getTimestamp(), finishLogEvent.getTimestamp()).toMillis();
    }
}
