package engine.decipherManager.mission;

import enigmaMachine.Machine;

public class Mission implements Runnable {

    private Machine myMachine;
    private int missionSize;
    private int missionNumber;

    public Mission(int num){
        missionNumber=num;
    }
    @Override
    public void run() {
        //for loop -> mission size
        //      doOne()
        System.out.println(Thread.currentThread().getName() + " #" + missionNumber);
    }
}
