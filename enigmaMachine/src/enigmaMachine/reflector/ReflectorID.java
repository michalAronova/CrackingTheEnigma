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
    public static String getRomeByInteger(int id){
        for(ReflectorID ID: ReflectorID.values()){
            if(ID.getId() == id){
                return ID.name();
            }
        }
        return null;
    }
    @Override
    public String toString() {
        return name();
    }
}
