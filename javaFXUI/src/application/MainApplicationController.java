package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import components.machineDetails.MachineDetailsController;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

public class MainApplicationController {

    @FXML private MachineDetailsController machineDetailsController;
    @FXML private GridPane machineDetails;
    @FXML private Button loadFileButton;
    @FXML private Label fileChosenLabel;

    @FXML
    public void initialize(){
        if(machineDetailsController != null){
            machineDetailsController.setMainApplicationController(this);
        }
    }

    public void loadFileButtonClicked(){
        fileChosenLabel.setText("File loaded!");
        machineDetailsController.fileLoaded();
        }
}