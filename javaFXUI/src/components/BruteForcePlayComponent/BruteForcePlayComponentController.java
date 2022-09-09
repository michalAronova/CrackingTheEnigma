package components.BruteForcePlayComponent;

import application.MainApplicationController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;

public class BruteForcePlayComponentController {
    @FXML private Button stopButton;
    @FXML private Button pauseButton;
    @FXML private Button resumeButton;
    @FXML private Label missionDoneLabel;
    @FXML private Label totalPotentialCandidatesNumberLabel;
    @FXML private Label totalTimeLabel;
    @FXML private ProgressBar progressBar;
    @FXML private FlowPane potentialCandidatesFlowPane;
    private MainApplicationController mainApplicationController;

    @FXML public void initialize(){

    }

    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }
}
