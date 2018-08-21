package com.creditsuisse.model;

import lombok.Data;

import java.time.Instant;

@Data
public class LogEventLine {
    private String id;
    private String state;
    private Instant timestamp;

    // additional application log fields
    private String type;
    private String host;

    public boolean isFinishedEvent() {
        return "FINISHED".equals(state);
    }
}
