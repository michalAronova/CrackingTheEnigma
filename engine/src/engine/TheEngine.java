package engine;

import engine.stock.Stock;
import enigmaMachine.keyBoard.KeyBoard;
import schema.generated.CTEEnigma;
import schema.generated.CTEMachine;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TheEngine implements Engine {
    private Stock stock;
    private String currentCode;
    private int processedMsgsCnt;
    private final static String JAXB_XML_PACKAGE_NAME = "schema.generated";

    @Override
    public void loadDataFromXML(String path) {
        try {
            InputStream inputStream = new FileInputStream(new File(path));
            CTEEnigma enigma = deserializeFrom(inputStream);
            CTEMachine machine = enigma.getCTEMachine();
            stock = new Stock(machine.getCTERotors().getCTERotor(), machine.getCTEReflectors().getCTEReflector(), new KeyBoard(machine.getABC()), machine.getRotorsCount());


        } catch (JAXBException | FileNotFoundException e) {
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