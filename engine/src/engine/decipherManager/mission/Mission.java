package engine.decipherManager.mission;

import engine.decipherManager.dictionary.Dictionary;
import engine.decipherManager.speedometer.Speedometer;
import enigmaMachine.Machine;
import enigmaMachine.keyBoard.KeyBoard;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Mission implements Runnable {
    private Machine machine;
    private final int missionSize;
    private final String toDecrypt;

    private final Dictionary dictionary;

    Speedometer<List<Character>, KeyBoard> speedometer;


    public Mission(Machine machine, List<Pair<Integer, Character>> startRotorsPositions, int missionSize, String toDecrypt, Dictionary dictionary) {
        this.machine = machine;
        this.missionSize = missionSize;
        this.toDecrypt = toDecrypt;
        this.dictionary = dictionary;
        machine.updateByPositionsList(startRotorsPositions);
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " debugging data");
        for (int i = 0; i < missionSize; i++){
            System.out.println("mission size:");
            //do thing:
            //  1. machine.process(toDecrypt);
            String processed = machine.processWord(toDecrypt);
            System.out.println("processed: " + processed);
            String[] words = processed.split(" ");
            boolean isCandidate = true;
            for (String word: Arrays.stream(words).collect(Collectors.toList())) {
            //  2. compare to dictionary
                if(!dictionary.isInDictionary(word)){
                    isCandidate = false;
                    break;
                }
            }
            if(isCandidate){
                System.out.println("candidate"  /*+machine.getCode()*/);
                //push to blockingQueue!
            //      2.1 if yes - put in the blockingQueue of decryption candidates
            }
            else{
                System.out.println("not candidate");
            }
            //speedometer
            //update machine by the speedometer return
        }
    }
}
