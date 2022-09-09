package engine.decipherManager;

import DTO.codeObj.CodeObj;
import DTO.missionResult.MissionResult;
import engine.decipherManager.dictionary.Dictionary;
import engine.decipherManager.mission.Mission;
import engine.stock.Stock;
import enigmaMachine.Machine;
import enigmaMachine.keyBoard.KeyBoard;
import enigmaMachine.rotor.Rotor;
import javafx.util.Pair;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import schema.generated.CTEDecipher;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DecipherManager {
    private int agentCount;
    private Dictionary dictionary;
    private int missionSize = 100;
    private Difficulty difficulty;
    private Map<Difficulty, Integer> diff2totalWork;
    private Machine machine;
    private Stock stock;
    private CodeObj machineCode;

    private String stockEncoded;

    private double possiblePositionPermutations;

    private String machineEncoded;
    public DecipherManager(CTEDecipher cteDecipher, Map<Difficulty, Integer> diff2totalWork, Machine machine,
                           Stock stock){
        agentCount = cteDecipher.getAgents();
        dictionary = new Dictionary(cteDecipher.getCTEDictionary());
        this.diff2totalWork = diff2totalWork;
        this.machine = machine;
        this.stock = stock;
        try{
            stockEncoded = serializableToString(this.stock);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        possiblePositionPermutations = calculatePermutationsCount();
    }

    private double calculatePermutationsCount() {
        return Math.pow(stock.getKeyBoard().length(), machine.getRotorCount());
    }


    public DecipherManager() { System.out.println("DM was created"); }

    public Difficulty getDifficulty() { return difficulty; }
    public int getAgentCount() { return agentCount; }
    public Dictionary getDictionary() { return dictionary; }
    public void setMissionSize(int missionSize) { this.missionSize = missionSize; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public static void main(String[] args) {
        DecipherManager DM = new DecipherManager();
        DM.manageAgents();
    }

    public void serializeMachine(Machine machine){
        try{
            machineEncoded = serializableToString(machine);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public Machine deepCopyMachine(){
        try{
            return (Machine)objectFromString(this.machineEncoded);
        }
        catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public void manageAgents() {
        //debug
        Machine copiedMachine = deepCopyMachine();
        System.out.println("decrypting code: ");
        System.out.println(copiedMachine.getMachineCode());
        String encryption = copiedMachine.processWord("hello tonight");
        //
        BlockingQueue<MissionResult> resultQueue = new LinkedBlockingQueue<>();

        //
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("Agent %d")
                .build();
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
        ThreadPoolExecutor threadExecutor = new ThreadPoolExecutor(agentCount, agentCount,
                                            Long.MAX_VALUE, TimeUnit.NANOSECONDS, workQueue , factory);

        threadExecutor.prestartAllCoreThreads();
        try {
            double totalMissionsAmount = possiblePositionPermutations / missionSize;
            double leftoverMissionsAmount = possiblePositionPermutations % missionSize;
            List<Character> nextRotorsPositions = initializeRotorsPositions(machineCode.getID2PositionList());
            //HERE WILL BE CALL FOR HARDER LEVELS CONFIG
            //reflector loop
            //      rotor id loop -> which rotors?
            //          rotor placement -> where rotors?
            //              positions loop -> f.e. AA AB BA BB (written below)
            for (int i = 0; i <= totalMissionsAmount; i++) {
                if(i != 0) {
                    nextRotorsPositions = getNextRotorsPositions(nextRotorsPositions, missionSize);
                }
                if(i == totalMissionsAmount){
                    workQueue.put(new Mission(deepCopyMachine(), nextRotorsPositions, leftoverMissionsAmount,
                            encryption, dictionary, this::speedometer));
                }
                else{
                    workQueue.put(new Mission(deepCopyMachine(), nextRotorsPositions, missionSize,
                            encryption, dictionary, this::speedometer));
                }
            }
        }
        catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted.");
        }
        threadExecutor.shutdown();
    }

    private List<Character> initializeRotorsPositions(List<Pair<Integer,Character>> origin) {
        List<Character> initialized = new ArrayList<>();
        for (Pair<Integer, Character> p: origin) {
            initialized.add(stock.getKeyBoard().charAt(0));
        }
        return initialized;
    }

    private List<Character> getNextRotorsPositions(List<Character> start, int missionSize){
        List<Character> characters = new ArrayList<>(start);
        for(int i = 0; i < missionSize ; i++){
            characters = speedometer(characters);
        }
        return characters;
    }

    private List<Pair<Integer, Character>> setNewCharacters(List<Pair<Integer, Character>> start, List<Character> characters) {
        List<Pair<Integer, Character>> updated = new ArrayList<>();
        int i = 0;
        for (Pair<Integer, Character> p: start) {
            updated.add(new Pair<>(p.getKey(), characters.get(i)));
            ++i;
        }
        return updated;
    }

    private List<Character> speedometer(List<Character> characters) {
        List<Character> updated = new ArrayList<>(characters);
        List<Character> keys = stock.getKeyBoard().getAsCharList();
        String keysString = stock.getKeyBoard().getKeyBoardString();

        for(int i = 0; i < characters.size(); i++){
            if(updated.get(i).equals(keys.get(keys.size() - 1))){
                updated.set(i, keys.get(0));
            }
            else{
                updated.set(i, keys.get(keysString
                                            .indexOf(
                                                updated.get(i)) + 1));
                break;
            }
        }
        return updated;
    }

    public void setMachineCode(CodeObj code) { machineCode = code; }

    private static String serializableToString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    private static Object objectFromString(String s) throws IOException, ClassNotFoundException
    {
        byte [] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o  = ois.readObject();
        ois.close();
        return o;
    }
}
