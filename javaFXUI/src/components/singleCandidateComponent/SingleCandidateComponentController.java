package components.singleCandidateComponent;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SingleCandidateComponentController {

    @FXML public VBox rootVBox;
    @FXML private Label codeLabel;
    @FXML private Label candidateLabel;
    @FXML private Label agentIDLabel;

    private String cssPrefix;

    private final String myStyleSheet = "singleCandidateComponent";
    public SingleCandidateComponentController() {};
    public SingleCandidateComponentController(String code, String candidate, String agentID) {
        this.codeLabel.setText(code);
        this.candidateLabel.setText(candidate);
        this.agentIDLabel.setText(agentID);
    }
    @FXML
    private void initialize() {
        rootVBox.getStylesheets().clear();
        if(cssPrefix != null){
            String css = cssPrefix + myStyleSheet;
            System.out.println(String.format("%s.css", css));
            rootVBox.getStylesheets()
                    .add(getClass().getClassLoader().getResource(String.format("%s.css", css)).toExternalForm());
        }
    }
    public void setCode(String code) {
        this.codeLabel.setText(code);
    }
    public void setCandidate(String candidate) {
        this.candidateLabel.setText(candidate);
    }
    public void setAgent(String agentID) { this.agentIDLabel.setText(agentID);}

    public Parent getRoot() {
        return rootVBox;
    }

    public void setCssPrefix(Object cssPrefix){
        this.cssPrefix = (String) cssPrefix;
    }

}
