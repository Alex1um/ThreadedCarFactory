package Parts;

public abstract class Part {

    public int getId() {
        return id;
    }

    private final int id;
    protected Part(int id) {
        this.id = id;
    }
}
