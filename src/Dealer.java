import Global.Config;
import Global.ThreadState;
import Parts.Car;
import Storages.CarStorage;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dealer extends Thread {
    private static final Logger logger = Logger.getLogger(Dealer.class.getName());
    private CarStorage storage;
    private AtomicInteger deliveryInterval;
    private int dealerId;
    private static int dealerIdCounter;
    private int dealedCount = 0;
    private final BiConsumer<String, ThreadState> changeCallback;

    public Dealer(CarStorage storage, AtomicInteger deliveryInterval, BiConsumer<String, ThreadState> changeCallback) {
        this.storage = storage;
        this.deliveryInterval = deliveryInterval;
        this.changeCallback = changeCallback;
        this.dealerId = dealerIdCounter;
        dealerIdCounter++;
    }

    @Override
    public void run() {
        try {
            while (true) {
                changeCallback.accept(String.format("Dealer %d(%d) waiting for car", dealerId, dealedCount), ThreadState.WAIT);
                Car product = storage.get();
                changeCallback.accept(String.format("Dealer %d(%d) dealing %d...", dealerId, dealedCount, product.getId()), ThreadState.WORK);
                Thread.sleep(deliveryInterval.get());
                if (Config.LOG_SALE) {
                    logger.log(Level.INFO, String.format("%s: Dealer %d: Auto: %d(Body: %d, Engine: %d, Accessory: %d)",
                            new Date(),
                            dealerId,
                            product.getId(),
                            product.getBody().getId(),
                            product.getEngine().getId(),
                            product.getAccessory().getId()));
                }
                dealedCount++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}