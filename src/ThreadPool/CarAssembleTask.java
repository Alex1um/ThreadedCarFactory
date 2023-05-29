package ThreadPool;

import Global.Config;
import Global.ThreadState;
import Parts.Accessory;
import Parts.Body;
import Parts.Car;
import Parts.Engine;
import Storages.AccessoryStorage;
import Storages.BodyStorage;
import Storages.CarStorage;
import Storages.EngineStorage;

import java.util.ArrayList;
import java.util.List;

public class CarAssembleTask extends ThreadPoolTask {
    private EngineStorage engineStorage;
    private BodyStorage bodyStorage;
    private AccessoryStorage accessoryStorage;
    private CarStorage carStorage;
    private int assemblyTimeMs;

    public CarAssembleTask(EngineStorage engineStorage,
                           BodyStorage bodyStorage,
                           AccessoryStorage accessoryStorage,
                            CarStorage carStorage,
                           int assemblyTimeMs
    ) {
        super();
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.carStorage = carStorage;
        this.assemblyTimeMs = assemblyTimeMs;
    }

    @Override
    public void run() {
        try {
            // Получаем детали из соответствующих складов
            getChangeCallback().accept(String.format("task %d: waiting for engine", getId()), ThreadState.WAIT);
            Engine engine = engineStorage.get();
            getChangeCallback().accept(String.format("task %d: waiting for body", getId()), ThreadState.WAIT);
            Body body = bodyStorage.get();
            getChangeCallback().accept(String.format("task %d: waiting for accessory", getId()), ThreadState.WAIT);
            Accessory accessory = accessoryStorage.get();

            // Собираем автомобиль
            getChangeCallback().accept(String.format("task %d: assembling car", getId()), ThreadState.WORK);
            Thread.sleep(assemblyTimeMs);
            Car product = new Car(engine, body, accessory);
            getProduceCallback().run();

            // Добавляем автомобиль на склад
            getChangeCallback().accept(String.format("task %d: putting car %d", getId(), product.getId()), ThreadState.WAIT);
            carStorage.put(product);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
