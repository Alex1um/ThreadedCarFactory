package Global;

import java.util.concurrent.atomic.AtomicInteger;

public final class DynamicValues {
    public final static AtomicInteger ENGINE_SUPPLY_INTERVAL_MS = new AtomicInteger(3000);
    public final static AtomicInteger BODY_SUPPLY_INTERVAL_MS = new AtomicInteger(3000);
    public final static AtomicInteger ACCESSORY_SUPPLY_INTERVAL_MS = new AtomicInteger(1000);
    public final static AtomicInteger DEALER_DELIVERY_INTERVAL_MS = new AtomicInteger(500);
    public final static AtomicInteger CAR_ASSEMBLE_TASK_DELAY_MS = new AtomicInteger(500);
}
