package components.bruteForceSetupComponent;

import application.MainApplicationController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import static java.lang.Integer.parseInt;

public class BruteForceSetupController {
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

//        missionSizeTextBox.textProperty().addListener((obs,oldv,newv) -> {
//            try {
//                missionSizeTextBox.getTextFormatter().getValueConverter().fromString(newv);
//                // no exception above means valid
//                missionSizeTextBox.setBorder(null);
//            } catch (RuntimeException e) {
//                missionSizeTextBox.setBorder(new Border(new BorderStroke(
//                        Color.RED, BorderStrokeStyle.SOLID,
//                        new CornerRadii(3), new BorderWidths(2), new Insets(-2))));
//            }
//        });

        bindSetButton();

        bruteForceStartButton.setDisable(true);
    }

    private void bindSetButton() {
        Bindings.bindBidirectional(missionSizeTextBox.textProperty(),
                missionSizeProperty,
                new NumberStringConverter());

        Bindings.bindBidirectional(agentsNumLabel.textProperty(),
                agentAmountChosenProperty,
                new NumberStringConverter());

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
        totalMissionNumLabel.setText(totalMissionAmount.toString());
    }

    public void toggleSetUpToAction(boolean isActive) {
        bruteForceSetButton.disableProperty().unbind();
        bruteForceSetButton.setDisable(isActive);
        bruteForceStartButton.setDisable(isActive);
        if(bruteForceSetButton.isDisabled()){
            bindSetButton();
        }
    }

    public void setDecryptionString(String output) {
        decryptionProperty.set(output);
    }
}
