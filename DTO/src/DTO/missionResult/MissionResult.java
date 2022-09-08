package DTO.missionResult;

import DTO.codeObj.CodeObj;

import java.util.List;

public class MissionResult {
    List<CodeObj> candidates;
    long time;

    public MissionResult(List<CodeObj> candidates, long time) {
        this.candidates = candidates;
        this.time = time;
    }

    public List<CodeObj> getCandidates() {
        return candidates;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Time taken: "+ time);
        sb.append(System.lineSeparator());
        sb.append(candidates);
        return sb.toString();
    }
}
