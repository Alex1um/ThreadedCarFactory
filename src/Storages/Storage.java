package Storages;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Storage<T> {
    private int capacity;
    private final List<T> stored;
    private JLabel infoLabel;
    private final Consumer<String> changeCallback;

    public Storage(int capacity, Consumer<String> changeCallback) {
        this.capacity = capacity;
        stored = new ArrayList<>();
        this.changeCallback = changeCallback;
    }

    public synchronized void put(T toStore) throws InterruptedException {
        while (stored.size() == capacity) {
            wait();
        }
        stored.add(toStore);
        changeCallback.accept(String.format("%s: %d", getClass().getSimpleName(), stored.size()));
//        SwingUtilities.invokeLater(() -> infoLabel.setText(String.format("%s: %d", getClass().getSimpleName(), stored.size())));
        notifyAll();
    }

    public synchronized T get() throws InterruptedException {
        while (stored.isEmpty()) {
            wait();
        }
        T toGet = stored.remove(0);
        changeCallback.accept(String.format("%s: %d", getClass().getSimpleName(), stored.size()));
//        SwingUtilities.invokeLater(() -> infoLabel.setText(String.format("%s: %d", getClass().getSimpleName(), stored.size())));
        notifyAll();
        return toGet;
    }

    public int getStoredSize() {
        return stored.size();
    }

}
