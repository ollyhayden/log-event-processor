package com.creditsuisse.model.db;

import lombok.*;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class LogEvent {
    @Id
    private String id;

    private Long duration;

    // application logs
    @Nullable
    private String type;
    @Nullable
    private String host;

    public LogEvent(String id,
                    Long duration,
                    String type,
                    String host) {
        this.id = id;
        this.duration = duration;
        this.type = type;
        this.host = host;
    }
}
