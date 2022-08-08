package DTO.techSpecs;

import DTO.codeObj.CodeObj;

import java.util.Map;

public class TechSpecs {

    private int totalRotors;
    private int rotorsInUse;
    private Map<Integer, Integer> notchLocation;
    private int reflectorsCount;
    private int processedMsg;
    //change to CodeObj
    private CodeObj currentCode;

    public TechSpecs(int totalRotors, int rotorsInUse, Map<Integer, Integer> notchLocation, int reflectorsCount, int processedMsg, CodeObj currentCode) {
        this.totalRotors = totalRotors;
        this.rotorsInUse = rotorsInUse;
        this.notchLocation = notchLocation;
        this.reflectorsCount = reflectorsCount;
        this.processedMsg = processedMsg;
        this.currentCode = currentCode;
    }

    public int getTotalRotors() {
        return totalRotors;
    }

    public int getRotorsInUse() {
        return rotorsInUse;
    }

    public Map<Integer, Integer> getNotchLocation() {
        return notchLocation;
    }

    public int getReflectorsCount() {
        return reflectorsCount;
    }

    public int getProcessedMsg() {
        return processedMsg;
    }

    public CodeObj getCurrentCode() {
        return currentCode;
    }
}
