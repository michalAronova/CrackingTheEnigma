package engine;

import DTO.codeObj.CodeObj;
import engine.stock.Stock;
import engine.validator.MachineValidator;
import engine.validator.Validator;
import enigmaMachine.keyBoard.KeyBoard;
import schema.generated.CTEEnigma;
import schema.generated.CTEMachine;
import DTO.techSpecs.TechSpecs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TheEngine implements Engine {
    private Stock stock;
    private CodeObj currentCode;
    private int processedMsgsCnt;
    private final static String JAXB_XML_PACKAGE_NAME = "schema.generated";

    @Override
    public void loadDataFromXML(String path) {
        try {
            boolean valid = true;
            if(!path.endsWith(".xml")) {
                //throw not an XML
                valid = false;
            }
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            CTEEnigma enigma = deserializeFrom(inputStream);
            CTEMachine machine = enigma.getCTEMachine();
            Validator validator = new MachineValidator(machine);
            try{
                validator.validate();
                stock = new Stock(machine.getCTERotors().getCTERotor(), machine.getCTEReflectors().getCTEReflector(),
                        new KeyBoard(machine.getABC().trim()), machine.getRotorsCount());
            } catch (Exception e) {/*----------*/
                throw new RuntimeException(e);/*-------------*/
            }
        } catch (JAXBException | FileNotFoundException e) {
            //change treatment of those exceptions!!!!!!!!!!!!!!!!!!!
            e.printStackTrace();
        }
    }
    private CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    public TechSpecs showTechSpecs() {
        return new TechSpecs(stock.getRotorMap().size(), stock.getRotorsCount(), stock.getNotches(),
                stock.getReflectorMap().size(), processedMsgsCnt, currentCode);
    }

}