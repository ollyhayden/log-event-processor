package com.creditsuisse.model.db;

import lombok.*;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class LogEvent {
    private static final int ALERT_DURATION_THRESHOLD_IN_MS = 4;

    @Id
    private String id;

    private Long duration;

    // application logs
    @Nullable
    private String type;
    @Nullable
    private String host;

    // TODO should we have a derived column in the database - perhaps a query on the events in DB to find what we need?
    private Boolean alert;

    public LogEvent(String id,
                    Long duration,
                    String type,
                    String host) {
        this.id = id;
        this.type = type;
        this.host = host;

        setDuration(duration);
    }

    // use dummy setter as alert is derived from duration column
    public void setAlert(Boolean ignored) {
        alert = duration > ALERT_DURATION_THRESHOLD_IN_MS;
    }

    public void setDuration(Long duration) {
        this.duration = duration;

        setAlert(null);
    }
}
