package exceptions.InputException;

import org.omg.CORBA.Environment;

import java.util.ArrayList;
import java.util.List;

public class ObjectInputException extends InputException {
    private final List<Object> validValues;

    public ObjectInputException(String message, int value){
        super(message);
        this.validValues = new ArrayList<>();
        validValues.add(value);
    }

    public ObjectInputException(String message, List<Object> validValues){
        super(message);
        this.validValues = validValues;
    }

    @Override
    public String toString(){
        return super.toString() + " | The valid values are: " + validValues;
    }
    @Override
    public String getMessage(){
        return this.toString();
    }

    public Object getValidValuesList(){ return validValues; }
}
