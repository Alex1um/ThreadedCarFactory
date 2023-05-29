package Suppliers;

import Global.Config;
import Global.ThreadState;
import Parts.Body;
import Storages.Storage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class BodySupplier extends Supplier<Body> {
    public BodySupplier(Storage<Body> storage, AtomicInteger supplyInterval, BiConsumer<String, ThreadState> changeCallback) {
        super(Body.class, storage, supplyInterval, changeCallback);
    }
}
