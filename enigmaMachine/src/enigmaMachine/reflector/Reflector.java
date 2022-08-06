package enigmaMachine.reflector;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reflector implements Reflecting {
    private final Map<Integer, Integer> reflector;
    private final String id;

    //assuming reflector starts with 0
    public Reflector() {
        this.id = "I";
        reflector = new HashMap<>();
        reflector.put(0, 3);
        reflector.put(1, 4);
        reflector.put(2, 5);
        reflector.put(3, 0);
        reflector.put(4, 1);
        reflector.put(5, 2);
    }
    //notice for michals!!!!
    //trnalste from base 1 to base 0
    public Reflector(@NotNull List<Pair<Integer,Integer>> pairs, String id) {
        this.id = id;
        reflector = new HashMap<>();
        pairs.forEach(p -> {
                            reflector.put(p.getKey(),p.getValue());
                            reflector.put(p.getValue(), p.getKey());
                            });
    }
    @Override
    public int  passThroughReflector(int toReflect) {
        return reflector.get(toReflect);
    }

}
