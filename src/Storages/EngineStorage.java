package Storages;

import Global.Config;
import Parts.Engine;

import java.util.function.Consumer;

public class EngineStorage extends Storage<Engine> {
    public EngineStorage(int capacity, Consumer<String> changeCallback) {
//        super(capacity, Config.controlPanel.engineStorageLabel);
        super(capacity, changeCallback);
    }
}