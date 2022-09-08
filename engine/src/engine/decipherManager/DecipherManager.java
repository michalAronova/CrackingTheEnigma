package engine.decipherManager;

import DTO.codeObj.CodeObj;
import engine.decipherManager.dictionary.Dictionary;
import engine.decipherManager.mission.Mission;
import engine.stock.Stock;
import enigmaMachine.Machine;
import enigmaMachine.keyBoard.KeyBoard;
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
    private int missionSize;
    private Difficulty difficulty;
    private Map<Difficulty, Integer> diff2totalWork;
    private Machine machine;
    private Stock stock;
    private CodeObj machineCode;

    private String stockEncoded;

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
    }
    public DecipherManager() {
        System.out.println("DM was created");
    }

    public Difficulty getDifficulty() { return difficulty; }
    public int getAgentCount() {
        return agentCount;
    }
    public Dictionary getDictionary() {
        return dictionary;
    }
    public void setMissionSize(int missionSize) { this.missionSize = missionSize; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }

    public static void main(String[] args) {
        DecipherManager DM = new DecipherManager();
        DM.manageAgents();
    }

    public void serializeMachine(Machine machine){
        try{
            machineEncoded = serializableToString(this.machine);
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
        String toDecrypt = "hello world";
        //
        BasicThreadFactory factory = new BasicThreadFactory.Builder()
                .namingPattern("Agent %d")
                .build();
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
        ThreadPoolExecutor threadExecutor = new ThreadPoolExecutor(3, 3,
                                            Long.MAX_VALUE, TimeUnit.NANOSECONDS, workQueue , factory);

        threadExecutor.prestartAllCoreThreads();
        try {
            int totalMissionsAmount = diff2totalWork.get(difficulty) / missionSize;
            int leftoverMissionsAmount = diff2totalWork.get(difficulty) % missionSize;
            List<Pair<Integer,Character>> nextRotorsPositions = initializeRotorsPositions(machineCode.getID2PositionList());
                    //HERE WILL BE CALL FOR HARDER LEVELS CONFIG
            //reflector loop
            //      rotor id loop -> which rotors?
            //          rotor placement -> where rotors?
            //              positions loop -> f.e. AA AB BA BB (written below)
            for (int i = 0; i <= totalMissionsAmount; i++) {
                if(i != 0) {
                    nextRotorsPositions = getNextRotorsPositions(stock.getKeyBoard(), nextRotorsPositions, missionSize);
                }
                if(i == totalMissionsAmount){
                    workQueue.put(new Mission(deepCopyMachine(), nextRotorsPositions, leftoverMissionsAmount, toDecrypt, dictionary));
                }
                else{
                    workQueue.put(new Mission(deepCopyMachine(), nextRotorsPositions, missionSize, toDecrypt, dictionary));
                }
            }
        }
        catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " was interrupted.");
        }

        //what does the thread need to do its mission?
        // -----> what is in the mission so the thread can work it?
        //1. machine, configured by the DM!
        //2.mission size, so agent knows how many moves to make on the machine
        //3.blockingQueue - where should it put its findings?


        //we need: machine maker
        //                            1024                          100         = 11 (100x10 + 24)
        //loop size is: total work (AAA-ZZZ) / mission size (chosen by user)

        // AAA AAB AAC AAD sent!
        //
        // ZZZ - 24

        //MICHALIOT FRAMEWORK
/*
        List<Pair<Integer,Character>> id2Position = new ArrayList<>();
        id2Position.add(new Pair<>(1, 'A'));
        id2Position.add(new Pair<>(3, 'A'));
        id2Position.add(new Pair<>(2, 'A'));

        String keyboard = "ABCDEF";
        int missionSize = 100;
        for(int i = 0; i < 11; i++){
            if(i != 0){
                id2Position = speedometer(keyboard, id2Position, missionSize);
            }
            //clone new machine
            //configure machine by id2position
            //submit new task (mission!) with this new machine, and mission size
            threadExecutor.submit(() -> System.out.println(Thread.currentThread().getName()));
        }
*/
        threadExecutor.shutdown();
    }

//    private Machine cloneMachine(List<Pair<Integer, Character>> rotorsPosition) {
//        //Machine machine =
//    }

    private List<Pair<Integer, Character>> initializeRotorsPositions(List<Pair<Integer,Character>> origin) {
        List<Pair<Integer, Character>> initialized = new ArrayList<>();
        for (Pair<Integer, Character> p: origin) {
            initialized.add(new Pair<>(p.getKey(), stock.getKeyBoard().charAt(0)));
        }
        return initialized;
    }

    private List<Pair<Integer,Character>> getNextRotorsPositions(KeyBoard keyboard,
                                                                 List<Pair<Integer,Character>> start, int missionSize){
        // 0 like in mivneh mahshevim
        // [2, 1, 0]
        // [[1,A], [2,A], [3,A]]
        // [A,A,A]
        List<Character> characters = new ArrayList<>();
        for (Pair<Integer, Character> p: start) {
            characters.add(p.getValue());
        }
        for(int i = 0; i < missionSize ; i++){
            characters = speedometer(characters, keyboard);
        }
        return setNewCharacters(start, characters);
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

    private List<Character> speedometer(List<Character> characters, KeyBoard keyboard) {
        List<Character> updated = new ArrayList<>(characters);
        char[] board = keyboard.toString().toCharArray();
        for(int i = 0; i < characters.size(); i++){
            if(updated.get(i).equals(board[board.length - 1])){
                updated.set(i, board[0]);
            }
            else{
                updated.set(i, board[keyboard.indexOf(updated.get(i)) + 1]);
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
