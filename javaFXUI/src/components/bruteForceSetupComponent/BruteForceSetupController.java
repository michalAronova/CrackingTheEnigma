package components.bruteForceSetupComponent;

import application.MainApplicationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class BruteForceSetupController {
    @FXML private Label totalMissionNumLabel;
    @FXML private Label agentsNumLabel;
    @FXML private ComboBox<?> difficultyComboBox;
    @FXML private TextField missionSizeTextBox;
    @FXML private Button bruteForceSetButton;
    @FXML private Button bruteForceStartButton;
    private MainApplicationController mainApplicationController;

    @FXML public void initialize(){
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
}
