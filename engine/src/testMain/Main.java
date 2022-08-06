package testMain;

import engine.Engine;
import engine.TechSpecs;
import engine.TheEngine;
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

public class Main {

    public static void main(String[] args) {
        Engine engine = new TheEngine();
        engine.loadDataFromXML("C:\\Downloads\\ex1-sanity-small.xml");
        TechSpecs ts = engine.showTechSpecs();
        System.out.println(ts.getTotalRotors());
        System.out.println(ts.getNotchLocation());

    }
}
