package DTO.codeObj;

import javafx.util.Pair;
import java.util.List;
import java.util.Map;

public class CodeObj {
    Map<Integer, Character> ID2Position;
    String reflectorID;
    List<Pair<Character, Character>> plugs;

    public CodeObj(Map<Integer, Character> ID2Position, String reflectorID, List<Pair<Character, Character>> plugs) {
        this.ID2Position = ID2Position;
        this.reflectorID = reflectorID;
        this.plugs = plugs;
    }

    public Map<Integer, Character> getID2Position() {
        return ID2Position;
    }

    public String getReflectorID() {
        return reflectorID;
    }

    public List<Pair<Character, Character>> getPlugs() {
        return plugs;
    }
}
