package components.bruteForceSetupComponent;

import application.MainApplicationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class BruteForceSetupController {
    @FXML private Label totalMissionNumLabel;
    @FXML private Label agentsNumLabel;
    @FXML private Slider numberOfAgentsSlider;
    @FXML private ComboBox<?> difficultyComboBox;
    @FXML private TextField missionSizeTextBox;
    @FXML private Button bruteForceSetButton;
    @FXML private Button bruteForceStartButton;
    private MainApplicationController mainApplicationController;
    private int agentCount;
    @FXML public void initialize(){}
    public void initialBruteForceSetUp(){
        numberOfAgentsSlider.setMax(mainApplicationController.getAgentCount());
        initialDiffComboBox();
    }
    private void initialDiffComboBox() {
        difficultyComboBox.getItems().clear();
        List<String> list = new ArrayList<String>();
        list.add("Easy");
        list.add("Medium");
        list.add("Hard");
        list.add("Not Possible");
        ObservableList obList = FXCollections.observableList(list);
        difficultyComboBox.setItems(obList);
    }

    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    public void setAgentCount(int agentCount) { this.agentCount = agentCount; }
}
