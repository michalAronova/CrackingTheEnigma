package DTO.codeObj;

import javafx.util.Pair;
import java.util.List;
import java.util.Map;

public class CodeObj {
    List<Pair<Integer, Character>> ID2PositionList;
    String reflectorID;
    List<Pair<Character, Character>> plugs;

    public CodeObj(List<Pair<Integer, Character>> ID2PositionList, String reflectorID, List<Pair<Character, Character>> plugs) {
        this.ID2PositionList = ID2PositionList;
        this.reflectorID = reflectorID;
        this.plugs = plugs;
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
}
