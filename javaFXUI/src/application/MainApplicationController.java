package application;

import DTO.codeObj.CodeObj;
import components.codeConfigurationComponent.CodeConfigComponentController;
import components.codeHistoryComponent.CodeHistoryComponentController;
import components.codeHistoryComponent.StatisticData;
import components.codeObjDisplayComponent.CodeObjDisplayComponentController;
import components.keyBoardComponent.KeyBoardComponentController;
import components.processComponent.ProcessComponentController;
import engine.Engine;
import engine.TheEngine;
import exceptions.XMLException.InvalidXMLException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import components.machineDetails.MachineDetailsController;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;

public class MainApplicationController {

    @FXML ScrollPane mainScrollPane;
    private Engine engine;
    @FXML private MachineDetailsController machineDetailsController;
    @FXML private CodeConfigComponentController codeConfigComponentController;
    @FXML private CodeObjDisplayComponentController codeObjDisplayComponentController;
    @FXML private CodeObjDisplayComponentController currentCodeDisplayComponent1Controller;
    @FXML private CodeObjDisplayComponentController currentCodeDisplayComponent2Controller;
    @FXML private CodeObjDisplayComponentController currentCodeDisplayComponent3Controller;
    @FXML private ProcessComponentController processComponentController;
    @FXML private KeyBoardComponentController keyBoardComponentController;
    @FXML private CodeHistoryComponentController codeHistoryComponentController;
    @FXML private GridPane machineDetails;
    @FXML private Button loadFileButton;
    @FXML private Label fileChosenLabel;
    private BooleanProperty isFileLoaded;

    public MainApplicationController(){
        isFileLoaded = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize(){
        if(machineDetailsController != null && codeConfigComponentController != null
                && codeObjDisplayComponentController != null
                && currentCodeDisplayComponent1Controller != null
                && currentCodeDisplayComponent2Controller != null
                && currentCodeDisplayComponent3Controller != null
                && processComponentController != null
                && keyBoardComponentController != null
                && codeHistoryComponentController != null){
            machineDetailsController.setMainApplicationController(this);
            codeConfigComponentController.setMainApplicationController(this);
            codeObjDisplayComponentController.setMainApplicationController(this);
            currentCodeDisplayComponent1Controller.setMainApplicationController(this);
            currentCodeDisplayComponent2Controller.setMainApplicationController(this);
            currentCodeDisplayComponent3Controller.setMainApplicationController(this);
            processComponentController.setMainApplicationController(this);
            keyBoardComponentController.setMainApplicationController(this);
            codeHistoryComponentController.setMainApplicationController(this);
        }
        else {

            System.out.println("hereeeee");
        }
        engine = new TheEngine();
    }

    public void loadFileButtonClicked(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(mainScrollPane.getScene().getWindow());

        if(selectedFile != null){
            try{
                engine.loadDataFromXML(selectedFile.getAbsolutePath());
                fileChosenLabel.getStyleClass().remove("error-message");
                fileChosenLabel.setText(selectedFile.getAbsolutePath());
                machineDetailsController.fileLoaded(engine.showTechSpecs());
                codeConfigComponentController.handleFileLoaded(engine.getKeyBoardList(),
                        engine.getRotorsCount(), engine.getTotalRotorAmount(),
                        engine.getReflectorCount());
                keyBoardComponentController.setComponent(engine.getKeyBoardList());
            }
            catch(InvalidXMLException e){
                fileChosenLabel.getStyleClass().add("error-message");
                fileChosenLabel.setText(e.getMessage());
            }

        }
        else{
            fileChosenLabel.getStyleClass().add("error-message");
            fileChosenLabel.setText("Couldn't load file");
        }
    }

    public void handleSetByRandom(){
        engine.setByAutoGeneratedCode();
        System.out.println(engine.getInitialCode());
        codeObjDisplayComponentController.onCodeChosen(engine.getInitialCode());
        //below will alert the relevant component that there's a new code to show:
        //currentCodeController.codeConfigured(engine.getInitialCode());
    }

    public void handleManualSet(CodeObj userCode){
        engine.setMachine(userCode);
        System.out.println(engine.getInitialCode());
        codeObjDisplayComponentController.onCodeChosen(engine.getInitialCode());
        //System.out.println(engine.getInitialCode().toString());
        //below will alert the relevant component that there's a new code to show:
        //currentCodeController.codeConfigured(engine.getInitialCode());
    }

    public void handleKeyboardPressed(String key){
        //notify process component controller...
    }

    public void singleModeOn(Boolean mode) {
        keyBoardComponentController.setFlowPaneDisable(!mode);
    }

    public void keyBoardButtonPressed(String text) {
        processComponentController.keyPressed(text);
    }

    public void onProcessButtonPressed(String input, String output){
        System.out.println("hello");
        StatisticData newProcess = new StatisticData(engine.getInitialCode().toString(), input, output, 25.0);
        codeHistoryComponentController.addNewProcess(newProcess);
    }
}