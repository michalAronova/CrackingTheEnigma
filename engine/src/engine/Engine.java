package engine;

import DTO.codeObj.CodeObj;
import DTO.techSpecs.TechSpecs;
import enigmaMachine.secret.Secret;
import javafx.util.Pair;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.List;

public interface Engine {
    boolean loadDataFromXML(String path);
    //boolean foo(CodeObj machineCode);
    TechSpecs showTechSpecs();
    CodeObj autoGenerateCodeObj();
    void SetMachine(CodeObj machineCode);
    String processMsg(String msg);
}
