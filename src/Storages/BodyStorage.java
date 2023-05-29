package Storages;

import Parts.Body;
import Global.Config;

import java.util.function.Consumer;

public class BodyStorage extends Storage<Body> {
    public BodyStorage(int capacity, Consumer<String> changeCallback) {
//        super(capacity, Config.controlPanel.bodyStorageLabel);
        super(capacity, changeCallback);
    }
}
