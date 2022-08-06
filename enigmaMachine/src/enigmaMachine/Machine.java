package enigmaMachine;

import enigmaMachine.keyBoard.KeyBoard;
import enigmaMachine.plugBoard.Plugs;
import enigmaMachine.reflector.Reflecting;
import enigmaMachine.rotor.Rotor;
import enigmaMachine.secret.Secret;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
public class Machine {
    private final KeyBoard keyboard;
    private List<Rotor> rotors;
    private Reflecting reflector;
    private Plugs plugBoard;

    public Machine(KeyBoard keyboard, List<Rotor> rotors, Reflecting reflector, Plugs plugBoard) {
        this.keyboard = keyboard;
        this.rotors = rotors;
        this.reflector = reflector;
        this.plugBoard = plugBoard;
    }
    public Machine(Secret settings, KeyBoard keyboard) {
        this.keyboard = keyboard;
        updateBySecret(settings);
    }

    public void updateBySecret(@NotNull Secret settings) {
        this.rotors = settings.getRotors();
        setRotors();
        this.reflector = settings.getReflector();
        this.plugBoard = settings.getPlugBoard();
    }
    private void setRotors() {
        for(int i = 0; i < rotors.size() - 1 ; i++){
            rotors.get(i).setNextRotor(rotors.get(i + 1));
        }
    }
    public void resetRotorsToInitial(){
        rotors.forEach(Rotor::reset);
    }
    private int passThroughRotorsR2L(int index) {
        int passedIndex = index;
        for(Rotor r : rotors) {
            passedIndex = r.rightToLeftReturnIndex(passedIndex);
        }
        return passedIndex;
    }
    private int passThroughRotorsL2R(int index) {
        int passedIndex = index;
        Collections.reverse(rotors);
        for(Rotor r : rotors) {
            passedIndex = r.leftToRightReturnIndex(passedIndex);
        }
        Collections.reverse(rotors);
        return passedIndex;
    }
    public Character process(Character input) {
        rotors.get(0).advance();
        char pluggedOutput = plugBoard.passThroughPlugBoard(input);
        int rotorsInput = keyboard.getIndex(pluggedOutput);
        int reflectorInput = passThroughRotorsR2L(rotorsInput);
        rotorsInput = reflector.passThroughReflector(reflectorInput);
        int pluggedInputIndex = passThroughRotorsL2R(rotorsInput);
        char pluggedInput = keyboard.getChar(pluggedInputIndex);
        pluggedOutput = plugBoard.passThroughPlugBoard(pluggedInput);
        return pluggedOutput;
    }
}
