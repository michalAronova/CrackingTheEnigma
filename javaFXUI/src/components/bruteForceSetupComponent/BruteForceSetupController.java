package components.bruteForceSetupComponent;

import application.MainApplicationController;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static java.lang.Integer.parseInt;

public class BruteForceSetupController {
    @FXML public GridPane rootGrid;
    @FXML private Label totalMissionNumLabel;
    @FXML private Label agentsNumLabel;
    @FXML private Slider numberOfAgentsSlider;
    @FXML private ComboBox<?> difficultyComboBox;
    @FXML private TextField missionSizeTextBox;
    @FXML private Button bruteForceSetButton;
    @FXML private Button bruteForceStartButton;
    private MainApplicationController mainApplicationController;
    private int agentCount;
    private IntegerProperty agentAmountChosenProperty;
    private StringProperty decryptionProperty;
    private IntegerProperty missionSizeProperty;
    private final String myStyleSheet = "bruteForceSetup";

    public BruteForceSetupController(){
        decryptionProperty = new SimpleStringProperty(null);
        agentAmountChosenProperty = new SimpleIntegerProperty(0);
        missionSizeProperty = new SimpleIntegerProperty(0);
    }

    @FXML public void initialize(){
        missionSizeTextBox.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));

        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        };

        missionSizeTextBox.setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));

        Bindings.bindBidirectional(missionSizeTextBox.textProperty(),
                missionSizeProperty,
                new NumberStringConverter());

        Bindings.bindBidirectional(agentsNumLabel.textProperty(),
                agentAmountChosenProperty,
                new NumberStringConverter());


        bindSetButton();

        bruteForceStartButton.setDisable(true);
    }

    private void bindSetButton() {
        bruteForceSetButton.disableProperty()
                .bind(Bindings.createBooleanBinding(
                        () -> decryptionProperty.getValue() == null
                                || agentAmountChosenProperty.getValue() == 0
                                || missionSizeProperty.getValue() == 0
                                || difficultyComboBox.valueProperty().getValue() == null,
                        decryptionProperty, agentAmountChosenProperty,
                        missionSizeProperty, difficultyComboBox.valueProperty()));
    }

    public void initialBruteForceSetUp(){
        initializeSlider();
        initialDiffComboBox();
        bruteForceStartButton.setDisable(true);
    }

    private void initializeSlider() {
        numberOfAgentsSlider.setMax(mainApplicationController.getAgentCount());
        numberOfAgentsSlider.setMin(1.0);

        numberOfAgentsSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                agentsNumLabel.setText(Integer.toString((int)numberOfAgentsSlider.getValue())));
    }

    private void initialDiffComboBox() {
        difficultyComboBox.getItems().clear();
        List<String> list = new ArrayList<String>();
        list.add("Easy");
        list.add("Medium");
        list.add("Hard");
        list.add("Not Possible");
        ObservableList obList = FXCollections.observableList(list);
        difficultyComboBox.setItems(obList);
    }

    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }

    public void setAgentCount(int agentCount) { this.agentCount = agentCount; }

    public void onSetButton() {
//        mainApplicationController.onBruteForceSet(parseInt(agentsNumLabel.getText()),
//                                    difficultyComboBox.getValue().toString(),
//                                    parseInt(missionSizeTextBox.getText()));
//        mainApplicationController.calculateTotalMissionAmount();
        mainApplicationController.onBruteForceSet(agentAmountChosenProperty.getValue(),
                difficultyComboBox.getValue().toString(),
                missionSizeProperty.getValue());
        mainApplicationController.calculateTotalMissionAmount();

        bruteForceStartButton.setDisable(false);
    }

    public void onStartButton(){
        bruteForceStartButton.setDisable(true);
        bruteForceSetButton.disableProperty().unbind();
        mainApplicationController.startBruteForce(decryptionProperty.getValue());
    }

    public void updateTotalMissionAmount(Double totalMissionAmount) {
        totalMissionNumLabel.setText(new DecimalFormat("#,###")
                                        .format(totalMissionAmount));
    }

    public void toggleSetUpToAction(boolean isActive) {
        //bruteForceSetButton.disableProperty().unbind();
        //bruteForceSetButton.setDisable(isActive);
        bruteForceStartButton.setDisable(isActive);
//        if(bruteForceSetButton.isDisabled()){
//            bindSetButton();
//        }
    }

    public void setDecryptionString(String output) {
        decryptionProperty.set(output);
    }

    public void changeTheme(Object cssPrefix) {
        rootGrid.getStylesheets().clear();
        if(cssPrefix != null){
            String css = cssPrefix + myStyleSheet;
            rootGrid.getStylesheets()
                    .add(getClass().getClassLoader().getResource(String.format("%s.css", css)).toExternalForm());
        }
    }
}
