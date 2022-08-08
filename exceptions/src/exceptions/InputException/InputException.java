package exceptions.InputException;

public class InputException extends RuntimeException {
    private String message;

    public InputException(String message){
        this.message = message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
