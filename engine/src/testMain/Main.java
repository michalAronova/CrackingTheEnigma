package testMain;

import engine.Engine;
import DTO.techSpecs.TechSpecs;
import engine.TheEngine;

public class Main {

    public static void main(String[] args) {
        Engine engine = new TheEngine();
        engine.loadDataFromXML("C:\\Downloads\\ex1-sanity-small.xml");
        TechSpecs ts = engine.showTechSpecs();
        System.out.println(ts.getTotalRotors());
        System.out.println(ts.getNotchLocation());

    }
}
