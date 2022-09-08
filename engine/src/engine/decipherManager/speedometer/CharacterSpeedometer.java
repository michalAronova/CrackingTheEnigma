package engine.decipherManager.speedometer;

import enigmaMachine.keyBoard.KeyBoard;

import java.util.ArrayList;
import java.util.List;

public class CharacterSpeedometer implements Speedometer<List<Character>, KeyBoard>{
    @Override
    public List<Character> calculateNext(List<Character> characters, KeyBoard keyboard) {
        List<Character> updated = new ArrayList<>(characters);
        char[] board = keyboard.toString().toCharArray();
        for(int i = 0; i < characters.size(); i++){
            if(updated.get(i).equals(board[board.length - 1])){
                updated.set(i, board[0]);
            }
            else{
                updated.set(i, board[keyboard.indexOf(updated.get(i)) + 1]);
                break;
            }
        }
        return updated;
    }
}
