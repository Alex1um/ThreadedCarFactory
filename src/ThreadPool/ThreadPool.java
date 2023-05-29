package ThreadPool;

import Global.Config;
import Global.ThreadState;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ThreadPool {
    private BlockingQueue<ThreadPoolTask> taskQueue;
    private List<PoolThread> threads;
    private boolean isStopped = false;

    private final Consumer<String> queueChangeCallback;

    public ThreadPool(int queueSize,
                      int workerCount,
                      Consumer<String> queueChangeCallback,
                      List<BiConsumer<String, ThreadState>> threadsChangeCallbacks,
                      Runnable produceCarCallback) {
        taskQueue = new ArrayBlockingQueue<>(queueSize);
        this.queueChangeCallback = queueChangeCallback;

        threads = new ArrayList<>();
        for (int i = 0; i < workerCount; i++) {
            threads.add(new PoolThread(i, taskQueue, queueChangeCallback, threadsChangeCallbacks.get(i), produceCarCallback));
        }
        for (PoolThread thread : threads) {
            thread.start();
        }
    }

    public synchronized void execute(ThreadPoolTask task) throws InterruptedException {
        if (this.isStopped)
            throw new IllegalStateException("ThreadPool is stopped");

        this.taskQueue.put(task);
        queueChangeCallback.accept(String.format("task queue size: %d", taskQueue.size()));
    }

    public synchronized void stop() {
        this.isStopped = true;
        for (PoolThread thread : threads) {
            thread.doStop();
        }
    }

    public synchronized int getPendingCount() {
        return taskQueue.size();
    }

    private class PoolThread extends Thread {
        private BlockingQueue<ThreadPoolTask> taskQueue;
        private boolean isStopped = false;
        private final Consumer<String> queueChangeCallback;
        private final BiConsumer<String, ThreadState> changeCallback;
        private final int id;
        private final Runnable produceCarCallback;
        public PoolThread(int id,
                          BlockingQueue<ThreadPoolTask> taskQueue,
                          Consumer<String> queueChangeCallback,
                          BiConsumer<String, ThreadState> changeCallback,
                          Runnable produceCarCallback
        ) {
            this.id = id;
            this.queueChangeCallback = queueChangeCallback;
            this.changeCallback = changeCallback;
            this.taskQueue = taskQueue;
            this.produceCarCallback = produceCarCallback;
        }

        public void run() {
            while (!isStopped) {
                try {
                    changeCallback.accept(String.format("worker %d waiting for task", id), ThreadState.IDLE);
                    ThreadPoolTask runnable = taskQueue.take();
                    runnable.setChangeCallback(changeCallback);
                    runnable.setProduceCallback(produceCarCallback);
                    queueChangeCallback.accept(String.format("task queue size: %d", taskQueue.size()));
                    runnable.run();
                } catch (Exception e) {
                    System.out.println("Error! " + e);
                    // log or otherwise report exception,
                    // but keep pool thread alive.
                }
            }
        }

        public synchronized void doStop() {
            isStopped = true;
            this.interrupt();
        }
    }
}