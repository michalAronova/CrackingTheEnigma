package application;

import DTO.codeObj.CodeObj;
import components.codeConfigurationComponent.CodeConfigComponentController;
import components.codeObjDisplayComponent.CodeObjDisplayComponent;
import engine.Engine;
import engine.TheEngine;
import exceptions.XMLException.InvalidXMLException;
import javafx.beans.binding.Bindings;
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
    @FXML private CodeObjDisplayComponent codeObjDisplayComponentController;
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
                && codeObjDisplayComponentController != null){
            machineDetailsController.setMainApplicationController(this);
            codeConfigComponentController.setMainApplicationController(this);
            codeObjDisplayComponentController.setMainApplicationController(this);
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
}