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
import enigmaMachine.reflector.ReflectorID;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Scanner;

public class ConsoleUserInterface implements UserInterface {
    private Engine engine;
    private boolean XMLLoaded = false;
    private boolean codeLoaded = false;

    public ConsoleUserInterface() {
        this.engine = new TheEngine();
    }

    private final static String NO_XML_MESSAGE = "You haven't loaded an XML yet... Please do that before performing this action!";
    private final static String NO_CODE_MESSAGE = "You haven't set the machine by code yet... Please do that before performing this action!";

    @Override
    public void run(){
        System.out.println(String.format("%nWelcome to the enigma machine simulator"));
        manageMenu();
    }

    private void manageMenu(){
        showMenuByEnum(MenuChoice.values());
        int choice = getValidChoice(MenuChoice.values().length);
        MenuChoice menuChoice = MenuChoice.valueByInt(choice);
        switch(menuChoice){
            case LOAD_XML:
                loadDataFromXML();
                manageMenu();
                break;
            case SHOW_DETAILS:
                showMachineDetails();
                manageMenu();
                break;
            case MANUAL_SET:
                manuallySetMachineCode();
                manageMenu();
                break;
            case AUTO_SET:
                autoSetMachineCode();
                manageMenu();
                break;
            case PROCESS:
                processInput();
                manageMenu();
                break;
            case RESET:
                resetMachine();
                manageMenu();
                break;
            case HISTORY_AND_STATISTICS:
                showHistoryAndStatistics();
                manageMenu();
                break;
            case SAVE_MACHINE_TO_FILE:
                saveMachineToFile();
                manageMenu();
                break;
            case LOAD_MACHINE_FROM_FILE:
                loadMachineFromFile();
                manageMenu();
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

    private <T extends Enum> void showMenuByEnum(T[] enumValues){
        System.out.println(String.format("Please specify your choice by number (1 - %d)", enumValues.length));
        int index = 1;
        for (T enumVal: enumValues) {
            System.out.println(String.format("%d. %s", index, enumVal));
            ++index;
        }
    }

    private <T extends Enum> void showMenuByEnum(T[] enumValues, int limit) {
        System.out.println(String.format("Please specify your choice by number (1 - %d)", limit));
        int index = 1;
        for (T enumVal : enumValues) {
            System.out.println(String.format("%d. %s", index, enumVal));
            ++index;
            if (index > limit) {
                return;
            }
        }
    }

    private Integer getValidChoice(int maxChoiceNumber) {
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
        if (!validateAndSetRotors(underConstructionCode)) {
            return;
        }
        if(!validateAndSetRotorPositions(underConstructionCode)){
            return;
        }
        if (!validateAndSetReflector(underConstructionCode)) {
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
                System.out.println("For example, rotor 3 on the left and rotor 1 on the right should be entered as: 3,1");
                System.out.println(String.format("Valid options: %d - %d", 1, engine.getTotalRotorAmount()));
                rotorIDs = s.nextLine();
                engine.validateAndSetRotors(underConstructionCode, rotorIDs);
                actionCompleted = true;
                System.out.println("Rotor IDs set successfully.");
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
                System.out.println("For example, position C on the left A on the right should be entered as: CA");
                rotorPositions = s.nextLine();
                engine.validateAndSetRotorPositions(underConstructionCode, rotorPositions);
                actionCompleted = true;
                System.out.println("Rotor positions set successfully.");
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

    private boolean validateAndSetReflector(CodeObj underConstructionCode) {
        boolean actionCompleted = false;
        Scanner s = new Scanner(System.in);
        String reflector;
        do {
            try {
                System.out.println("Setting your reflector: ");
                showMenuByEnum(ReflectorID.values(), engine.getReflectorCount());
                reflector = s.nextLine();
                engine.validateAndSetReflector(underConstructionCode, reflector);
                actionCompleted = true;
                System.out.println("Reflector set successfully.");
            }
            catch(InputException e) {
                System.out.println(e.getMessage());
                if(!wantsToRetry()){
                    break;
                }
            } catch (NumberFormatException numberException) {
                System.out.println("Wrong input format: please enter a single number");
                if (!wantsToRetry()) {
                    break;
                }
            }
        } while (!actionCompleted);

        return actionCompleted;
    }

    private boolean validateAndSetPlugs(CodeObj underConstructionCode){
        boolean actionCompleted = false;
        Scanner s = new Scanner(System.in);
        String plugs;

        do{
            try{
                System.out.println("Setting your plugs: enter a continuous string of the pairs you wish to plug.");
                System.out.println("For example, the pairs <A|F,B|C> should be entered as: AFBC");
                System.out.println("For no plugs, press ENTER");
                System.out.println("Valid options: " + engine.getKeyBoardList());
                plugs = s.nextLine();
                engine.validateAndSetPlugs(underConstructionCode, plugs);
                actionCompleted = true;
                System.out.println("Plugs set successfully.");
            } catch (InputException e) {
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
        System.out.println("Please note your keyboard includes: " + engine.getKeyBoardList());
        do {
            try {
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
        System.out.println(String.format("Processed message: %n%s%n",output));
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
        histories.forEach(System.out::println);
    }

    @Override
    public void exit() {
        System.out.println("Alright, bye!");
    }

    public void saveMachineToFile(){
        System.out.println("Please enter path to file you want to save this machine to");
        Scanner s = new Scanner(System.in);
        String fileName = s.nextLine();
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(fileName))) {
            out.writeObject(this.engine);
            out.flush();
            System.out.println("Saved successfully to file.");
        }
        catch(IOException e){
            System.out.println("IOException occurred. Returning to Main Menu.");
        }
    }

    public void loadMachineFromFile() {
        System.out.println("Please enter path to file you want to load this machine from");
        Scanner s = new Scanner(System.in);
        String fileName = s.nextLine();
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(fileName))) {
            this.engine = (TheEngine) in.readObject();
            XMLLoaded = engine.isStockLoaded();
            codeLoaded = engine.getInitialCode() != null;
            System.out.println("Loaded successfully from file.");
        }
        catch(FileNotFoundException e){
            System.out.println("Error: file not found. Returning to Main Menu.");
        }
        catch(IOException e){
            System.out.println("IOException occurred. Returning to Main Menu.");
        }
        catch(ClassNotFoundException e){
            System.out.println("ClassNotFoundException occurred. Returning to Main Menu.");
        }
        catch(SecurityException e){
            System.out.println("Error: you do not have permissions to this file. Returning to Main Menu.");
        }
    }
}