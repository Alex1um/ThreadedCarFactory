import Storages.AccessoryStorage;
import Storages.BodyStorage;
import Storages.CarStorage;
import Storages.EngineStorage;
import ThreadPool.ThreadPool;
import ThreadPool.CarAssembleTask;

import java.util.concurrent.atomic.AtomicInteger;

public class Controller extends Thread {

    private final EngineStorage engineStorage;
    private final BodyStorage bodyStorage;
    private final AccessoryStorage accessoryStorage;
    private final CarStorage carStorage;
    private final ThreadPool threadPool;
    private final int minReadyProducts;
    private final AtomicInteger assemblyTimeMs;

    public Controller(
            EngineStorage engineStorage,
            BodyStorage bodyStorage,
            AccessoryStorage accessoryStorage,
            CarStorage CarStorage,
            ThreadPool threadPool,
            int minReadyProducts,
            AtomicInteger assemblyTimeMs
    ) {
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = CarStorage;
        this.threadPool = threadPool;
        this.minReadyProducts = minReadyProducts;
        this.assemblyTimeMs = assemblyTimeMs;
    }

    @Override
    public void run() {
        try {
            synchronized (carStorage) {
                while (true) {
                    int stored = carStorage.getStoredSize();
                    int pending = threadPool.getPendingCount();
                    if (stored + pending < minReadyProducts) {
                        for (int i = 0;i < minReadyProducts - stored - pending; i++) {
                            threadPool.execute(new CarAssembleTask(engineStorage,
                                    bodyStorage,
                                    accessoryStorage,
                                    carStorage,
                                    assemblyTimeMs.get()
                            ));
                        }
                    }
                    carStorage.wait();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
