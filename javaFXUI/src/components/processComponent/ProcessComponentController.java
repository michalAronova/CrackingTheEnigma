package components.processComponent;

import application.MainApplicationController;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.controlsfx.control.SegmentedButton;

public class ProcessComponentController {

    @FXML private HBox toggleContainer;
    @FXML private ToggleButton automatToggle;

    @FXML private ToggleButton singleToggle;

    @FXML private TextField userTextField;

    @FXML private TextField resultTextField;

    @FXML private Button processButton;
    @FXML private Button resetButton;

    @FXML private Button clearButton;
    @FXML private Button doneButton;
    @FXML private Label errorLabel;
    private MainApplicationController mainApplicationController;
    private StringBuilder resultText;

    private FadeTransition errorTransition;

    @FXML public void initialize(){
        errorLabel.setOpacity(0);
        errorLabel.getStyleClass().add("error-message");
        errorTransition = createErrorTransition();
        resultTextField.setDisable(true);

        PersistentButtonToggleGroup toggleGroup = new PersistentButtonToggleGroup();
        automatToggle.setToggleGroup(toggleGroup);
        singleToggle.setToggleGroup(toggleGroup);

        singleToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            mainApplicationController.singleModeOn(newValue);
            if(newValue){
                clearAllFields();
            }
        });

        automatToggle.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue){
                clearAllFields();
            }
        }));

        SegmentedButton segmentedButton = new SegmentedButton(automatToggle, singleToggle);
        segmentedButton.setToggleGroup(toggleGroup);
        segmentedButton.getStyleClass().add(SegmentedButton.STYLE_CLASS_DARK);

        toggleContainer.getChildren().clear();
        toggleContainer.getChildren().add(segmentedButton);
        processButton.visibleProperty().bind(automatToggle.selectedProperty());
        doneButton.visibleProperty().bind(singleToggle.selectedProperty());

        userTextField.setOnAction(e -> {
            if(singleToggle.isSelected()){
                handleDoneButton();
            }
            else{
                handleProcessButton();
            }
        });

        resultText = new StringBuilder();
        userTextField.setOnKeyTyped(ke -> {
            String characterTyped = ke.getCharacter().toUpperCase();
            if(!mainApplicationController.checkForKeyInKeyBoard(characterTyped.charAt(0))
            && !characterTyped.equals(System.lineSeparator())){
                //ENTER IS ALSO HERE :(
                //CTRL+V IS ALSO HERE...
                errorLabel.setText(ke.getCharacter() + " is not part of this machine's keyboard");
                errorTransition.play();
                ke.consume();
            }
        });
        userTextField.textProperty().addListener((observable, oldValue, newValue)
                                                    -> textListener(oldValue, newValue));

        processButton.setOnAction(e -> handleProcessButton());
        doneButton.setOnAction(e -> handleDoneButton());
        resetButton.setOnAction(e -> handleResetButton());
        clearButton.setOnAction(e -> clearAllFields());
    }

    private FadeTransition createErrorTransition() {
        errorTransition = new FadeTransition(Duration.millis(3500), errorLabel);
        errorTransition.setFromValue(0);
        errorTransition.setToValue(1.0);
        errorTransition.setCycleCount(2);
        errorTransition.setAutoReverse(true);
        return errorTransition;
    }

    private void clearAllFields() {
        userTextField.clear();
        resultText.delete(0, resultText.length());
        resultTextField.clear();
    }

    private void handleResetButton() {
        mainApplicationController.codeResetRequested();
    }

    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }


    private void textListener(String oldValue, String newValue){
        if(singleToggle.isSelected()){
            int change = newValue.length() - oldValue.length();
            if(change > 1){
                userTextField.setText(oldValue);
                errorLabel.setText("You're in single mode, pasting is not supported");
                errorTransition.play();
            }
            else if(change == 1){
                resultText.append(mainApplicationController.processSingleCharacter(newValue.charAt(0)));
                resultTextField.setText(resultText.toString());
                //call the keyboard component... notify
            }
            else{
                resultText.delete(resultText.length() + change, resultText.length());
                resultTextField.setText(resultText.toString());
            }
        }
    }

    private void handleProcessButton() {
        mainApplicationController.onProcessButtonPressed(userTextField.getText());
    }

    private void handleDoneButton(){
        mainApplicationController.onSingleModeDone(userTextField.getText(), resultTextField.getText());
    }

    public void keyPressed(String text){
        userTextField.setText(userTextField.getText().concat(text));
    }

    public void showOutput(String output) {
        resultTextField.setText(output);
    }
}
