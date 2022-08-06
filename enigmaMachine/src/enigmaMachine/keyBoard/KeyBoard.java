package enigmaMachine.keyBoard;

import java.util.List;
import java.util.stream.Collectors;

public class KeyBoard {
    private final String keyBoard;

    public KeyBoard() {
        keyBoard = "abcdef";
    }

    public boolean isInKeyBoard(Character c) {
        return keyBoard.contains(c.toString());
    }

    public int getIndex(char c){
        return keyBoard.indexOf(c);
    }

    public char getChar(int index) {
        return keyBoard.charAt(index);
    }
    public List<Character> getAsCharList() {
        return keyBoard.chars()
                .mapToObj(e->(char)e).collect(Collectors.toList());
    }
}

