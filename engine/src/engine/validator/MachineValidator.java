package engine.validator;

import enigmaMachine.reflector.ReflectorID;
import exceptions.XMLException.InvalidXMLException;
import exceptions.XMLException.XMLExceptionMsg;
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
        validateABC();
        validateRotorsAndCount(); // continue to this method only if abc is valid :)
        validateReflectors(); // continue to this method only if abc is valid :)
        return true;
    }
    //must call validateABC before this method
    private void validateRotorsAndCount(){
        List<CTERotor> rotors = machine.getCTERotors().getCTERotor();
        int rotorsCount = machine.getRotorsCount();
        if(rotorsCount > rotors.size() || rotorsCount > 99 || rotorsCount < 2) {
            throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "invalid rotorCount");
        }
        boolean[] rotorsCheckers = new boolean[rotors.size() + 1];
        for(CTERotor r : rotors){
            int id = r.getId();
            if(id > rotors.size() || id <= 0) {
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "id out of range");
            }
            if(rotorsCheckers[id]) {
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "more than one rotor with same ID");
            }
            rotorsCheckers[id] = true;
            if(r.getNotch() > machine.getABC().trim().length() || r.getNotch() <= 0) {
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "notch location out of range");
            }
            checkPositioning(r); //throws exception if not valid positioning
        }
    }
    private void validateReflectors(){
        List<CTEReflector> reflectors = machine.getCTEReflectors().getCTEReflector();
        Map<String, Integer> ID2Cnt = new HashMap<>();
        for(CTEReflector r : reflectors){
            if(ID2Cnt.getOrDefault(r.getId(), 0) == 0 || checkValidReflectorID(r.getId())) {
                ID2Cnt.put(r.getId(), 1);
            }
            else{
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDREFLECTOR, "invalid reflector ID");
            }
            List<CTEReflect> reflector = r.getCTEReflect();
            for(CTEReflect reflection : reflector) {
                if(reflection.getInput() == reflection.getOutput()) {
                    throw new InvalidXMLException(XMLExceptionMsg.INVALIDREFLECTOR, "self reflecting");
                }
                int abcLen = machine.getABC().trim().length();
                if(reflection.getInput() > abcLen || reflection.getOutput() > abcLen) {
                    throw new InvalidXMLException(XMLExceptionMsg.INVALIDREFLECTOR, "reflection value out of range");
                }
            }
        }
    }
    private void validateABC(){
        String abc = machine.getABC().trim();
        if(abc.length()%2 == 1) {
            throw new InvalidXMLException(XMLExceptionMsg.INVALIDABC, "odd abc");
        }
        if(!checkUniqueString(abc)){
            throw new InvalidXMLException(XMLExceptionMsg.INVALIDABC, "abc not unique");
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
    private void checkPositioning(CTERotor r){
        Map<String, Integer> char2CntLEFT = new HashMap<>();
        Map<String, Integer> char2CntRIGHT = new HashMap<>();
        List<CTEPositioning> rotorPermutations = r.getCTEPositioning();
        for(CTEPositioning p : rotorPermutations) {
            String left = p.getLeft();
            String right = p.getRight();
            if(left.length() != 1 || right.length() != 1){
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "not a single character in positioning");
            }
            if(!machine.getABC().trim().contains(left) || !machine.getABC().trim().contains(right)){
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "positioning value not in abc");
            }
            if(char2CntLEFT.getOrDefault(left,0) == 0) {
                char2CntLEFT.put(left, 1);
            }
            else {
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "double mapping");
            }
            if(char2CntRIGHT.getOrDefault(right,0) == 0) {
                char2CntRIGHT.put(right, 1);
            }
            else {
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDROTOR, "double mapping");
            }
        }
    }

    private boolean checkValidReflectorID(String ID) {
        for(ReflectorID r : ReflectorID.values()){
            if (r.name().equals(ID)) {
                return true;
            }
        }
        return false;
    }
}
