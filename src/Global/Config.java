package Global;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class Config {
    public static final int CONTROLLER_MIN_CARS;
    public static final int ENGINE_STORAGE_CAPACITY;
    public static final int BODY_STORAGE_CAPACITY;
    public static final int ACCESSORY_STORAGE_CAPACITY;
    public static final int CAR_STORAGE_CAPACITY;

    public static final int ACCESSORY_SUPPLIER_COUNT;
    public static final int DEALER_COUNT;
    public static final int WORKER_COUNT;
    public static final int POOL_QUEUE_SIZE;
    public static final boolean LOG_SALE;

    static {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Failed to load config properties: " + e.getMessage());
        }

        CONTROLLER_MIN_CARS = Integer.parseInt(props.getProperty("CONTROLLER_MIN_CARS", "2"));
        ENGINE_STORAGE_CAPACITY = Integer.parseInt(props.getProperty("ENGINE_STORAGE_CAPACITY", "10"));
        BODY_STORAGE_CAPACITY = Integer.parseInt(props.getProperty("BODY_STORAGE_CAPACITY", "5"));
        ACCESSORY_STORAGE_CAPACITY = Integer.parseInt(props.getProperty("ACCESSORY_STORAGE_CAPACITY", "20"));
        CAR_STORAGE_CAPACITY = Integer.parseInt(props.getProperty("CAR_STORAGE_CAPACITY", "3"));
        ACCESSORY_SUPPLIER_COUNT = Integer.parseInt(props.getProperty("ACCESSORY_SUPPLIER_COUNT", "3"));
        DEALER_COUNT = Integer.parseInt(props.getProperty("DEALER_COUNT", "3"));
        WORKER_COUNT = Integer.parseInt(props.getProperty("WORKER_COUNT", "3"));
        POOL_QUEUE_SIZE = Integer.parseInt(props.getProperty("POOL_QUEUE_SIZE", "3"));
        LOG_SALE = Boolean.parseBoolean(props.getProperty("LOG_SALE", "true"));
    }

    private Config() {}
}