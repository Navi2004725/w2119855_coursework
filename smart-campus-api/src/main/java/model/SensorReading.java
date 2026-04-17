package model;

import java.time.Instant;

public class SensorReading {
    private String id;
    private Instant timestamp;
    private Double value;

    public SensorReading() {
    }

    public SensorReading(String id, Instant timestamp, Double value) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
