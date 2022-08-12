package DTO.techSpecs;

import DTO.codeObj.CodeObj;

import java.util.Map;

public class TechSpecs {

    private final int totalRotors;
    private final int rotorsInUse;

    private Map<Integer, Integer> noches;
    private final int reflectorsCount;
    private final int processedMsg;
    private final CodeObj currentCode;
    private final CodeObj updatedCode;

    //mimi
    public TechSpecs(int totalRotors, int rotorsInUse, Map<Integer, Integer> noches, int reflectorsCount,
                     int processedMsg, CodeObj currentCode, CodeObj updatedCode) {
        this.totalRotors = totalRotors;
        this.rotorsInUse = rotorsInUse;
        this.noches = noches;//mimi
        this.reflectorsCount = reflectorsCount;
        this.processedMsg = processedMsg;
        this.currentCode = currentCode;
        this.updatedCode = updatedCode;
    }

    public int getTotalRotors() {
        return totalRotors;
    }

    public int getRotorsInUse() {
        return rotorsInUse;
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

    public CodeObj getUpdatedCode() { return updatedCode; }

}
