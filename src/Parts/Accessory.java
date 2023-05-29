package Parts;

import java.util.concurrent.atomic.AtomicInteger;

public class Accessory extends Part {
    private static int accessoryIdCounter = 0;
    public Accessory() {
        super(accessoryIdCounter++);
    }

}
