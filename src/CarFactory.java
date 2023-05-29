import Global.Config;
import Global.DynamicValues;
import Global.FactoryControlPanel;
import Global.ThreadState;
import Storages.AccessoryStorage;
import Storages.BodyStorage;
import Storages.CarStorage;
import Storages.EngineStorage;
import Suppliers.AccessorySupplier;
import Suppliers.BodySupplier;
import Suppliers.EngineSupplier;
import ThreadPool.ThreadPool;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.*;

public class CarFactory {
    public static int totalProducedCars = 0;
    public static Consumer<String> getChangeCallback(JLabel label) {
        return (s) -> SwingUtilities.invokeLater(() -> {
            label.setText(s);
        });
    }

    public static BiConsumer<String, ThreadState> getChangeCallbackColor(JLabel label) {
        label.setOpaque(true);
        return (str, s) -> SwingUtilities.invokeLater(() -> {
            Color c = switch (s) {
                case IDLE -> Color.WHITE;
                case WAIT -> Color.YELLOW;
                case WORK -> Color.GREEN;
            };
            label.setBackground(c);
            label.setText(str);
        });
    }

    public static void main(String[] args) {
        FactoryControlPanel controlPanel = new FactoryControlPanel();
        JFrame frame = new JFrame("Car factory");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(controlPanel.panel1);
        frame.pack();
        controlPanel.addListeners();

        EngineStorage engineStorage = new EngineStorage(
                Config.ENGINE_STORAGE_CAPACITY,
                getChangeCallback(controlPanel.engineStorageLabel)
        );
        BodyStorage bodyStorage = new BodyStorage(Config.BODY_STORAGE_CAPACITY,
                getChangeCallback(controlPanel.bodyStorageLabel)
        );
        AccessoryStorage accessoryStorage = new AccessoryStorage(Config.ACCESSORY_STORAGE_CAPACITY,
                getChangeCallback(controlPanel.accessoryStorageLabel)
        );
        CarStorage carStorage = new CarStorage(Config.CAR_STORAGE_CAPACITY,
                getChangeCallback(controlPanel.carStorageLabel)
        );

        ArrayList<BiConsumer<String, ThreadState>> callbacks = new ArrayList<>();
        for (int i = 0; i < Config.WORKER_COUNT; i++) {
            callbacks.add(getChangeCallbackColor((JLabel) controlPanel.workersPanel.getComponent(i)));
        }
        ThreadPool threadPool = new ThreadPool(Config.POOL_QUEUE_SIZE, Config.WORKER_COUNT,
                getChangeCallback(controlPanel.carAssemblyTaskCountLabel),
                callbacks,
                () -> SwingUtilities.invokeLater(() -> {
                    totalProducedCars++;
                    controlPanel.carpProducedCountLabel.setText(String.format("Total produced cars: %d", totalProducedCars));
                }));

        Controller controller = new Controller(engineStorage, bodyStorage, accessoryStorage, carStorage, threadPool, Config.CONTROLLER_MIN_CARS, DynamicValues.CAR_ASSEMBLE_TASK_DELAY_MS);

        EngineSupplier engineSupplier = new EngineSupplier(engineStorage, DynamicValues.ENGINE_SUPPLY_INTERVAL_MS, getChangeCallbackColor(controlPanel.engineSupplierLabel));

        BodySupplier bodySupplier = new BodySupplier(bodyStorage, DynamicValues.BODY_SUPPLY_INTERVAL_MS, getChangeCallbackColor(controlPanel.bodySupplierLabel));

        ArrayList<AccessorySupplier> accessorySupplierList = new ArrayList<>();
        for (int i = 0; i < Config.ACCESSORY_SUPPLIER_COUNT; i++) {
            accessorySupplierList.add(
                    new AccessorySupplier(accessoryStorage,
                    DynamicValues.ACCESSORY_SUPPLY_INTERVAL_MS,
                    getChangeCallbackColor((JLabel) controlPanel.accessorySuppliersPanel.getComponent(i)))
            );
        }

        ArrayList<Dealer> dealersList = new ArrayList<>();
        for (int i = 0; i < Config.DEALER_COUNT; i++) {
            dealersList.add(new Dealer(carStorage,
                    DynamicValues.DEALER_DELIVERY_INTERVAL_MS,
                    getChangeCallbackColor((JLabel) controlPanel.dealersPanel.getComponent(i)))
            );
        }


        frame.setVisible(true);

        controller.start();
        for (Dealer dealer : dealersList) {
            dealer.start();
        }
        engineSupplier.start();
        bodySupplier.start();
        for (AccessorySupplier accessorySupplier : accessorySupplierList) {
            accessorySupplier.start();
        }

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            for (AccessorySupplier accessorySupplier : accessorySupplierList) {
                accessorySupplier.interrupt();
            }
            engineSupplier.interrupt();
            bodySupplier.interrupt();
            controller.interrupt();
            threadPool.stop();
            for (Dealer dealer : dealersList) {
                dealer.interrupt();
            }
        }

    }
}
