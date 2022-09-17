package components.machineDetails;

import DTO.techSpecs.TechSpecs;
import application.MainApplicationController;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class MachineDetailsController {

    @FXML private Label rotorInUseData;
    @FXML private Label totalRotorsData;
    @FXML private Label totalReflectorsData;
    @FXML private Label messagesProcessedData;
    @FXML private Label machineDetailsTitleLabel;
    private ParallelTransition titleTransition;
    private MainApplicationController mainApplicationController;

    @FXML
    public void initialize(){
        titleTransition = createAnimationForTitle();
    }

    public void setMainApplicationController(MainApplicationController mainApplicationController) {
        this.mainApplicationController = mainApplicationController;
    }

    public void fileLoaded(TechSpecs machineDetails){
        rotorInUseData.setText(String.format("%d",machineDetails.getRotorsInUse()));
        totalRotorsData.setText(String.format("%d",machineDetails.getTotalRotors()));
        totalReflectorsData.setText(String.format("%d",machineDetails.getReflectorsCount()));
        messagesProcessedData.setText(String.format("%d",machineDetails.getProcessedMsg()));

        if(mainApplicationController.getAnimationEnabledProperty().getValue()){
            titleTransition.play();
        }
    }

    private ParallelTransition createAnimationForTitle(){
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(2000), machineDetailsTitleLabel);
        scaleTransition.setToX(2f);
        scaleTransition.setToY(2f);
        scaleTransition.setCycleCount(2);
        scaleTransition.setAutoReverse(true);

        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(2000), machineDetailsTitleLabel);
        translateTransition.setByX(100f);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);
        ParallelTransition pt = new ParallelTransition(scaleTransition, translateTransition);
        pt.setAutoReverse(true);

        return pt;
    }
}
