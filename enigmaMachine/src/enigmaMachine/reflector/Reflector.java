package enigmaMachine.reflector;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Reflector implements Reflecting {
    private final Map<Integer, Integer> reflectionMap;
    private final ReflectorID id;

    //assuming reflector starts with 0
    public Reflector() {
        this.id = ReflectorID.I;
        reflectionMap = new HashMap<>();
        reflectionMap.put(0, 3);
        reflectionMap.put(1, 4);
        reflectionMap.put(2, 5);
        reflectionMap.put(3, 0);
        reflectionMap.put(4, 1);
        reflectionMap.put(5, 2);
    }
    public Reflector(String id, Map<Integer, Integer> reflectionMap) {
        this.id = ReflectorID.valueOf(id);
        this.reflectionMap = reflectionMap;
    }

    @Override
    public int  passThroughReflector(int toReflect) {
        return reflectionMap.get(toReflect);
    }

    @Override
    public String toString() {
        return "Reflector{" +
                "id=" + id +
                ", reflectionMap=" + reflectionMap +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reflector reflector = (Reflector) o;
        return Objects.equals(reflectionMap, reflector.reflectionMap) && id == reflector.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(reflectionMap, id);
    }
}
