package engine;

import java.util.Map;

public class TechSpecs {

    private int totalRotors;
    private int rotorsInUse;
    private Map<Integer, Integer> notchLocation;
    private int reflectorsCount;
    private int processedMsg;
    private String currentCode;

    public TechSpecs(int totalRotors, int rotorsInUse, Map<Integer, Integer> notchLocation, int reflectorsCount, int processedMsg, String currentCode) {
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

    public String getCurrentCode() {
        return currentCode;
    }
}
