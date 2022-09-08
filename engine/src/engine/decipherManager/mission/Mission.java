package engine.decipherManager.mission;

import DTO.codeObj.CodeObj;
import DTO.missionResult.MissionResult;
import engine.decipherManager.dictionary.Dictionary;
import engine.decipherManager.speedometer.Speedometer;
import enigmaMachine.Machine;
import enigmaMachine.keyBoard.KeyBoard;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Mission implements Runnable {
    private Machine machine;
    private final double missionSize;
    private final String toDecrypt;
    private final Dictionary dictionary;
    private List<Character> currentPositions;
    private final Speedometer speedometer;

    private List<CodeObj> candidates;


    public Mission(Machine machine, List<Character> startRotorsPositions, double missionSize,
                   String toDecrypt, Dictionary dictionary, Speedometer speedometer) {
        this.machine = machine;
        this.missionSize = missionSize;
        this.toDecrypt = toDecrypt;
        this.dictionary = dictionary;
        this.currentPositions = startRotorsPositions;
        machine.updateByPositionsList(currentPositions);
        this.speedometer = speedometer;
        candidates = new LinkedList<>();
    }

    @Override
    public void run() {
        long startTime = System.nanoTime();
        for (int i = 0; i < missionSize; i++){
            //do thing:
            //  1. machine.process(toDecrypt);
            CodeObj currentCode = machine.getMachineCode();
            String processed = machine.processWord(toDecrypt);
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
                System.out.println(Thread.currentThread().getName() + " found a candidate!");
                System.out.println("origin: " + toDecrypt);
                System.out.println("processed: " + processed);
                System.out.println("code: ");
                System.out.println(currentCode);
                candidates.add(currentCode);
            //      2.1 if yes - put in the blockingQueue of decryption candidates
            }
            else{
                //System.out.println(String.format("%s - did not find candidate in: %s", Thread.currentThread().getName(), currentCode.toString()));
            }
            currentPositions = speedometer.calculateNext(currentPositions);
            machine.updateByPositionsList(currentPositions);
        }
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        MissionResult result = new MissionResult(candidates, timeElapsed);
        //push to blockingQueue!
    }
}
