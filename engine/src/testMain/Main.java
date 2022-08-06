package testMain;

import schema.generated.CTEEnigma;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Main {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "schema.generated";

    public static void main(String[] args) {
        try {
            InputStream inputStream = new FileInputStream(new File("C:\\Users\\micha\\IdeaProjects\\CrackingTheEnigma\\engine\\src\\resources\\ex1-sanity-small.xml"));
            CTEEnigma enigma = deserializeFrom(inputStream);
            System.out.println("#rotors: " + enigma.getCTEMachine().getRotorsCount());
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }
}
