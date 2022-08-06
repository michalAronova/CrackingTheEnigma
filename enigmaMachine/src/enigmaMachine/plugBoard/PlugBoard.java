package enigmaMachine.plugBoard;

import enigmaMachine.keyBoard.KeyBoard;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlugBoard implements Plugs {

    private final Map<Character, Character> plugBoard;
    private final KeyBoard keyBoard;

    public PlugBoard() {
        this.keyBoard = new KeyBoard();
        List<Character> keys = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f');
        plugBoard = new HashMap<>();
        keys.forEach(c -> plugBoard.put(c, c));
        plugBoard.replace('b', 'c');
        plugBoard.replace('c', 'b');
    }

    public PlugBoard(@NotNull LinkedList<Pair<Character, Character>> plugs) {
        this.keyBoard = new KeyBoard();
        List<Character> keys = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f');
        plugBoard = new HashMap<>();
        clearAllPlugs();
        setAllPlugs(plugs);
    }

    public PlugBoard(@NotNull List<Pair<Character, Character>> plugs, List<Character> keys, KeyBoard keyBoard) {
        this.keyBoard = keyBoard;
        plugBoard = new HashMap<>();
        clearAllPlugs();
        setAllPlugs(plugs);
    }

    @Override
    public Character passThroughPlugBoard(Character input) {
        return plugBoard.get(input);
    }

    @Override
    public void clearAllPlugs() {
        keyBoard.getAsCharList().forEach(c -> plugBoard.put(c, c));
    }
    @Override
    public void setAllPlugs(List<Pair<Character, Character>> plugs) {
        plugs.forEach(p -> {
            plugBoard.replace(p.getKey(), p.getValue());
            plugBoard.replace(p.getValue(), p.getKey());
        });
    }
}
