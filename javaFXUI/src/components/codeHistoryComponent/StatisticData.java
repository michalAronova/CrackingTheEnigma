package components.codeHistoryComponent;

import DTO.codeHistory.Translation;
import DTO.codeObj.CodeObj;

public class StatisticData {
    private String code;
    private String input;
    private String output;
    private Double time;

    public StatisticData(String code, String input, String output, Double time) {
        this.code = code;
        this.input = input;
        this.output = output;
        this.time = time;
    }
    public StatisticData(CodeObj codeObj, Translation translation) {
        code = codeObj.toString();
        input = translation.getInput();
        output = translation.getOutput();
        time = translation.getTime();
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getInput() { return input; }

    public String getOutput() { return output; }

    public Double getTime() { return time; }
}


/*
public class Statistic {
    private SimpleStringProperty code;
    private SimpleStringProperty input;
    private SimpleStringProperty output;
    private SimpleDoubleProperty time;

    public Statistic(){
        code = new SimpleStringProperty("some code");
        input = new SimpleStringProperty("hello");
        output = new SimpleStringProperty("world");
        time = new SimpleDoubleProperty(25);
    }

    public Statistic(SimpleStringProperty code, SimpleStringProperty input, SimpleStringProperty output, SimpleDoubleProperty time) {
        this.code = code;
        this.input = input;
        this.output = output;
        this.time = time;
    }

    /*public Statistic(CodeObj codeObj, Translation translation){
        code = codeObj.toString();
        input = new SimpleStringProperty(translation.getInput());
        output = new SimpleStringProperty(translation.getOutput());
        time = new SimpleDoubleProperty(translation.getTime());
    }*/
 /*   public String getCode() { return code.get(); }

    public void setCode(String code) { this.code.set(code); }

    public String getInput() { return input.get(); }

    public String getOutput() { return output.get(); }

    public Double getTime() { return time.get(); }
}
*/