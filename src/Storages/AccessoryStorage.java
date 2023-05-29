package Storages;

import Global.Config;
import Parts.Accessory;

import java.util.function.Consumer;

public class AccessoryStorage extends Storage<Accessory> {
    public AccessoryStorage(int capacity, Consumer<String> changeCallback) {
//        super(capacity, Config.controlPanel.accessoryStorageLabel);
        super(capacity, changeCallback);
    }
}
