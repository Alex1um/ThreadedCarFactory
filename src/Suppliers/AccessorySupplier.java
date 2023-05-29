package Suppliers;

import Global.Config;
import Global.ThreadState;
import Parts.Accessory;
import Storages.Storage;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class AccessorySupplier extends Supplier<Accessory> {
    public AccessorySupplier(Storage<Accessory> storage, AtomicInteger supplyInterval, BiConsumer<String, ThreadState> changeCallback) {
        super(Accessory.class, storage, supplyInterval, changeCallback);
    }
}
