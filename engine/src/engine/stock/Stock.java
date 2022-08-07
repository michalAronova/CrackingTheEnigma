package engine.stock;

import enigmaMachine.keyBoard.KeyBoard;
import enigmaMachine.reflector.Reflecting;
import enigmaMachine.reflector.Reflector;
import enigmaMachine.rotor.Rotor;
import enigmaMachine.rotor.TheRotor;
import schema.generated.CTEPositioning;
import schema.generated.CTEReflect;
import schema.generated.CTEReflector;
import schema.generated.CTERotor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stock {
    Map<Integer,Rotor> rotorMap;
    Map<String, Reflecting> reflectorMap;
    KeyBoard keyBoard;
    int rotorsCount;
    public Stock(List<CTERotor> rotors, List<CTEReflector> reflectors, KeyBoard keyBoard, int rotorsCount) {
        rotorMap = new HashMap<>();
        reflectorMap = new HashMap<>();
        this.keyBoard = keyBoard;
        this.rotorsCount = rotorsCount;
        fillRotorsMap(rotors);
        fillReflectorsMap(reflectors);
    }

    private void fillRotorsMap(List<CTERotor> CTERotors) {
        CTERotors.forEach(r -> rotorMap.put(r.getId(), new TheRotor(r.getId(), r.getNotch(), getPermutation(r.getCTEPositioning()))));
    }
    private String[] getPermutation(List<CTEPositioning> positionings) {
        String[] permutations = new String[2];
        String permutationL = "";
        String permutationR = "";
        for(CTEPositioning pos : positionings) {
            permutationL = permutationL.concat(pos.getLeft());
            permutationR = permutationR.concat(pos.getRight());
        }
        permutations[0] = permutationL;
        permutations[1] = permutationR;
        return permutations;
    }
    private void fillReflectorsMap(List<CTEReflector> CTEReflectors) {
        CTEReflectors.forEach(r -> reflectorMap.put(r.getId(), new Reflector(r.getId(), reflectionListToMap(r.getCTEReflect()))));
    }
    //getting pairs in base 1 and return map in base 0
    private Map<Integer, Integer> reflectionListToMap(List<CTEReflect> reflectionList) {
        Map<Integer, Integer> int2int = new HashMap<>();
        reflectionList.forEach(r -> { int2int.put((r.getInput()-1), (r.getOutput()-1));
                                      int2int.put((r.getOutput()-1), (r.getInput()-1));
                                });
        return int2int;
    }
    public Map<Integer, Integer> getNotches() {
        Map<Integer, Integer> id2notch = new HashMap<>();
        rotorMap.forEach((id, r) -> id2notch.put(id, r.getNotch()));
        return id2notch;
    }
    public Map<Integer, Rotor> getRotorMap() {
        return rotorMap;
    }

    public Map<String, Reflecting> getReflectorMap() {
        return reflectorMap;
    }

    public KeyBoard getKeyBoard() {
        return keyBoard;
    }

    public int getRotorsCount() {
        return rotorsCount;
    }
}
