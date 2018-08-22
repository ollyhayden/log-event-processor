package com.creditsuisse.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
public class LogEventLine {
    // TODO consider enumeration in the future?
    public static final String STATE_STARTED = "STARTED";
    public static final String STATE_FINISHED = "FINISHED";

    private String id;
    private String state;
    private Instant timestamp;

    // additional application log fields
    private String type;
    private String host;

    public LogEventLine(String id, String state, Instant timestamp) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
    }

    public LogEventLine(String id, String state, Instant timestamp, String type, String host) {
        this(id, state, timestamp);

        this.type = type;
        this.host = host;
    }

    @JsonIgnore
    public boolean isFinishedEvent() {
        return STATE_FINISHED.equals(state);
    }
}
