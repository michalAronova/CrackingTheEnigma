package components.processComponent;

import application.MainApplicationController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import org.controlsfx.control.SegmentedButton;

import java.util.Random;

public class ProcessComponentController {

    @FXML private HBox toggleContainer;
    @FXML private ToggleButton automatToggle;

    @FXML private ToggleButton singleToggle;

    @FXML private TextField userTextField;

    @FXML private TextField resultTextField;

    @FXML private Button processButton;

    @FXML private Button clearButton;
    @FXML private Button doneButton;
    @FXML private Label errorLabel;

    private MainApplicationController mainApplicationController;

    private StringBuilder resultText;

    @FXML public void initialize(){
        errorLabel.setOpacity(0);
        resultTextField.setDisable(true);

        PersistentButtonToggleGroup toggleGroup = new PersistentButtonToggleGroup();
        automatToggle.setToggleGroup(toggleGroup);
        singleToggle.setToggleGroup(toggleGroup);

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
        userTextField.textProperty().addListener((observable, oldValue, newValue)
                                                    -> textListener(oldValue, newValue));

        processButton.setOnAction(e -> handleProcessButton());
    }

    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }


    private void textListener(String oldValue, String newValue){
        //if not in keyboard, notify then return
        //use of errorLabel with animation of "opacity" to show up and disappear
        if(singleToggle.isSelected()){
            if(oldValue.length() < newValue.length()){
                resultText.append(singleProcess());
                resultTextField.setText(resultText.toString());
                //call the keyboard component... notify
            }
            else{
                resultText.deleteCharAt(resultText.length() - 1);
                resultTextField.setText(resultText.toString());
            }
        }
    }

    private void handleProcessButton() {
        String userText = userTextField.getText();
        resultText.delete(0, resultText.length());
        for(char c: userText.toCharArray()){
            resultText.append(singleProcess());
        }
        resultTextField.setText(resultText.toString());
    }

    private void handleDoneButton(){
        //does something...
    }

    private char singleProcess(){
        String str = "abcdefghijklmnop";
        Random rand = new Random();
        char[] arr = str.toCharArray();
        return arr[rand.nextInt(arr.length - 1)];
    }

    public void keyPressed(){
        //this will be called from the keyboard component!
    }
}
