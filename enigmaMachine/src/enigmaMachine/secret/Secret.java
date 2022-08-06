package enigmaMachine.secret;

import enigmaMachine.plugBoard.Plugs;
import enigmaMachine.reflector.Reflecting;
import enigmaMachine.rotor.Rotor;

import java.util.List;

public class Secret {
    private final List<Rotor> rotors;
    private final Reflecting reflector;
    private final Plugs plugBoard;

    public Secret(List<Rotor> rotors, Reflecting reflector, Plugs plugBoard) {
        this.rotors = rotors;
        this.reflector = reflector;
        this.plugBoard = plugBoard;
    }

    public Plugs getPlugBoard() {
        return plugBoard;
    }

    public Reflecting getReflector() {
        return reflector;
    }

    public List<Rotor> getRotors() {
        return rotors;
    }
}
