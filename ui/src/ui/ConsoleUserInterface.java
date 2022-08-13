package ui;

import DTO.codeHistory.CodeHistory;
import DTO.codeObj.CodeObj;
import DTO.techSpecs.TechSpecs;
import com.sun.org.apache.bcel.internal.classfile.Code;
import engine.Engine;
import engine.TheEngine;
import exceptions.InputException.InputException;
import exceptions.InputException.ObjectInputException;
import exceptions.InputException.OutOfBoundInputException;
import exceptions.XMLException.InvalidXMLException;

import java.util.List;
import java.util.Scanner;

public class ConsoleUserInterface implements UserInterface{
    private final Engine engine;
    private boolean XMLLoaded = false;
    private boolean codeLoaded = false;

    public ConsoleUserInterface() {
        this.engine = new TheEngine();
    }

    private final static String NO_XML_MESSAGE = "Oops! You haven't loaded an XML yet... Please do that before performing this action!";
    private final static String NO_CODE_MESSAGE = "Oops! You haven't set the machine by code yet... Please do that before performing this action!";
    @Override
    public void run(){
        System.out.println(String.format("%nWelcome to the enigma machine simulator"));
        showMenuByEnum(MenuChoice.values());
        int choice = getValidChoice(MenuChoice.values().length);
        MenuChoice menuChoice = MenuChoice.valueByInt(choice);
        switch(menuChoice){
            case LOAD_XML:
                loadDataFromXML();
                run();
                break;
            case SHOW_DETAILS:
                showMachineDetails();
                run();
                break;
            case MANUAL_SET:
                manuallySetMachineCode();
                run();
                break;
            case AUTO_SET:
                autoSetMachineCode();
                run();
                break;
            case PROCESS:
                processInput();
                run();
                break;
            case RESET:
                resetMachine();
                run();
                break;
            case HISTORY_AND_STATISTICS:
                showHistoryAndStatistics();
                run();
                break;
            case EXIT:
                exit();
                break;
        }
    }

    private boolean wantsToRetry(){
        Scanner s = new Scanner(System.in);
        System.out.println("press 'Q' to return to the main menu, or any other character to retry");
        return !(s.nextLine().toUpperCase().contains("Q"));
    }

