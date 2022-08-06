package enigmaMachine.reflector;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reflector implements Reflecting {
    private final Map<Integer, Integer> reflectionMap;
    private final String id;

    //assuming reflector starts with 0
    public Reflector() {
        this.id = "I";
        reflectionMap = new HashMap<>();
        reflectionMap.put(0, 3);
        reflectionMap.put(1, 4);
        reflectionMap.put(2, 5);
        reflectionMap.put(3, 0);
        reflectionMap.put(4, 1);
        reflectionMap.put(5, 2);
    }
    //notice for michals!!!!
    //trnalste from base 1 to base 0
    public Reflector(@NotNull List<Pair<Integer,Integer>> pairs, String id) {
        this.id = id;
        reflectionMap = new HashMap<>();
        pairs.forEach(p -> {
            reflectionMap.put(p.getKey(),p.getValue());
            reflectionMap.put(p.getValue(), p.getKey());
                            });
    }

    public Reflector(String id, Map<Integer, Integer> reflectionMap) {
        this.id = id;
        this.reflectionMap = reflectionMap;
    }

    @Override
    public int  passThroughReflector(int toReflect) {
        return reflectionMap.get(toReflect);
    }

}
