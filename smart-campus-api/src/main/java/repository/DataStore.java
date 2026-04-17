package repository;

import model.Room;
import model.Sensor;
import model.SensorReading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    private static final DataStore INSTANCE = new DataStore();

    private final Map<String, Room> rooms = new HashMap<>();
    private final Map<String, Sensor> sensors = new HashMap<>();
    private final Map<String, List<SensorReading>> readingsBySensor = new HashMap<>();

    private DataStore() {
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public Map<String, Room> getRooms() {
        return rooms;
    }

    public Map<String, Sensor> getSensors() {
        return sensors;
    }

    public Map<String, List<SensorReading>> getReadingsBySensor() {
        return readingsBySensor;
    }
}
