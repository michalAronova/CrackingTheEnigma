package engine.validator;

import schema.generated.CTEDecipher;

public class DecipherValidator implements Validator {

    CTEDecipher cteDecipher;
    public DecipherValidator(CTEDecipher cteDecipher){
        this.cteDecipher = cteDecipher;
    }
    @Override
    public boolean validate() {
        return true;
    }
}
