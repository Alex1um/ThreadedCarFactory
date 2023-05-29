package Parts;

import java.util.List;

public class Car extends Part {
    private final Engine engine;
    private final Body body;
    private final Accessory accessory;

    private static int carIdCounter = 0;

    public Car(Engine engine, Body body, Accessory accessory) {
        super(carIdCounter++);
        this.engine = engine;
        this.body = body;
        this.accessory = accessory;
    }

    public Engine getEngine() {
        return engine;
    }

    public Body getBody() {
        return body;
    }

    public Accessory getAccessory() {
        return accessory;
    }
}