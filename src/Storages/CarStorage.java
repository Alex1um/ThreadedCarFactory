package Storages;

import Global.Config;
import Parts.Car;

import java.util.function.Consumer;

public class CarStorage extends Storage<Car> {
    public CarStorage(int capacity, Consumer<String> changeCallback) {
//        super(capacity, Config.controlPanel.carStorageLabel);
        super(capacity, changeCallback);
    }
}
