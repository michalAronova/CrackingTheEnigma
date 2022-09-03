package enigmaMachine.reflector;

import java.io.Serializable;

public interface Reflecting extends Serializable, Cloneable {
    int passThroughReflector(int toReflect);
}
