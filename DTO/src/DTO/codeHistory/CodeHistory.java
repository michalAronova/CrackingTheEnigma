package DTO.codeHistory;
import DTO.codeObj.CodeObj;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CodeHistory {
    private CodeObj code;
    private List<Translation> inputOutput;

    public CodeHistory(CodeObj code){
        inputOutput = new LinkedList<>();
        this.code = code;
    }

    public void addTranslation(String input, String output, double time){
        inputOutput.add(new Translation(input, output, time));
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Code: %s %n", code));
        for (int i = 0; i < inputOutput.size(); i++) {
            sb.append(String.format("      %d. %s%n", i+1, inputOutput.get(i).toString()));
        }
        return sb.toString();
    }
}
