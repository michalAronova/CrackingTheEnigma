package engine;

import DTO.techSpecs.TechSpecs;

public interface Engine {
    void loadDataFromXML(String path);
    TechSpecs showTechSpecs();

}
