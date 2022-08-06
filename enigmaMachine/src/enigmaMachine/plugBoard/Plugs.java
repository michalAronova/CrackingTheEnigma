package enigmaMachine.plugBoard;

import javafx.util.Pair;
import java.util.List;

public interface Plugs {
    Character passThroughPlugBoard(Character input);
    void clearAllPlugs();
    void setAllPlugs(List<Pair<Character, Character>> plugs);
}
