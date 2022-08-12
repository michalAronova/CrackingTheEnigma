package enigmaMachine.plugBoard;

import enigmaMachine.keyBoard.KeyBoard;
import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class PlugBoard implements Plugs {

    private Map<Character, Character> plugBoard;
    private KeyBoard keyBoard;

    public PlugBoard(@NotNull List<Pair<Character, Character>> plugs, KeyBoard keyBoard) {
        this.keyBoard = keyBoard;
        plugBoard = new HashMap<>();
        clearAllPlugs();
        setAllPlugs(plugs);
    }

    public PlugBoard() {
        this.keyBoard = new KeyBoard();
        List<Character> keys = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f');
        plugBoard = new HashMap<>();
        keys.forEach(c -> plugBoard.put(c, c));
        plugBoard.replace('b', 'c');
        plugBoard.replace('c', 'b');
    }

    public PlugBoard(@NotNull List<Pair<Character, Character>> plugs) {
        this.keyBoard = new KeyBoard();
        List<Character> keys = Arrays.asList('a', 'b', 'c', 'd', 'e', 'f');
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

    @Override
    public String toString() { //mimi- return only non default plugs?
        return "PlugBoard{" + plugBoard + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlugBoard plugBoard1 = (PlugBoard) o;
        return Objects.equals(plugBoard, plugBoard1.plugBoard) && Objects.equals(keyBoard, plugBoard1.keyBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plugBoard, keyBoard);
    }
}
