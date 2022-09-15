package engine.validator;

import exceptions.InputException.OutOfBoundInputException;
import exceptions.XMLException.InvalidXMLException;
import schema.generated.CTEDecipher;

public class DecipherValidator implements Validator {

    private final int MINIMUM_AGENT_COUNT = 2;
    private final int MAXIMUM_AGENT_COUNT = 50;
    CTEDecipher cteDecipher;
    public DecipherValidator(CTEDecipher cteDecipher){
        this.cteDecipher = cteDecipher;
    }
    @Override
    public boolean validate() {
        if(cteDecipher.getAgents() > MAXIMUM_AGENT_COUNT || cteDecipher.getAgents() < MINIMUM_AGENT_COUNT){
            throw new OutOfBoundInputException("Invalid agent count in XML: " + cteDecipher.getAgents(),
                                                MINIMUM_AGENT_COUNT, MAXIMUM_AGENT_COUNT);
        }
        return true;
    }
}
