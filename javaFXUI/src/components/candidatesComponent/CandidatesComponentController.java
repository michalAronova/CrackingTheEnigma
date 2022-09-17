package components.candidatesComponent;

import application.MainApplicationController;
import components.singleCandidateComponent.SingleCandidateComponentController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;
import java.util.List;

public class CandidatesComponentController {

    @FXML private ScrollPane rootScrollPane;
    @FXML private FlowPane potentialCandidatesFlowPane;
    private final String myStyleSheet = "candidates";
    private final String singleCandidatePath = "/components/singleCandidateComponent/";
    private final String singleCandidateStyleSheet = "singleCandidateComponent";

    private final List<SingleCandidateComponentController> singleControllerList;

    private MainApplicationController mainApplicationController;

    public CandidatesComponentController(){
        singleControllerList = new LinkedList<>();
    }

    public void addCandidate(Node singleCandidateTile, SingleCandidateComponentController controller){
        potentialCandidatesFlowPane.getChildren().add(singleCandidateTile);
        singleControllerList.add(controller);
    }

    public void removeAllCandidates(){
        potentialCandidatesFlowPane.getChildren().clear();
        singleControllerList.clear();
    }

    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    public void changeTheme(Object cssPrefix) {
        rootScrollPane.getStylesheets().clear();
        if(cssPrefix != null){
            String css = cssPrefix + myStyleSheet;
            rootScrollPane.getStylesheets()
                    .add(getClass().getClassLoader().getResource(String.format("%s.css", css)).toExternalForm());
        }

        singleControllerList.forEach(controller -> {
            controller.getRoot().getStylesheets().clear();
            if(cssPrefix != null){
                String css = cssPrefix + singleCandidateStyleSheet;
                controller.getRoot().getStylesheets()
                        .add(getClass().getClassLoader().getResource(String.format("%s.css", css)).toExternalForm());
            }
        });
    }
}

