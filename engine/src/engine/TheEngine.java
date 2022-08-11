package engine;

import DTO.codeObj.CodeObj;
import engine.stock.Stock;
import engine.validator.MachineValidator;
import engine.validator.Validator;
import enigmaMachine.Machine;
import enigmaMachine.keyBoard.KeyBoard;
import enigmaMachine.plugBoard.PlugBoard;
import enigmaMachine.plugBoard.Plugs;
import enigmaMachine.reflector.Reflecting;
import enigmaMachine.reflector.Reflector;
import enigmaMachine.reflector.ReflectorID;
import enigmaMachine.rotor.Rotor;
import enigmaMachine.secret.Secret;
import exceptions.InputException.InputException;
import exceptions.XMLException.InvalidXMLException;
import exceptions.XMLException.XMLExceptionMsg;
import javafx.util.Pair;
import schema.generated.CTEEnigma;
import schema.generated.CTEMachine;
import DTO.techSpecs.TechSpecs;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class TheEngine implements Engine {
    private Stock stock;
    private Machine machine;
    private CodeObj currentCode;
    private int processedMsgsCnt;
    private final static String JAXB_XML_PACKAGE_NAME = "schema.generated";

    @Override
    public boolean loadDataFromXML(String path) {
        try {
            if(!path.endsWith(".xml")) {
                throw new InvalidXMLException(XMLExceptionMsg.INVALIDFILE, "not an XML");
            }
            File file = new File(path);
            InputStream inputStream = new FileInputStream(file);
            CTEEnigma enigma = deserializeFrom(inputStream);
            CTEMachine machine = enigma.getCTEMachine();
            Validator validator = new MachineValidator(machine);
            try{
                validator.validate();
                stock = new Stock(machine.getCTERotors().getCTERotor(), machine.getCTEReflectors().getCTEReflector(),
                        new KeyBoard(machine.getABC().trim().toUpperCase()), machine.getRotorsCount());
                this.machine = new Machine(stock.getKeyBoard(), stock.getRotorsCount());
                return true;
            } catch (InvalidXMLException e) {
                throw e;
            }
        } catch (JAXBException | FileNotFoundException e) {
            throw new InvalidXMLException(XMLExceptionMsg.INVALIDFILE, "file not found");
        }
    }

    @Override
    public void SetMachine(CodeObj machineCode){
        machine.updateBySecret(SecretFromCodeObj(machineCode));
    }

    private Secret SecretFromCodeObj(CodeObj machineCode){
        List<Rotor> rotors = new ArrayList<>();
        for(Pair<Integer,Character> id2start: machineCode.getID2PositionList()){
            Rotor rotor = stock.getRotorMap().get(id2start.getKey());
            rotor.setInitialPosition(id2start.getValue());
            rotors.add(rotor);
        }

        Reflecting reflector = stock.getReflectorMap().get(machineCode.getReflectorID());
        Plugs plugBoard = new PlugBoard(machineCode.getPlugs(), stock.getKeyBoard());
        return new Secret(rotors, reflector, plugBoard);
    }

    @Override
    public String processMsg(String msg){
        msg = msg.toUpperCase();
        StringBuilder sb = new StringBuilder();
        if(!stock.getKeyBoard().isInKeyBoard(msg)) {
            //edit the exception: able to get the invalid character and tell user which char wasnt in the abc
            //msg to user will be like "char <x> not recognized in machine"...
            throw new InputException("Invalid input: not a recognized character");
        }
        for(Character c : msg.toCharArray()){
            if(c == ' '){
                sb.append(c);
            }
            else {
                sb.append(machine.process(c));
            }
        }
        return sb.toString();
    }

    public CodeObj autoGenerateCodeObj(){
        String reflectorID = raffleReflector();
        List<Pair<Character, Character>> plugs = rafflePlugs();
        List<Pair<Integer, Character>> rotorsID2Position = raffleRotors();
        return new CodeObj(rotorsID2Position,reflectorID, plugs);
    }

    private String raffleReflector() {
        int reflectorCount = stock.getReflectorMap().size();
        Random random = new Random();
        int generated = random.nextInt(reflectorCount);
        return ReflectorID.values()[generated].toString();
    }

    private List<Pair<Character, Character>> rafflePlugs() {
        List<Pair<Character, Character>> plugs = new ArrayList<>();
        int maxPlugs = stock.getKeyBoard().length() / 2;
        Random random = new Random();
        int plugsAmount = random.nextInt(maxPlugs + 1); //how many plugs will there be? (0 - length/2)
        int curRandom;
        char first, second;
        List<Character> keys = stock.getKeyBoard().getAsCharList();
        for(int i = 0; i < plugsAmount; i++){
            curRandom = random.nextInt(keys.size());
            first = keys.get(curRandom);
            keys.remove(curRandom);
            curRandom = random.nextInt(keys.size());
            second = keys.get(curRandom);
            keys.remove(curRandom);
            plugs.add(new Pair<>(first, second));
        }
        return plugs;
    }

    private List<Pair<Integer,Character>> raffleRotors() {
        List<Pair<Integer,Character>> raffledRotors = new ArrayList<>();
        List<Integer> ids = new LinkedList<>(stock.getRotorMap().keySet());
        Random random = new Random();
        int startingIndex, rotorIDidx, rotorID;
        char startingChar;

        for(int i = 0; i < stock.getRotorsCount(); i++){
            rotorIDidx = random.nextInt(ids.size());
            rotorID = ids.get(rotorIDidx);
            ids.remove(rotorIDidx);
            startingIndex = random.nextInt(stock.getKeyBoard().length());
            startingChar = stock.getRotorMap().get(rotorID).getRightPermutation().charAt(startingIndex);
            raffledRotors.add(new Pair<>(rotorID, startingChar));
        }
        return raffledRotors;
    }

    private CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(in);
    }

    public TechSpecs showTechSpecs() {
        return new TechSpecs(stock.getRotorMap().size(), stock.getRotorsCount(), stock.getNotches(),
                stock.getReflectorMap().size(), processedMsgsCnt, currentCode);
    }

}