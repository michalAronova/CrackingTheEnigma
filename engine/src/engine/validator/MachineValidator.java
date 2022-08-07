package engine.validator;

import schema.generated.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineValidator implements Validator{
    CTEMachine machine;

    public MachineValidator(CTEMachine machine){
        this.machine = machine;
    }

    @Override
    public boolean validate() {
        /*machine.getCTERotors().getCTERotor(), machine.getCTEReflectors().getCTEReflector(),
                        new KeyBoard(machine.getABC().trim()), machine.getRotorsCount()*/
        validateABC();
        validateRotorsAndCount(); // continue to this method only if abc is valid :)
        validateReflectors();
        return true;
    }
    //must call validateABC before this method
    private void validateRotorsAndCount(){
        List<CTERotor> rotors = machine.getCTERotors().getCTERotor();
        int rotorsCount = machine.getRotorsCount();
        if(rotorsCount > rotors.size() || rotorsCount > 99 || rotorsCount < 2) {
            //throws invalid rotorCount
            int y = 4;
        }
        boolean[] rotorsCheckers = new boolean[rotors.size() + 1];
        for(CTERotor r : rotors){
            int id = r.getId();
            if(id > rotors.size() || id <= 0) {
                //throws invalid rotor id
                int m = 1;
            }
            if(rotorsCheckers[id]) {
                //throws not unique id
                int a = 0;
            }
            rotorsCheckers[id] = true;
            if(r.getNotch() > machine.getABC().trim().length() || r.getNotch() <= 0) {
                //throws invalid notch location
                int b = 4;
            }
            if(!checkUniquePositioning(r)){
                //throws not unique permutation
                int y=8;
            }
        }
    }
    private void validateReflectors(){
        List<CTEReflector> reflectors = machine.getCTEReflectors().getCTEReflector();
        Map<String, Integer> ID2Cnt = new HashMap<>();
        for(CTEReflector r : reflectors){
            if(ID2Cnt.getOrDefault(r.getId(), 0) == 0 || checkValidReflectorID(r.getId())) {
                //need to implement checkValidReflectorID!!!
                ID2Cnt.put(r.getId(), 1);
            }
            else{
                //throws invalid id reflector
                int m = 1 ;
            }
            List<CTEReflect> reflector = r.getCTEReflect();
            for(CTEReflect reflection : reflector) {
                if(reflection.getInput() == reflection.getOutput()) {
                    //throws invalid reflector (reflect to itself)
                    int m = 1;
                }
                int abcLen = machine.getABC().length();
                if(reflection.getInput() > abcLen || reflection.getOutput() > abcLen) {
                    //throws invalid reflector (out of range)
                    int m = 1 ;
                }
            }
        }
    }
    private void validateABC(){
        String abc = machine.getABC().trim();
        if(abc.length()%2 == 1) {
            //throws odd abc
            int x = 5; //to delete
        }
        if(!checkUniqueString(abc)){
            //throws invalid abc
            int z = 1; //to delete
        }
    }
    private boolean checkUniqueString(String s) {
        Map<Character, Integer> char2cnt = new HashMap<>();
        for(Character c : s.toCharArray()){
            if(char2cnt.getOrDefault(c,0) == 0) {
                char2cnt.put(c, 1);
            }
            else {
                return false;
            }
        }
        return true;
    }
    private boolean checkUniquePositioning(CTERotor r){
        Map<String, Integer> char2CntLEFT = new HashMap<>();
        Map<String, Integer> char2CntRIGHT = new HashMap<>();
        List<CTEPositioning> rotorPermutations = r.getCTEPositioning();
        for(CTEPositioning p : rotorPermutations) {
            String left = p.getLeft();
            String right = p.getRight();
            if(left.length() != 1 || right.length() != 1){
                return false;
            }
            if(char2CntLEFT.getOrDefault(left,0) == 0) {
                char2CntLEFT.put(left, 1);
            }
            else {
                //throws not unique permutatuin
                return false;
            }
            if(char2CntRIGHT.getOrDefault(right,0) == 0) {
                char2CntRIGHT.put(right, 1);
            }
            else {
                //throws not unique permutatuin
                return false;
            }
        }
        return true;
    }

    private boolean checkValidReflectorID(String ID) {
        //need to implemet!!!!!!!!!!!!!!!!!!!!!!!!!!
        return true;
    }
}
