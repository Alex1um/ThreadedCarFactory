package Suppliers;

import Global.Config;
import Global.ThreadState;
import Parts.Engine;
import Storages.Storage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class EngineSupplier extends Supplier<Engine> {
    public EngineSupplier(Storage<Engine> storage, AtomicInteger supplyInterval, BiConsumer<String, ThreadState> changeCallback) {
        super(Engine.class, storage, supplyInterval, changeCallback);
    }
}
