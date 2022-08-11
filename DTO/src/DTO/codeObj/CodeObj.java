package DTO.codeObj;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CodeObj {
    private List<Pair<Integer, Character>> ID2PositionList;
    private String reflectorID;
    private List<Pair<Character, Character>> plugs;
    boolean updatedRotorIds = false;
    boolean updatedRotorPos = false;
    boolean updatedReflector = false;
    boolean updatedPlugs = false;

    public CodeObj() {
        ID2PositionList = new ArrayList<>();
    }

    public CodeObj(List<Pair<Integer, Character>> ID2PositionList, String reflectorID, List<Pair<Character, Character>> plugs){
        this.ID2PositionList = ID2PositionList;
        this.reflectorID = reflectorID;
        this.plugs = plugs;
        updatedRotorIds = true;
        updatedRotorPos = true;
        updatedReflector = true;
        updatedPlugs = true;
    }

    public List<Pair<Integer, Character>> getID2PositionList() {
        return ID2PositionList;
    }

    public String getReflectorID() {
        return reflectorID;
    }

    public List<Pair<Character, Character>> getPlugs() {
        return plugs;
    }

    @Override
    public String toString(){
        //<בחירת וסידור גלגלים בחריצים><בחירת מיקום גלגלים ראשוני><מספר משקף בספרות רומיות><צמדי תקעים>
        //<45,27,94><AO!><III><A|Z,D|E>
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        for(int i = ID2PositionList.size() - 1; i >= 0; i--){
            sb.append(ID2PositionList.get(i).getKey());
            sb.append(",");
        }
        sb.replace(sb.length() - 1, sb.length(),">");
        sb.append("<");
        for(int i = ID2PositionList.size() - 1; i >= 0; i--){
            sb.append(ID2PositionList.get(i).getValue());
        }
        sb.append("><");
        sb.append(reflectorID+">");
        if(plugs.size() > 0){
            sb.append("<");
            plugs.forEach(p -> sb.append(p.getKey()+"|"+p.getValue()+","));
            sb.replace(sb.length() - 1, sb.length(),">");
        }
        return sb.toString();
    }

    public void setID2PositionList(List<Pair<Integer, Character>> ID2PositionList){
        this.ID2PositionList = ID2PositionList;
    }

    public void setRotorIDs(List<Integer> rotorIDs){
        if(!updatedRotorPos){
            for (Integer id: rotorIDs) {
                ID2PositionList.add(new Pair<>(id, null));
            }
        }
        else{
            List<Pair<Integer, Character>> updated = new ArrayList<>();
            for (int i = 0; i < rotorIDs.size() ; i++) {
                updated.add(new Pair<>(rotorIDs.get(i), ID2PositionList.get(i).getValue()));
            }
            ID2PositionList = updated;
        }
        updatedRotorIds = true;
    }

    public void setRotorPos(List<Character> rotorPositions){
        if(!updatedRotorIds){
            for (Character pos: rotorPositions) {
                ID2PositionList.add(new Pair<>(null, pos));
            }
        }
        else{
            List<Pair<Integer, Character>> updated = new ArrayList<>();
            for (int i = 0; i < rotorPositions.size() ; i++) {
                updated.add(new Pair<>(ID2PositionList.get(i).getKey(), rotorPositions.get(i)));
            }
            ID2PositionList = updated;
        }
        updatedRotorPos = true;
    }

    public void setReflectorID(String reflectorID){
        updatedReflector = true;
        this.reflectorID = reflectorID;
    }

    public void setPlugs(List<Pair<Character, Character>> plugs){
        updatedPlugs = true;
        this.plugs = plugs;
    }

    public boolean isValidCode(){
        return updatedRotorIds && updatedRotorPos && updatedReflector && updatedPlugs;
    }
}
