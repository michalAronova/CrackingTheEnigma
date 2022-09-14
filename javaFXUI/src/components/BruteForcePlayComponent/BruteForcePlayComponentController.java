package components.BruteForcePlayComponent;

import DTO.codeObj.CodeObj;
import application.MainApplicationController;
import engine.Engine;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.util.Pair;
import logic.bruteForceTask.BruteForceTask;
import logic.uiAdapter.UIAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BruteForcePlayComponentController {
    @FXML private Button stopButton;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Label missionDoneLabel;
    private IntegerProperty missionsDoneProperty;
    @FXML private Label totalPotentialCandidatesNumberLabel;
    private IntegerProperty totalPotentialCandidatesProperty;
    @FXML private Label totalTimeLabel;

    private DoubleProperty totalTimeProperty;
    @FXML private ProgressBar progressBar;
    @FXML private Label progressPercentageLabel;

    private DoubleProperty progressPercentageProperty;
    @FXML private FlowPane potentialCandidatesFlowPane;
    private MainApplicationController mainApplicationController;

    private BruteForceTask currentRunningTask;

    private BooleanProperty taskPaused;

    private BooleanProperty taskCancelled;

    private Engine engine;

    private IntegerProperty totalMissionAmountProperty;

    private String currentDecryption;

    public BruteForcePlayComponentController(){
        totalPotentialCandidatesProperty = new SimpleIntegerProperty(0);
        totalTimeProperty = new SimpleDoubleProperty(0);
        progressPercentageProperty = new SimpleDoubleProperty(0);
        missionsDoneProperty = new SimpleIntegerProperty(0);
        taskPaused = new SimpleBooleanProperty(false);
        taskCancelled = new SimpleBooleanProperty(false);
        totalMissionAmountProperty = new SimpleIntegerProperty(0);
    }

    @FXML public void initialize(){
        resumeButton.setDisable(true);
        pauseButton.setDisable(true);
        stopButton.setDisable(true);

        totalPotentialCandidatesNumberLabel.textProperty()
                .bind(Bindings.format("%d", totalPotentialCandidatesProperty));
        totalTimeLabel.textProperty()
                .bind(Bindings.format("%f", totalTimeProperty));
//        progressPercentageLabel.textProperty()
//                .bind(Bindings.format("%f", progressPercentageProperty));
        missionDoneLabel.textProperty()
                .bind(Bindings.format("%d", missionsDoneProperty));
    }

    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    public void setEngine(Engine engine){
        this.engine = engine;
    }

    public void onBruteForceStarted(String decryption) {
        currentDecryption = decryption;
        cleanOldResults();
        UIAdapter uiAdapter = createUIAdapter();
        toggleTaskButtons(true);
        initiateBruteForce(uiAdapter, () -> toggleTaskButtons(false));
    }

    private void toggleTaskButtons(boolean isActive) {
        stopButton.setDisable(!isActive);
        //pauseButton.setDisable(!isActive);
        mainApplicationController.toggleSetUpToAction(isActive);

        if(isActive){
            pauseButton.disableProperty().bind(taskPaused);
            resumeButton.disableProperty().bind(taskPaused.not());
        }
        else{
            pauseButton.disableProperty().unbind();
            resumeButton.disableProperty().unbind();
            resumeButton.setDisable(true);
            pauseButton.setDisable(true);
        }
    }

    private void onPauseClicked(){
        taskPaused.set(true);
    }

    private void onResumeClicked(){
        taskPaused.set(false);
        taskPaused.notifyAll();
    }

    private void onStopClicked(){
        taskCancelled.setValue(true);
        currentRunningTask.cancel();
    }

    private void cleanOldResults() {
        potentialCandidatesFlowPane.getChildren().clear();
        progressBar.setProgress(0);
        totalPotentialCandidatesProperty.set(0);
        missionsDoneProperty.set(0);
    }

    public void initiateBruteForce(UIAdapter uiAdapter, Runnable onFinish) {
        currentRunningTask = new BruteForceTask(currentDecryption, engine, uiAdapter,
                totalMissionAmountProperty.get(),
                                taskPaused, taskCancelled,
                                (q) -> onTaskFinished(Optional.ofNullable(onFinish)));

        bindTaskToUIComponents(currentRunningTask, onFinish);

        new Thread(currentRunningTask).start();
    }

    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                missionResult -> { //Consumer<MissionResult> addCandidate
                    totalPotentialCandidatesProperty.set(totalPotentialCandidatesProperty.get() + missionResult.getCandidates().size());
                    createTiles(missionResult.getCandidates(), missionResult.getAgentID());
                },
                (delta) -> { //Consumer<Integer> updateTotalMissionDone
                    totalMissionAmountProperty.set(totalMissionAmountProperty.get() + 1);
                }
        );
    }

    private void createTiles(List<Pair<String, CodeObj>> candidates, String agentID) {
        candidates.forEach(p -> createTile(p.getKey(), p.getValue(), agentID));
    }

    private void createTile(String candidate, CodeObj code, String agentID){
        System.out.println("Creating tile for: " + code +", " + agentID + ", "+ candidate);
//        try {
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(HistogramResourcesConstants.MAIN_FXML_RESOURCE);
//            Node singleCandidateTile = loader.load();
//
//            SingleCandidateController singleCandidateController = loader.getController();
//            singleCandidateController.setAgentID(agentID);
//            singleCandidateController.setCandidate(candidate);
//            singleCandidateController.setCode(code);
//
//            potentialCandidatesFlowPane.getChildren().add(singleCandidateTile);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        // task progress bar
        progressBar.progressProperty().bind(aTask.progressProperty());

        // task percent label
//        progressPercentageLabel.textProperty().bind(
//                Bindings.concat(
//                        Bindings.format(
//                                "%.0f",
//                                Bindings.multiply(
//                                        aTask.progressProperty(),
//                                        100)),
//                        " %"));

        // task cleanup upon finish
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        //this.progressPercentageLabel.textProperty().unbind();
        this.progressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }

    public void updateTotalMissionAmount(int totalMissionAmount) {
        this.totalMissionAmountProperty.set(totalMissionAmount);
    }
}
