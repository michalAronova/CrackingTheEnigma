package testMain;

import engine.Engine;
import DTO.techSpecs.TechSpecs;
import engine.TheEngine;
import exceptions.XMLException.InvalidXMLException;
import exceptions.XMLException.XMLExceptionMsg;

public class Main {

    public static void main(String[] args) {
        Engine engine = new TheEngine();
        try{
            //return bool if loading succeed, later should notify ui for printing msg accordingly (success!)
            if(engine.loadDataFromXML("C:\\Downloads\\ex1-error-3.xml")) {
                TechSpecs ts = engine.showTechSpecs();
                System.out.println(ts.getTotalRotors());
                System.out.println(ts.getNotchLocation());
            }
        }
        catch(InvalidXMLException e) {
            System.out.println(e.getMessage());
        }
    }
}
