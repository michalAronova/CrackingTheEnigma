package enigmaMachine.keyBoard;

import java.util.List;
import java.util.stream.Collectors;

public class KeyBoard {
    private final String keyBoard;
    public KeyBoard() {
        keyBoard = "abcdef";
    }
    public KeyBoard(String abc) {
        keyBoard = abc;
    }

    public boolean isInKeyBoard(Character c) {
        return keyBoard.contains(c.toString());
    }
    public boolean isInKeyBoard(String s) {
        for(Character c : s.toCharArray()) {
            if(!isInKeyBoard(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    public int indexOf(char c){
        return keyBoard.indexOf(c);
    }

    public char charAt(int index) {
        return keyBoard.charAt(index);
    }
    public int length() {
        return keyBoard.length();
    }
    public List<Character> getAsCharList() {
        return keyBoard.chars()
                .mapToObj(e->(char)e).collect(Collectors.toList());
    }
    public List<Object> getAsObjList() {
        return keyBoard.chars()
                .mapToObj(e->(char)e).collect(Collectors.toList());
    }
    public String getKeyBoardString() {
        return keyBoard;
    }

}

