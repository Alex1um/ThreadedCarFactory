package Parts;

public class Engine extends Part {

    private static int engineIdCounter = 0;
    public Engine() {
        super(engineIdCounter++);
    }

}
