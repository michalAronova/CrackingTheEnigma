package engine.decipherManager.resultListener;

import DTO.missionResult.MissionResult;

import java.util.concurrent.BlockingQueue;

public class ResultListener implements Runnable{
    private BlockingQueue<MissionResult> resultQueue;

    public ResultListener(BlockingQueue<MissionResult> resultQueue) {
        this.resultQueue = resultQueue;
    }


    @Override
    public void run() {
        try{
            System.out.println("attempting to take...");
            System.out.println(resultQueue.take());
        }
        catch(InterruptedException e){
            System.out.println(Thread.currentThread().getName() + " was interrupted");
        }
    }
}
