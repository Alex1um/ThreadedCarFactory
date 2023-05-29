package Suppliers;

import Global.ThreadState;
import Parts.Part;
import Storages.Storage;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public abstract class Supplier<T extends Part> extends Thread {
    private final Storage<T> storage;
    private AtomicInteger deliveryInterval;
    private Class<T> supplyType;
    private JLabel infoLabel;
    private int suppliedCount = 0;
    private final BiConsumer<String, ThreadState> changeCallback;

    public Supplier(Class<T> supplyType, Storage<T> storage, AtomicInteger deliveryInterval, BiConsumer<String, ThreadState> changeCallback) {
        this.storage = storage;
        this.deliveryInterval = deliveryInterval;
        this.changeCallback = changeCallback;
        this.supplyType = supplyType;
    }

    @Override
    public void run() {
        try {
            while (true) {
                T part = supplyType.newInstance();
                changeCallback.accept(String.format("%s(%d) supplying: %d...", getClass().getSimpleName(), suppliedCount, part.getId()), ThreadState.WORK);
                Thread.sleep(deliveryInterval.get());
                changeCallback.accept(String.format("%s(%d) waiting to put: %d...", getClass().getSimpleName(), suppliedCount, part.getId()), ThreadState.WAIT);
                storage.put(part);
                suppliedCount++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
