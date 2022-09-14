package components.bruteForceSetupComponent;

import application.MainApplicationController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

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
    private int agentAmountChosen;

    @FXML public void initialize(){

    }
    public void initialBruteForceSetUp(){
        initializeSlider();
        initialDiffComboBox();
    }

    private void initializeSlider() {
        numberOfAgentsSlider.setMax(mainApplicationController.getAgentCount());
        numberOfAgentsSlider.setMin(1.0);

        numberOfAgentsSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                agentsNumLabel.setText(Integer.toString((int)numberOfAgentsSlider.getValue()));
            }
        });
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

    public void onSetButton() {
        mainApplicationController.onBruteForceSet(parseInt(agentsNumLabel.getText().toString()),
                                    difficultyComboBox.getSelectionModel().getSelectedItem().toString(),
                                    parseInt(missionSizeTextBox.getText()));
        mainApplicationController.calculateTotalMissionAmount();
    }

    public void onStartButton(){
        mainApplicationController.startBruteForce();
    }

    public void updateTotalMissionAmount(Integer totalMissionAmount) {
        totalMissionNumLabel.setText(totalMissionAmount.toString());
    }

    public void toggleSetUpToAction(boolean isActive) {
        bruteForceSetButton.setDisable(isActive);
        bruteForceStartButton.setDisable(isActive);
    }
}
