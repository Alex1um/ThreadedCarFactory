package ThreadPool;

import Global.ThreadState;

import java.util.function.BiConsumer;

public abstract class ThreadPoolTask implements Runnable {

    public int getId() {
        return id;
    }

    private final int id;
    private static int idCounter = 0;

    private BiConsumer<String, ThreadState> changeCallback = (s, ts) -> {};



    private Runnable produceCallback = () -> {};
    protected Runnable getProduceCallback() {
        return produceCallback;
    }

    public void setProduceCallback(Runnable produceCallback) {
        this.produceCallback = produceCallback;
    }
    protected BiConsumer<String, ThreadState> getChangeCallback() {
        return changeCallback;
    }

    public void setChangeCallback(BiConsumer<String, ThreadState> changeCallback) {
        this.changeCallback = changeCallback;
    }
    public ThreadPoolTask() {
        this.id = idCounter;
        idCounter++;
    }

}
