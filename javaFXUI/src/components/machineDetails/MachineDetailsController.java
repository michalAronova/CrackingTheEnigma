package components.machineDetails;

import application.MainApplicationController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MachineDetailsController {

    @FXML private Label rotorInUseData;
    @FXML private Label totalRotorsData;
    @FXML private Label totalReflectorsData;
    @FXML private Label messagesProcessedData;
    @FXML private Label machineDetailsTitleLabel;

    private MainApplicationController mainApplicationController;

    public void setMainApplicationController(MainApplicationController mainApplicationController) {
        this.mainApplicationController = mainApplicationController;
    }

    public void fileLoaded(){
        rotorInUseData.setText("0");
        totalRotorsData.setText("3");
        totalReflectorsData.setText("1");
        messagesProcessedData.setText("0");
    }
}
