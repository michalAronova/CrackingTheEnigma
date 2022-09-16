package components.BruteForcePlayComponent;

import DTO.codeObj.CodeObj;
import application.MainApplicationController;
import components.singleCandidateComponent.SingleCandidateComponentController;
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
import java.net.URL;
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

    private final BooleanProperty taskPaused;

    private BooleanProperty taskCancelled;

    private Engine engine;

    private DoubleProperty totalMissionAmountProperty;

    private String currentDecryption;

    public BruteForcePlayComponentController(){
        totalPotentialCandidatesProperty = new SimpleIntegerProperty(0);
        totalTimeProperty = new SimpleDoubleProperty(0);
        progressPercentageProperty = new SimpleDoubleProperty(0);
        missionsDoneProperty = new SimpleIntegerProperty(0);
        taskPaused = new SimpleBooleanProperty(false);
        taskCancelled = new SimpleBooleanProperty(false);
        totalMissionAmountProperty = new SimpleDoubleProperty(0);
    }

    @FXML public void initialize(){
        resumeButton.setDisable(true);
        pauseButton.setDisable(true);
        stopButton.setDisable(true);
        initializeDataLabels();
        totalPotentialCandidatesNumberLabel.textProperty()
                .bind(Bindings.format("%d", totalPotentialCandidatesProperty));
        totalTimeLabel.textProperty()
                .bind(Bindings.format("%.2f", totalTimeProperty));
        progressPercentageLabel.textProperty()
                .bind(Bindings.format("%.2f", progressPercentageProperty));
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

    @FXML
    private void onPauseClicked(){
        taskPaused.set(true);
    }

    @FXML
    private void onResumeClicked(){
        taskPaused.set(false);
        synchronized (taskPaused){
            taskPaused.notifyAll();
        }
    }

    @FXML
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

    private void initializeDataLabels() {
        missionDoneLabel.setText("0");
        totalPotentialCandidatesNumberLabel.setText("0");
        totalTimeLabel.setText("0");
    }

    public void initializeDataProperties() {
        totalPotentialCandidatesProperty.set(0);
        missionsDoneProperty.set(0);
        totalTimeProperty.set(0);
        progressPercentageProperty.set(0);
        if (currentRunningTask != null) {
            currentRunningTask.updateProgress(0);
            progressBar.setProgress(0);
        }
    }
    public void initiateBruteForce(UIAdapter uiAdapter, Runnable onFinish) {
        currentRunningTask = new BruteForceTask(currentDecryption, engine, uiAdapter,
                totalMissionAmountProperty.get(),
                                taskPaused, taskCancelled,
                                (q) -> onTaskFinished(Optional.ofNullable(onFinish)));

        bindTaskToUIComponents(currentRunningTask, onFinish);

        new Thread(currentRunningTask, "Manage Agents Task Thread").start();
    }

    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                missionResult -> { //Consumer<MissionResult> addCandidate
                    totalPotentialCandidatesProperty.set(totalPotentialCandidatesProperty.get() + missionResult.getCandidates().size());
                    createTiles(missionResult.getCandidates(), missionResult.getAgentID());
                },
                (delta) -> { //Consumer<Integer> updateTotalMissionDone
                    missionsDoneProperty.set(missionsDoneProperty.get() + 1);
                    currentRunningTask.updateProgress(missionsDoneProperty.get());
                },
                (timeDelta) ->{ //Consumer<Long> updateTotalTime;
                    totalTimeProperty.set(totalTimeProperty.get() + timeDelta);
                }
        );
    }

    private void createTiles(List<Pair<String, CodeObj>> candidates, String agentID) {
        candidates.forEach(p -> createTile(p.getKey(), p.getValue(), agentID));
    }

    private void createTile(String candidate, CodeObj code, String agentID){
        try {
            /*
        URL url = getClass().getResource("/filteredDictionary/dictionaryComponent.fxml");
        fxmlLoader.setLocation(url);
        Parent root = fxmlLoader.load(url.openStream());


             */
            FXMLLoader loader = new FXMLLoader();

            URL url = getClass().getResource("/components/singleCandidateComponent/singleCandidateComponent.fxml");
            loader.setLocation(url);
            Node singleCandidateTile = loader.load();

            SingleCandidateComponentController singleCandidateController = loader.getController();
            singleCandidateController.setAgent(agentID);
            singleCandidateController.setCandidate(candidate);
            singleCandidateController.setCode(code.toString());

            potentialCandidatesFlowPane.getChildren().add(singleCandidateTile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bindTaskToUIComponents(Task<Boolean> aTask, Runnable onFinish) {
        // task progress bar
        progressBar.progressProperty().bind(aTask.progressProperty());

        // task percent label
        progressPercentageLabel.textProperty().bind(
                Bindings.concat(
                        Bindings.format(
                                "%.0f",
                                Bindings.multiply(
                                        aTask.progressProperty(),
                                        100)),
                        " %"));

        // task cleanup upon finish
        aTask.progressProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue.equals(1.0)){
                onTaskFinished(Optional.ofNullable(onFinish));
            }
        }));
//        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
//
//        });
    }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        //this.progressPercentageLabel.textProperty().unbind();
        this.progressBar.progressProperty().unbind();
        taskCancelled.setValue(false);
        taskPaused.setValue(false);
        //onFinish.ifPresent(Runnable::run);
        toggleTaskButtons(false);
    }

    public void updateTotalMissionAmount(double totalMissionAmount) {
        this.totalMissionAmountProperty.set(totalMissionAmount);
    }

    public void setDecryptionString(String currentDecryption) {
        this.currentDecryption = currentDecryption;
    }
}
