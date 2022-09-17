package application;

import DTO.codeObj.CodeObj;
import components.BruteForcePlayComponent.BruteForcePlayComponentController;
import components.bruteForceSetupComponent.BruteForceSetupController;
import components.candidatesComponent.CandidatesComponentController;
import components.codeConfigurationComponent.CodeConfigComponentController;
import components.codeHistoryComponent.CodeHistoryComponentController;
import components.codeHistoryComponent.StatisticData;
import components.codeObjDisplayComponent.CodeObjDisplayComponentController;
import components.dictionaryComponent.DictionaryComponentController;
import components.keyBoardComponent.KeyBoardComponentController;
import components.processComponent.ProcessComponentController;
import components.singleCandidateComponent.SingleCandidateComponentController;
import engine.Engine;
import engine.TheEngine;
import exceptions.InputException.InputException;
import exceptions.InputException.OutOfBoundInputException;
import exceptions.XMLException.InvalidXMLException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

import components.machineDetails.MachineDetailsController;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedList;

public class MainApplicationController {

    @FXML ScrollPane mainScrollPane;
    private Engine engine;
    private CodeObj currentCode;
    private IntegerProperty codeChanges;
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
    @FXML private DictionaryComponentController dictionaryComponentController;
    @FXML private CandidatesComponentController candidatesComponentController;
    @FXML private GridPane machineDetails;
    @FXML private Button loadFileButton;
    @FXML private Label fileChosenLabel;
    @FXML private Tab encryptTabPane;
    @FXML private Tab bruteForceTabPane;

    @FXML private RadioButton noThemeRadioButton;
    @FXML private RadioButton wwiiThemeRadioButton;
    @FXML private RadioButton tsThemeRadioButton;
    @FXML CheckBox animationCheckBox;

    private BooleanProperty animationEnabled;

    //for CSS changes:
    private final String cssPath = "main/resources/";
    private final String myStyleSheet = "mainApplication";
    private final String wwiiStyleSheetPrefix = "wwii-";
    private final String tsStyleSheetPrefix = "ts-";
    private ToggleGroup cssChoiceGroup;
    public MainApplicationController(){
        codeChanges = new SimpleIntegerProperty(0);
        animationEnabled = new SimpleBooleanProperty(false);
    }

    public IntegerProperty getCodeChangesProperty(){
        return codeChanges;
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
                && bruteForcePlayComponentController != null
                && dictionaryComponentController != null
                && candidatesComponentController != null){
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
            dictionaryComponentController.setMainApplicationController(this);
            candidatesComponentController.setMainApplicationController(this);
            handleDuplicateComponents();
        }
        else {
            System.out.println("nullComponent");
        }
        engine = new TheEngine();
        bruteForcePlayComponentController.setEngine(engine);
        encryptTabPane.setDisable(true);
        bruteForceTabPane.setDisable(true);

        cssChoiceGroup = new ToggleGroup();
        noThemeRadioButton.setToggleGroup(cssChoiceGroup);
        noThemeRadioButton.setUserData(null);
        noThemeRadioButton.setSelected(true);

        wwiiThemeRadioButton.setToggleGroup(cssChoiceGroup);
        wwiiThemeRadioButton.setUserData(wwiiStyleSheetPrefix);

        tsThemeRadioButton.setToggleGroup(cssChoiceGroup);
        tsThemeRadioButton.setUserData(tsStyleSheetPrefix);

        cssChoiceGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (cssChoiceGroup.getSelectedToggle() != null) {
                changeTheme(cssChoiceGroup.getSelectedToggle().getUserData());
            }
        });

        animationEnabled.bind(animationCheckBox.selectedProperty());
    }

    public BooleanProperty getAnimationEnabledProperty(){ return animationEnabled; }

    private void changeTheme(Object cssPrefix) {
        bruteForcePlayComponentController.setCssPrefixForCandidates(cssPrefix);

        mainScrollPane.getStylesheets().clear();
        if(cssPrefix != null){
            cssPrefix = cssPath + cssPrefix;
            String css = cssPrefix + myStyleSheet;
            mainScrollPane.getStylesheets()
                    .add(getClass().getClassLoader().getResource(String.format("%s.css", css)).toExternalForm());
            if(cssPrefix.toString().equals(tsStyleSheetPrefix)){
                Stage stage = (Stage) mainScrollPane.getScene().getWindow();
                stage.getIcons().clear();
                stage.getIcons().add(new Image("/main/resources/jerry-square.jpg"));
            }
            else{
                Stage stage = (Stage) mainScrollPane.getScene().getWindow();
                stage.getIcons().clear();
                stage.getIcons().add(new Image("/main/resources/alan_turing_icon.jpg"));
            }
        }
        else{
            Stage stage = (Stage) mainScrollPane.getScene().getWindow();
            stage.getIcons().clear();
            stage.getIcons().add(new Image("/main/resources/alan_turing_icon.jpg"));
        }

        bruteForcePlayComponentController.changeTheme(cssPrefix);
        candidatesComponentController.changeTheme(cssPrefix);
        codeObjDisplayComponentController.changeTheme(cssPrefix);
        codeConfigComponentController.changeTheme(cssPrefix);
        currentCodeDisplayComponent1Controller.changeTheme(cssPrefix);
        currentCodeDisplayComponent2Controller.changeTheme(cssPrefix);
        currentCodeDisplayComponent3Controller.changeTheme(cssPrefix);
        dictionaryComponentController.changeTheme(cssPrefix);
        bruteForceSetupController.changeTheme(cssPrefix);
        machineDetailsController.changeTheme(cssPrefix);
        //processComponentController.changeTheme(cssPrefix);
        //processForBruteForceController.changeTheme(cssPrefix);
    }

    public String getCssPath() {
        return cssPath;
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
                currentCode = null;
                codeChanges.setValue(0);
                encryptTabPane.setDisable(true);
                bruteForceTabPane.setDisable(true);
                codeObjDisplayComponentController.onCodeChosen(null);
                dictionaryComponentController
                        .fillDictionaryTable(new LinkedList<>(engine.getDM().getDictionary().getWords()));
            }
            catch(InvalidXMLException | OutOfBoundInputException e){
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
        currentCode = engine.getInitialCode();
        codeObjDisplayComponentController.onCodeChosen(currentCode);
        codeChanges.setValue(codeChanges.getValue() + 1);
        if(codeChanges.getValue() == 1){
            enableBruteForceTabPane();
        }
    }

    private void enableBruteForceTabPane() {
        bruteForceTabPane.setDisable(false);
        bruteForceSetupController.setAgentCount(engine.getAgentCountFromDM());
        bruteForceSetupController.initialBruteForceSetUp();
        processForBruteForceController.changeLabelForBruteForce();
    }

    public void handleManualSet(CodeObj userCode){
        engine.setMachine(userCode);
        encryptTabPane.setDisable(false);
        codeObjDisplayComponentController.onCodeChosen(engine.getInitialCode());
        currentCode = userCode;
        codeChanges.setValue(codeChanges.getValue() + 1);
        if(codeChanges.getValue() == 1){
            enableBruteForceTabPane();
        }
    }

    public void singleModeOn(Boolean mode) {
        keyBoardComponentController.setFlowPaneDisable(!mode);
    }

    public void keyBoardButtonPressed(String text) {
        processComponentController.keyPressed(text);
        Character lastProcessedChar = processComponentController.getLastProcessedChar();
        keyBoardComponentController.activateKeyAnimation(lastProcessedChar);
    }

    public void notifyKeyBoardOfInput(){
        Character lastProcessedChar = processComponentController.getLastProcessedChar();
        keyBoardComponentController.activateKeyAnimation(lastProcessedChar);
    }

    public void onProcessButtonPressed(String input, ProcessComponentController controller){
        //IF THE CONTROLLER IS BRUTE FORCE, CHECK FOR WORDS IN DICTIONARY!
        if(controller.isBruteForceProcess()){
            if(!engine.getDM().getDictionary().areAllWordsInDictionary(input)){
                throw new InputException("You may only translate words from the dictionary!");
            }
            input = engine.getDM().getDictionary().removeExcludedChars(input);
        }
        String output = processInMachine(input);
        controller.showOutput(output);
        StatisticData newProcess = new StatisticData(engine.getLastTranslationMade().getKey(),
                                                    engine.getLastTranslationMade().getValue());
        currentCode = engine.getUpdatedCode();
        currentCodeDisplayComponent1Controller.onCodeChosen(currentCode);
        if(!controller.isBruteForceProcess()){
            codeHistoryComponentController.addNewProcess(newProcess);
            currentCodeDisplayComponent2Controller.onCodeChosen(currentCode);
        }
        else{
            currentCodeDisplayComponent3Controller.onCodeChosen(currentCode);
            bruteForceSetupController.setDecryptionString(output);
        }
    }

    public void codeResetRequested() {
        engine.resetMachine();
        currentCode = engine.getInitialCode();
        codeChanges.setValue(codeChanges.getValue() + 1);
    }

    public boolean checkForKeyInKeyBoard(Character c) {
        return engine.getKeyBoardList().contains(c);
    }

    public String processInMachine(String message){
        return engine.processMsg(message);
    }

    public Character processSingleCharacter(Character c){
        Character result = engine.processCharacterWithoutHistory(c);
        currentCode = engine.getUpdatedCode();
        return result;
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

    public CodeObj getCurrentCode() {
        return currentCode;
    }

    public void onBruteForceSet(int agentCountChosen, String difficulty, int missionSize) {
            engine.setDMParamsFromUI(agentCountChosen, difficulty, missionSize);
            bruteForcePlayComponentController.initializeDataProperties();
    }

    public void calculateTotalMissionAmount() {
        double totalMissionAmount = engine.calculateTotalMissionsAmount();
        bruteForceSetupController.updateTotalMissionAmount(totalMissionAmount);
        bruteForcePlayComponentController.updateTotalMissionAmount(totalMissionAmount);
    }

    public void toggleSetUpToAction(boolean isActive) {
        bruteForceSetupController.toggleSetUpToAction(isActive);
    }

    public void startBruteForce(String decryption) {
        bruteForcePlayComponentController.onBruteForceStarted(decryption);
    }

    public void injectWordToProcess(String rowData) {
        processForBruteForceController.injectWord(rowData);
    }

    public void addNewCandidate(Node singleCandidateTile, SingleCandidateComponentController controller) {
        candidatesComponentController.addCandidate(singleCandidateTile, controller);
    }

    public void clearCandidates() {
        candidatesComponentController.removeAllCandidates();
    }
}