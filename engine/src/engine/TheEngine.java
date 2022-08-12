package engine;

import DTO.codeHistory.CodeHistory;
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
import exceptions.InputException.ObjectInputException;
import exceptions.InputException.OutOfBoundInputException;
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
    private LinkedList<CodeHistory> codesHistories = new LinkedList<>();
    private final static String JAXB_XML_PACKAGE_NAME = "schema.generated";
    private final static int MIN_ROTOR_COUNT = 2;
    private final static int MAX_ROTOR_COUNT = 99;

    public TheEngine(){
        currentCode = new CodeObj();
    }

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
            Validator validator = new MachineValidator(machine, MIN_ROTOR_COUNT, MAX_ROTOR_COUNT);
            try{
                validator.validate();
                stock = new Stock(machine.getCTERotors().getCTERotor(), machine.getCTEReflectors().getCTEReflector(),
                        new KeyBoard(machine.getABC().trim().toUpperCase()), machine.getRotorsCount());
                this.machine = new Machine(stock.getKeyBoard(), stock.getRotorsCount());
                this.currentCode = null; //new machine - no code yet!
                this.processedMsgsCnt = 0; //new machine - new count!
                this.codesHistories.clear(); //new machine - new histories!
                return true;
            } catch (InvalidXMLException e) {
                throw e;
            }
        } catch (JAXBException | FileNotFoundException e) {
            throw new InvalidXMLException(XMLExceptionMsg.INVALIDFILE, "file not found");
        }
    }

    @Override
    public void setMachine(CodeObj machineCode){
        codesHistories.add(new CodeHistory(machineCode));
        currentCode = machineCode;
        machine.updateBySecret(secretFromCodeObj(currentCode));
    }

    private Secret secretFromCodeObj(CodeObj machineCode){
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
            throw new ObjectInputException("Invalid input: not a recognized character",
                                                    stock.getKeyBoard().getAsObjList());
        }
        long startTime = System.nanoTime();
        for(Character c : msg.toCharArray()){
            sb.append(machine.process(c));
        }
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        processedMsgsCnt++;
        codesHistories.getLast().addTranslation(msg, sb.toString(), timeElapsed);
        return sb.toString();
    }

    private CodeObj autoGenerateCodeObj(){
        String reflectorID = raffleReflector();
        List<Pair<Character, Character>> plugs = rafflePlugs();
        List<Pair<Integer, Character>> rotorsID2Position = raffleRotors();
        Map<Integer, Integer> noches = raffleNoches(rotorsID2Position);
        return new CodeObj(rotorsID2Position, noches, reflectorID, plugs);
    }

    @Override
    public void setByAutoGeneratedCode(){
        CodeObj generatedCode = autoGenerateCodeObj();
        setMachine(generatedCode);
    }

    @Override
    public CodeObj getMachineCode(){ return currentCode; }

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
                stock.getReflectorMap().size(), processedMsgsCnt, currentCode, getUpdatedCode());
    }

    @Override
    public void resetMachine(){
        machine.resetRotorsToInitial();
    }

    @Override
    public void validateAndSetReflector(CodeObj underConstructionCode, int wantedReflectorID){

        String IDRome = ReflectorID.getRomeByInteger(wantedReflectorID);
        if(stock.getReflector(IDRome) != null) {
            underConstructionCode.setReflectorID(IDRome);
        }
        else{
            throw new ObjectInputException("Invalid Reflector input ",
                    new ArrayList<>(stock.getReflectorMap().keySet()));
        }
    }

    @Override
    public void validateAndSetPlugs(CodeObj underConstructionCode, String plugs){
        if(plugs.length() % 2 != 0) {
            throw new InputException("Error: You entered an odd number of characters. Expecting two characters for each plug");
        }
        plugs = plugs.toUpperCase();
        if(!stock.getKeyBoard().isInKeyBoard(plugs)) {
            throw new ObjectInputException("Error: invalid values for plugs. ",stock.getKeyBoard().getAsObjList());
        }
        Map<Character, Integer> char2count = new HashMap<>();
        for(Character c : plugs.toCharArray()){
            if(char2count.getOrDefault(c, 0) != 0){
                throw new InputException("Error: each character may appear once at most!");
            }
            else {
                char2count.put(c, 1);
            }
        }
        underConstructionCode.setPlugs(createPairsFromString(plugs));
    }

    private List<Pair<Character, Character>> createPairsFromString(String plugs){
        List<Pair<Character, Character>> plugsList = new ArrayList<>();
        Pair<Character, Character> pair;
        char first, second;
        for(int i = 0; i<plugs.length(); i += 2) {
            first = plugs.charAt(i);
            second = plugs.charAt(i + 1);
            pair = new Pair<>(first, second);
            plugsList.add(pair);
        }
        return plugsList;
    }
    @Override
    public void validateAndSetRotors(CodeObj underConstructionCode, String rotors) {
        if (!rotors.matches("[0-9,]+")) {
            throw new InputException("Rotors IDs are only numbers");
        }
        List<Integer> elements = Arrays
                                    .stream(rotors.split(","))
                                    .map(Integer::valueOf)
                                    .collect(Collectors.toList());

        if(elements.size() != stock.getRotorsCount()){
            throw new ObjectInputException("Error: invalid rotor amount",stock.getRotorsCount());
        }

        Map<Integer, Integer> element2count = new HashMap<>();
        for (Integer id: elements) {
            if(id < 1 || id > stock.getRotorMap().size()){
                throw new OutOfBoundInputException("Rotor id", 1, stock.getRotorMap().size()); //maybe boundException?
            }
            if(element2count.getOrDefault(id, 0) != 0) {
                throw new InputException("Can't use same rotor more than once!");
            }
            element2count.put(id,1);
        }
        Collections.reverse(elements);
        underConstructionCode.setRotorIDs(elements);
    }

    @Override
    public void validateAndSetRotorPositions(CodeObj underConstructionCode, String positionsOfRotors) {
        if(positionsOfRotors.length() != stock.getRotorsCount()) {
            throw new ObjectInputException("Error: invalid rotor positions amount", stock.getRotorsCount());
        }
        positionsOfRotors = positionsOfRotors.toUpperCase();
        List<Character> rotorPositions = positionsOfRotors.chars().mapToObj(e->(char)e).collect(Collectors.toList());
        for(Character pos : rotorPositions) {
            if(!stock.getKeyBoard().isInKeyBoard(pos)){
                throw new  ObjectInputException(String.format("Error: character %c not in keyBoard", pos),
                                                                stock.getKeyBoard().getAsObjList());
            }
        }
        Collections.reverse(rotorPositions);
        underConstructionCode.setRotorPos(rotorPositions);
    }

    @Override
    public List<CodeHistory> showCodeHistory() { return codesHistories; }
    @Override
    public int getProcessedMsgsCnt() {
        return processedMsgsCnt;
    }
    @Override
    public CodeObj getCurrentCode() {
        return currentCode;
    }
    @Override
    public CodeObj getUpdatedCode() {
        List<Pair<Integer, Character>> updatedPosList = new LinkedList<>();
        Integer ID;
        char pos;
        for(Pair<Integer, Character> p : currentCode.getID2PositionList()){
            ID = p.getKey();
            pos = stock.getRotorMap().get(ID).getCurrentPositionChar();
            updatedPosList.add(new Pair<>(ID, pos));
        }
        return new CodeObj(updatedPosList, currentCode,
                getRelativeNotchesMap(currentCode.getID2PositionList()));
    }


    @Override
    public String toString() {
        return "TheEngine{" +
                "stock=" + stock +
                ", machine=" + machine +
                ", currentCode=" + currentCode +
                ", processedMsgsCnt=" + processedMsgsCnt +
                ", codesHistories=" + codesHistories +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TheEngine theEngine = (TheEngine) o;
        return processedMsgsCnt == theEngine.processedMsgsCnt && Objects.equals(stock, theEngine.stock) && Objects.equals(machine, theEngine.machine) && Objects.equals(currentCode, theEngine.currentCode) && Objects.equals(codesHistories, theEngine.codesHistories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stock, machine, currentCode, processedMsgsCnt, codesHistories);
    }
}