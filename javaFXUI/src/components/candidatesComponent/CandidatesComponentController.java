package components.candidatesComponent;

import application.MainApplicationController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

public class CandidatesComponentController {

    @FXML private FlowPane potentialCandidatesFlowPane;

    private final String myStyleSheet = "candidates.css";
    private final String singleCandidatePath = "/components/singleCandidateComponent/";
    private final String singleCandidateStyleSheet = "singleCandidateComponent.css";

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

    public void changeTheme(Object cssPrefix) {
        potentialCandidatesFlowPane.getScene().getStylesheets().clear();
        if(cssPrefix != null){
            String css = cssPrefix + myStyleSheet;
            potentialCandidatesFlowPane.getScene().getStylesheets().add(css);
        }

        potentialCandidatesFlowPane.getChildren().forEach(candidate -> {
            candidate.getScene().getStylesheets().clear();
            if(cssPrefix != null){
                String css = singleCandidatePath + cssPrefix + singleCandidateStyleSheet;
                candidate.getScene().getStylesheets().add(css);
            }
        });
    }
}

