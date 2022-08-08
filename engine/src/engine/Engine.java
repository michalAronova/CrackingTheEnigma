package engine;

import DTO.techSpecs.TechSpecs;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public interface Engine {
    boolean loadDataFromXML(String path);
    TechSpecs showTechSpecs();

}
