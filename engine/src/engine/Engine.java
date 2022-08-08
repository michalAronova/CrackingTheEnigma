package engine;

import DTO.codeObj.CodeObj;
import DTO.techSpecs.TechSpecs;
import enigmaMachine.secret.Secret;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface Engine {
    boolean loadDataFromXML(String path);
    //boolean foo(CodeObj machineCode);
    TechSpecs showTechSpecs();
    CodeObj autoGenerateCodeObj();
    void SetMachine(CodeObj machineCode);
    String processMsg(String msg);
}
