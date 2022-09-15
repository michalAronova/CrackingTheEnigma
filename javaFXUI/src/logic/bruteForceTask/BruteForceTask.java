package logic.bruteForceTask;

import engine.Engine;
import exceptions.taskExceptions.TaskIsCancelledException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.concurrent.Task;
import logic.uiAdapter.UIAdapter;

import java.util.function.Consumer;

public class BruteForceTask extends Task<Boolean> {

    private String decryption;
    private Engine engine;
    private UIAdapter uiAdapter;
    private double totalMissionAmount;
    private BooleanProperty isPaused;
    private BooleanProperty isCancelled;
    private Consumer<Runnable> onCancel;

    public BruteForceTask(String decryption, Engine engine,
                          UIAdapter uiAdapter, double totalMissionAmount,
                          BooleanProperty isPaused, BooleanProperty isCancelled,
                          Consumer<Runnable> onCancel) {
        this.decryption = decryption;
        this.engine = engine;
        this.uiAdapter = uiAdapter;
        this.totalMissionAmount = totalMissionAmount;
        //for progress...
        this.isPaused = isPaused;
        this.isCancelled = isCancelled;
        this.onCancel = onCancel;
    }

    @Override
    protected Boolean call() throws Exception {
        updateProgress(0, totalMissionAmount);
        try{
            engine.getDM().setIsCancelled(isCancelled);
            engine.getDM().setIsPaused(isPaused);
            engine.getDM().setCurrentRunningTask(this);
            engine.getDM().setTransferMissionResult((result) -> uiAdapter.addNewCandidate(result));
            engine.getDM().setUpdateTotalMissionDone((delta) -> uiAdapter.updateTotalMissionDone(delta));

            //new Thread(() -> engine.manageAgents(decryption), "Manage Agents Thread").start();
            engine.manageAgents(decryption);
        }
        catch(TaskIsCancelledException e){
            onCancel.accept(null);
        }

        return Boolean.TRUE;
    }

    public UIAdapter getUiAdapter(){ return uiAdapter; }

    public void updateProgress(int current){
        updateProgress(current, totalMissionAmount);
    }

}
