package cz.kul.snippets.designpatterns.example03_flyweight;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

/**
 * Flyweight can reduce memory footprint by sharing data between objects. Object is
 * diveded into two parts - shareable and unicate. Shareable part is stored somewhere and
 * shared by other object, unicate part is unique for the particular object.
 * 
 * Examples
 * <ul>
 * <li>string interning</li>
 * <li>characters in text editor - format (font, weight, size) is stored as flyweight and
 * shared by character objects</li>
 * </ul>
 * 
 * @author kulhalad
 * @since 7.4.2
 */
public class Main_Flyweight {

    public static void main(String[] args) {

        DeviceFactory.getFactory().create("dev1", "Ostrava", "t01");
        DeviceFactory.getFactory().create("dev2", "Prague", "t01");

        LogMessage msg1 = new LogMessage("msg1", "dev1");
        LogMessage msg2 = new LogMessage("msg2", "dev1");

        assertTrue(msg1.getDevice() == msg2.getDevice()); // So Device is shared (Device is flyweight)
    }

}

/**
 * This is client class. Instances of LogMessage share Device instances.
 * 
 * @author kulhalad
 * @since 7.4.2
 */
class LogMessage {
    private DeviceFactory deviceFactory = DeviceFactory.getFactory();

    private String message;
    private Device device;

    public LogMessage(String message, String deviceName) {
        this.message = message;
        if (!deviceFactory.exists(deviceName)) {
            throw new RuntimeException(); // TODO how to solve that?
        }
        this.device = deviceFactory.get(deviceName);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

}

/**
 * This is flyweight object. It is shared by many other objects. Immutable.
 */
class Device {
    private String name;
    private String location;
    private String type;

    Device(String name, String location, String type) {
        super();
        this.name = name;
        this.location = location;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Device other = (Device) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }

}

class DeviceFactory {

    private static DeviceFactory singleton;

    public Map<String, Device> cache = new HashMap<>();

    private DeviceFactory() {
    }

    public static DeviceFactory getFactory() {
        if (singleton == null) {
            synchronized (DeviceFactory.class) {
                if (singleton == null) {
                    singleton = new DeviceFactory();
                }
            }
        }
        return singleton;
    }

    public boolean exists(String name) {
        return cache.containsKey(name);
    }

    public Device get(String name) {
        return cache.get(name);
    }

    public void create(String name, String location, String type) {
        Device device = new Device(name, location, type);
        cache.put(name, device);
    }

}
