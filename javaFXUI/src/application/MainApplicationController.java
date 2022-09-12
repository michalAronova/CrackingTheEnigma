package application;

import DTO.codeObj.CodeObj;
import components.BruteForcePlayComponent.BruteForcePlayComponentController;
import components.bruteForceSetupComponent.BruteForceSetupController;
import components.codeConfigurationComponent.CodeConfigComponentController;
import components.codeHistoryComponent.CodeHistoryComponentController;
import components.codeHistoryComponent.StatisticData;
import components.codeObjDisplayComponent.CodeObjDisplayComponentController;
import components.keyBoardComponent.KeyBoardComponentController;
import components.processComponent.ProcessComponentController;
import engine.Engine;
import engine.TheEngine;
import exceptions.XMLException.InvalidXMLException;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import components.machineDetails.MachineDetailsController;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;

public class MainApplicationController {

    @FXML ScrollPane mainScrollPane;
    private Engine engine;
    private ObservableValue<CodeObj> initialCode;
    private ObservableValue<CodeObj> currentCode;

    @FXML private MachineDetailsController machineDetailsController;
    @FXML private CodeConfigComponentController codeConfigComponentController;
    @FXML private CodeObjDisplayComponentController codeObjDisplayComponentController;
    @FXML private CodeObjDisplayComponentController currentCodeDisplayComponent1Controller;
    @FXML private CodeObjDisplayComponentController currentCodeDisplayComponent2Controller;
    @FXML private CodeObjDisplayComponentController currentCodeDisplayComponent3Controller;
    @FXML private ProcessComponentController processComponentController;
    @FXML private ProcessComponentController processForBruteForceController;
    @FXML private KeyBoardComponentController keyBoardComponentController;
    @FXML private CodeHistoryComponentController codeHistoryComponentController;
    @FXML private BruteForceSetupController bruteForceSetupController;
    @FXML private BruteForcePlayComponentController bruteForcePlayComponentController;

    @FXML private GridPane machineDetails;
    @FXML private Button loadFileButton;
    @FXML private Label fileChosenLabel;
    @FXML private Tab encryptTabPane;
    @FXML private Tab bruteForceTabPane;
    public MainApplicationController(){
    }

    @FXML
    public void initialize(){
        if(machineDetailsController != null && codeConfigComponentController != null
                && codeObjDisplayComponentController != null
                && currentCodeDisplayComponent1Controller != null
                && currentCodeDisplayComponent2Controller != null
                && currentCodeDisplayComponent3Controller != null
                && processComponentController != null
                && processForBruteForceController != null
                && keyBoardComponentController != null
                && codeHistoryComponentController != null
                && bruteForceSetupController != null
                && bruteForcePlayComponentController != null){
            machineDetailsController.setMainApplicationController(this);
            codeConfigComponentController.setMainApplicationController(this);
            codeObjDisplayComponentController.setMainApplicationController(this);
            currentCodeDisplayComponent1Controller.setMainApplicationController(this);
            currentCodeDisplayComponent2Controller.setMainApplicationController(this);
            currentCodeDisplayComponent3Controller.setMainApplicationController(this);
            processComponentController.setMainApplicationController(this);
            processForBruteForceController.setMainApplicationController(this);
            keyBoardComponentController.setMainApplicationController(this);
            codeHistoryComponentController.setMainApplicationController(this);
            bruteForceSetupController.setMainApplicationController(this);
            bruteForcePlayComponentController.setMainApplicationController(this);
            handleDuplicateComponents();
        }
        else {
            System.out.println("nullComponent");
        }
        engine = new TheEngine();
        encryptTabPane.setDisable(true);
        bruteForceTabPane.setDisable(false);
        //bruteForceTabPane.setDisable(true);

    }

    private void handleDuplicateComponents() {
        codeObjDisplayComponentController.setHeaderLabelText("Machine Configuration");
        processComponentController.setBruteForceProcess(false);
        processForBruteForceController.setBruteForceProcess(true);
        processForBruteForceController.getSingleToggle().setDisable(true);
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
        encryptTabPane.setDisable(false);
        enableBruteForceTabPane();
        System.out.println(engine.getInitialCode());
        codeObjDisplayComponentController.onCodeChosen(engine.getInitialCode());
        //below will alert the relevant component that there's a new code to show:
        //currentCodeController.codeConfigured(engine.getInitialCode());
    }

    private void enableBruteForceTabPane() {
        bruteForceTabPane.setDisable(false);
        bruteForceSetupController.setAgentCount(engine.getAgentCountFromDM());
        bruteForceSetupController.initialBruteForceSetUp();
    }

    public void handleManualSet(CodeObj userCode){
        engine.setMachine(userCode);
        encryptTabPane.setDisable(false);
        enableBruteForceTabPane();
        System.out.println(engine.getInitialCode());
        codeObjDisplayComponentController.onCodeChosen(engine.getInitialCode());

        //System.out.println(engine.getInitialCode().toString());
        //below will alert the relevant component that there's a new code to show:
        //currentCodeController.codeConfigured(engine.getInitialCode());
    }

    public void singleModeOn(Boolean mode) {
        keyBoardComponentController.setFlowPaneDisable(!mode);
    }

    public void keyBoardButtonPressed(String text) {
        processComponentController.keyPressed(text);
        Character lastProcessedChar = processComponentController.getLastProcessedChar();
        keyBoardComponentController.activateKeyAnimation(lastProcessedChar);
    }

    public void onProcessButtonPressed(String input, ProcessComponentController controller){
        String output = processInMachine(input);
        controller.showOutput(output);
        StatisticData newProcess = new StatisticData(engine.getLastTranslationMade().getKey(),
                                                    engine.getLastTranslationMade().getValue());
        CodeObj updatedCode = engine.getUpdatedCode();
        currentCodeDisplayComponent1Controller.onCodeChosen(updatedCode);
        if(!controller.isBruteForceProcess()){
            codeHistoryComponentController.addNewProcess(newProcess);
            currentCodeDisplayComponent2Controller.onCodeChosen(updatedCode);
        }
        else{
            currentCodeDisplayComponent3Controller.onCodeChosen(updatedCode);
        }
    }

    public void codeResetRequested() {
        engine.resetMachine();
    }

    public boolean checkForKeyInKeyBoard(Character c) {
        return engine.getKeyBoardList().contains(c);
    }

    public String processInMachine(String message){
        return engine.processMsg(message);
    }

    public Character processSingleCharacter(Character c){
        return engine.processCharacterWithoutHistory(c);
    }

    public void onSingleModeDone(String input, String output) {
        engine.enterManualHistory(input, output);

        StatisticData newProcess = new StatisticData(engine.getLastTranslationMade().getKey(),
                engine.getLastTranslationMade().getValue());
        codeHistoryComponentController.addNewProcess(newProcess);
    }

    public int getAgentCount() {
        //double d = (double)engine.getAgentCountFromDM();
        return engine.getAgentCountFromDM();
    }

    public void onBruteForceSet(int agentCountChosen, String difficulty, int missionSize) {
            engine.setDMParamsFromUI(agentCountChosen, difficulty, missionSize);
    }

    public void calculateTotalMissionAmount() {
        bruteForceSetupController.updateTotalMissionAmount(engine.calculateTotalMissionsAmount());

    }
}