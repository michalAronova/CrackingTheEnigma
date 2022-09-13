package components.BruteForcePlayComponent;

import DTO.codeObj.CodeObj;
import application.MainApplicationController;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.util.Pair;
import logic.uiAdapter.UIAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class BruteForcePlayComponentController {
    @FXML private Button stopButton;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Label missionDoneLabel;
    @FXML private Label totalPotentialCandidatesNumberLabel;
    @FXML private Label totalTimeLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Label progressPercentageLabel;
    @FXML private FlowPane potentialCandidatesFlowPane;
    private MainApplicationController mainApplicationController;

    @FXML public void initialize(){

    }

    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    public void onBruteForceStarted() {
        cleanOldResults();
        UIAdapter uiAdapter = createUIAdapter();

        toggleTaskButtons(true);

        initiateBruteForce(uiAdapter, () -> toggleTaskButtons(false));
    }

    public void initiateBruteForce(UIAdapter uiAdapter, Runnable onFinish) {
        currentRunningTask = new CalculateHistogramsTask(fileName.get(), totalWords, uiAdapter, (q) -> controller.onTaskFinished(Optional.ofNullable(onFinish)));

        bindTaskToUIComponents(currentRunningTask, onFinish);

        new Thread(currentRunningTask).start();
    }

    private UIAdapter createUIAdapter() {
        return new UIAdapter(
                missionResult -> { //Consumer<MissionResult> addCandidate
                    createTiles(missionResult.getCandidates(), missionResult.getAgentID());
                },
                (delta) -> { //Consumer<Integer> updateTotalCandidates
                    totalCandidates.set(totalCandidates.get() + delta);
                },
                (delta) -> { //Consumer<Integer> updateTotalMissionDone
                    totalMission.set(totalMissions.get() + delta);
                }
        );
    }

    private void createTiles(List<Pair<String, CodeObj>> candidates, String agentID) {
        candidates.forEach(p -> createTile(p.getKey(), p.getValue(), agentID));
    }

    private void createTile(String candidate, CodeObj code, String agentID){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HistogramResourcesConstants.MAIN_FXML_RESOURCE);
            Node singleCandidateTile = loader.load();

            SingleCandidateController singleCandidateController = loader.getController();
            singleCandidateController.setAgentID(agentID);
            singleCandidateController.setCandidate(candidate);
            singleCandidateController.setCode(code);

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
        aTask.valueProperty().addListener((observable, oldValue, newValue) -> {
            onTaskFinished(Optional.ofNullable(onFinish));
        });
    }

    public void onTaskFinished(Optional<Runnable> onFinish) {
        this.progressPercentageLabel.textProperty().unbind();
        this.progressBar.progressProperty().unbind();
        onFinish.ifPresent(Runnable::run);
    }
}
