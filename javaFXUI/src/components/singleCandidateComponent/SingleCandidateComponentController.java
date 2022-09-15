package components.singleCandidateComponent;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SingleCandidateComponentController {

    @FXML private Label codeLabel;
    @FXML private Label candidateLabel;
    @FXML private Label agentIDLabel;
    public SingleCandidateComponentController() {};
    public SingleCandidateComponentController(String code, String candidate, String agentID) {
        this.codeLabel.setText(code);
        this.candidateLabel.setText(candidate);
        this.agentIDLabel.setText(agentID);
    }
    private void initialize() {
        //codeLabel.textProperty().bind(Bindings.concat("<", word, ">"));
        //countLabel.textProperty().bind(count.asString());
    }
    public void setCode(String code) {
        this.codeLabel.setText(code);
    }
    public void setCandidate(String candidate) {
        this.candidateLabel.setText(candidate);
    }
    public void setAgent(String agentID) { this.agentIDLabel.setText(agentID);}
}
