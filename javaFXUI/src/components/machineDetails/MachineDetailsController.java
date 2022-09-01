package components.machineDetails;

import DTO.techSpecs.TechSpecs;
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

    public void fileLoaded(TechSpecs machineDetails){
        rotorInUseData.setText(String.format("%d",machineDetails.getRotorsInUse()));
        totalRotorsData.setText(String.format("%d",machineDetails.getTotalRotors()));
        totalReflectorsData.setText(String.format("%d",machineDetails.getReflectorsCount()));
        messagesProcessedData.setText(String.format("%d",machineDetails.getProcessedMsg()));
    }
}
