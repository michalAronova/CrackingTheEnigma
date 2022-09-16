package components.candidatesComponent;

import application.MainApplicationController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

public class CandidatesComponentController {

    @FXML private FlowPane potentialCandidatesFlowPane;

    private MainApplicationController mainApplicationController;

    public void addCandidate(Node singleCandidateTile){
        potentialCandidatesFlowPane.getChildren().add(singleCandidateTile);
    }

    public void removeAllCandidates(){
        potentialCandidatesFlowPane.getChildren().clear();
    }

    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }
}

