package Parts;

public class Body extends Part {
    private static int bodyIdCounter = 0;
    public Body() {
        super(bodyIdCounter++);
    }

}