    private boolean containsNumbersOnly(String s){
        if(s == null || s.equals("")){
            return false;
        }
        for (Character c: s.toCharArray()) {
            if(!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }

    private void showMenuByEnum(Enum[] enumValues){
        System.out.println(String.format("Please specify your choice by number (1 - %d)", enumValues.length));
        for (Enum enumVal: enumValues) {
            System.out.println(enumVal);
        }
    }

    private int getValidChoice(int maxChoiceNumber){
        boolean receivedValid = false;
        Scanner s = new Scanner(System.in);
        String choice;

        do{
            choice = s.nextLine();
            receivedValid = containsNumbersOnly(choice);
            if(!receivedValid){
                System.out.println("Invalid choice. Choice must be a number.");
            } else if (Integer.parseInt(choice) < 1 || Integer.parseInt(choice) > maxChoiceNumber) {
                System.out.println(String.format("Invalid choice. " +
                        "Valid choices are a number between 1 - %d", maxChoiceNumber));
                receivedValid = false;
            }
        }while(!receivedValid);
        return Integer.parseInt(choice);
    }

    @Override //this method needs to be available ALWAYS. How do we do that?
    public void loadDataFromXML() {
        Scanner s = new Scanner(System.in);
        String path;
        do{
            try{
                System.out.println("Please enter a full path to your desired XML to load the machine.");
                path = s.nextLine();
                engine.loadDataFromXML(path);
                XMLLoaded = true;
                codeLoaded = false; //new XML - previous code is KAPOOT
            }
            catch(InvalidXMLException e) {
                System.out.println(e.getMessage());
                if(!wantsToRetry()){
                    break;
                }
            }
        }while(!XMLLoaded);
        if(XMLLoaded){
            System.out.println("Machine loaded successfully from XML.");
        }
    }

    @Override
    public void showMachineDetails() {
        if(!XMLLoaded){
            System.out.println(NO_XML_MESSAGE);
            return;
        }
        TechSpecs TS = engine.showTechSpecs();
        System.out.println("Machine Details:");
        System.out.println(TS);
    }

    @Override
    public void manuallySetMachineCode() {
        if(!XMLLoaded){
            System.out.println(NO_XML_MESSAGE);
            return;
        }
        CodeObj underConstructionCode = new CodeObj();

        //for each step, the user may choose to "forfeit" and go back to main menu without setting a new code.
        //if such occurs, the validate methods will return false, thus we will simply return upon getting false.
        if(!validateAndSetRotors(underConstructionCode)){
            return;
        }
        if(!validateAndSetRotorPositions(underConstructionCode)){
            return;
        }
        if(!validateAndSetReflector(underConstructionCode)){ //NOT DONE, PROBLEM WITH REFLECTORID.. SHOULD IT BE DEPENDANT?
            return;
        }
        if(!validateAndSetPlugs(underConstructionCode)){
            return;
        }
        engine.setMachine(underConstructionCode);
        announceCodeMachineSet();
    }

    private boolean validateAndSetRotors(CodeObj underConstructionCode){
        boolean actionCompleted = false;
        Scanner s = new Scanner(System.in);
        String rotorIDs;

        do{
            try{
                System.out.println(String.format("Setting your rotor IDs: please choose %d rotors by ID, " +
                        "from left to right, seperated by a comma.", engine.getRotorsCount()));
                System.out.println("For example, rotor 3 on the left and rotor 1 on the right should be entered as '3,1'");
                rotorIDs = s.nextLine();
                engine.validateAndSetPlugs(underConstructionCode, rotorIDs);
                actionCompleted = true;
            }
            catch(InputException e) {
                System.out.println(e.getMessage());
                if(!wantsToRetry()){
                    break;
                }
            }
        }while(!actionCompleted);

        return actionCompleted;
    }

    private boolean validateAndSetRotorPositions(CodeObj underConstructionCode){
        boolean actionCompleted = false;
        Scanner s = new Scanner(System.in);
        String rotorPositions;

        do{
            try{
                System.out.println(String.format("Setting your rotor positions: please choose %d rotor positions by character, from left to right.", engine.getRotorsCount()));
                System.out.println("For example, position C on the left A on the right should be entered as 'CA'");
                rotorPositions = s.nextLine();
                engine.validateAndSetPlugs(underConstructionCode, rotorPositions);
                actionCompleted = true;
            }
            catch(InputException e) {
                System.out.println(e.getMessage());
                if(!wantsToRetry()){
                    break;
                }
            }
        }while(!actionCompleted);

        return actionCompleted;
    }

    //NOT DONE, PROBLEM WITH REFLECTORID ENUM.. SHOULD IT BE DEPENDANT?
    private boolean validateAndSetReflector(CodeObj underConstructionCode){
        boolean actionCompleted = false;
        Scanner s = new Scanner(System.in);
        String reflector;
        int reflectorID = 1;
        do{
            try{
                System.out.println("Setting your reflector: ");
                //showMenuByEnum(ReflectorID.values());
                reflector = s.nextLine();
                //reflectorID = getValidChoice(ReflectorID.values().length)
                engine.validateAndSetReflector(underConstructionCode, reflectorID);
                actionCompleted = true;
            }
            catch(InputException e) {
                System.out.println(e.getMessage());
                if(!wantsToRetry()){
                    break;
                }
            }
        }while(!actionCompleted);

        return actionCompleted;
    }

    private boolean validateAndSetPlugs(CodeObj underConstructionCode){
        boolean actionCompleted = false;
        Scanner s = new Scanner(System.in);
        String plugs;

        do{
            try{
                System.out.println("Setting your plugs: enter a continuous string of the pairs you wish to plug.");
                System.out.println("For example, the pairs <A|F,B|C> should be entered as 'AFBC'");
                plugs = s.nextLine();
                engine.validateAndSetPlugs(underConstructionCode, plugs);
                actionCompleted = true;
            }
            catch(InputException e) {
                System.out.println(e.getMessage());
                if(!wantsToRetry()){
                    break;
                }
            }
        }while(!actionCompleted);

        return actionCompleted;
    }

    @Override
    public void autoSetMachineCode() {
        if(!XMLLoaded){
            System.out.println(NO_XML_MESSAGE);
            return;
        }
        engine.setByAutoGeneratedCode();
        announceCodeMachineSet();
    }

    private void announceCodeMachineSet(){
        System.out.println(String.format("Machine set successfully. Code is: %n%s%n", engine.getInitialCode()));
        codeLoaded = true;
    }

    @Override
    public void processInput() {
        if(!XMLLoaded){
            System.out.println(NO_XML_MESSAGE);
            return;
        }
        if(!codeLoaded){
            System.out.println(NO_CODE_MESSAGE);
            return;
        }
        boolean validInput = false;
        Scanner s = new Scanner(System.in);
        String input, output = "";

        do{
            try{
                System.out.println("Insert desired input below:");
                input = s.nextLine();
                output = engine.processMsg(input);
                validInput = true;
            }
            catch(InputException e) {
                System.out.println(e.getMessage());
                if(!wantsToRetry()){
                    break;
                }
            }
        }while(!validInput);
        System.out.println("Processed message: "+ output);
    }

    @Override
    public void resetMachine() {
        if(!XMLLoaded){
            System.out.println(NO_XML_MESSAGE);
            return;
        }
        if(!codeLoaded){
            System.out.println(NO_CODE_MESSAGE);
            return;
        }

        engine.resetMachine();
        System.out.println("Machine reset successfully.");
    }

    @Override
    public void showHistoryAndStatistics() {
        if(!XMLLoaded){
            System.out.println(NO_XML_MESSAGE);
            return;
        }
        List<CodeHistory> histories = engine.showCodeHistory();
        if(histories.isEmpty()){
            System.out.println("No codes set on this machine yet, no history to show.");
            return;
        }
        for (CodeHistory codeHistory: histories) {
            System.out.println(codeHistory);
        }
    }

    @Override
    public void exit() {
        System.out.println("Alright, bye!");
    }
}
