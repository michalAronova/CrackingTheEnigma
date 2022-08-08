package testMain;

import DTO.codeObj.CodeObj;
import engine.Engine;
import DTO.techSpecs.TechSpecs;
import engine.TheEngine;
import enigmaMachine.secret.Secret;
import exceptions.XMLException.InvalidXMLException;
import exceptions.XMLException.XMLExceptionMsg;

public class Main {

    public static void main(String[] args) {
        Engine engine = new TheEngine();
        try{
            //return bool if loading succeed, later should notify ui for printing msg accordingly (success!)
            if(engine.loadDataFromXML("C:\\Downloads\\ex1-sanity-small.xml")) {
                //TechSpecs ts = engine.showTechSpecs();
                //System.out.println(ts.getTotalRotors());
                //System.out.println(ts.getNotchLocation());
            }
        }
        catch(InvalidXMLException e) {
            System.out.println(e.getMessage());
        }
        CodeObj code = engine.autoGenerateCodeObj();
        engine.SetMachine(code);
        System.out.println("Machine code: " + code);
        String myString = "abc abc";
        String result = engine.processMsg(myString);
        System.out.println("input  -> " + myString);
        System.out.println("output -> " + result);
    }
}
