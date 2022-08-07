package enigmaMachine.reflector;

public enum ReflectorID {
    I(1),
    II(2),
    III(3),
    IV(4),
    V(5);
    private final int id;

    private ReflectorID(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    @Override
    public String toString() {
        return name();
    }
}
