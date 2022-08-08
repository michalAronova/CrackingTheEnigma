package engine;

import DTO.techSpecs.TechSpecs;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface Engine {
    void loadDataFromXML(String path) throws JAXBException, FileNotFoundException;
    TechSpecs showTechSpecs();

}
