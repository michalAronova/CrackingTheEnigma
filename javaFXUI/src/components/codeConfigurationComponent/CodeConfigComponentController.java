package components.codeConfigurationComponent;

import DTO.codeObj.CodeObj;
import application.MainApplicationController;
import components.codeConfigurationComponent.plugsComponent.PlugsComponentController;
import components.codeConfigurationComponent.rotorConfigurationComponent.RotorConfigComponentController;
import enigmaMachine.reflector.ReflectorID;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import utils.AutoCompleteComboBox;
import utils.AutoCompleteComboBox.HideableItem;
import java.util.ArrayList;
import java.util.List;

public class CodeConfigComponentController {

    @FXML public Button setByManualButton;
    @FXML public Button setByRandomButton;
    @FXML public Label messageLabel;
    @FXML public Accordion codeConfigurationAccordion;
    @FXML public Button setButton;
    @FXML private ComboBox<String> reflectorComboBox;

    @FXML private PlugsComponentController plugsComponentController;
    @FXML private RotorConfigComponentController rotorConfigComponentController;
    private final BooleanProperty rotorsFilled;
    private List<Integer> rotorIDs;
    private List<Character> rotorPositions;

    private MainApplicationController mainApplicationController;

    public CodeConfigComponentController(){
        rotorsFilled = new SimpleBooleanProperty(false);
    }

    @FXML public void initialize(){
        if(plugsComponentController != null && rotorConfigComponentController != null){
            plugsComponentController.setCodeConfigController(this);
            rotorConfigComponentController.setCodeConfigController(this);
        }
        System.out.println(reflectorComboBox.valueProperty());
        setButton.disableProperty().bind(Bindings
                .createBooleanBinding(()->
                        reflectorComboBox.valueProperty().getValue() == null || !rotorsFilled.getValue()
                        , reflectorComboBox.valueProperty(), rotorsFilled));

        reflectorComboBox.setPromptText("Reflector ID");
        setByRandomButton.setDisable(true);
        setByManualButton.setDisable(true);
    }
    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }
    @FXML public void onSetByManualClicked(ActionEvent actionEvent) {
        codeConfigurationAccordion.setDisable(false);
        setByManualButton.setDisable(true);
    }

    public BooleanProperty getRotorsFilledProperty(){ return rotorsFilled; }
    @FXML public void onSetByRandomClicked(ActionEvent actionEvent) {
        codeConfigurationAccordion.setDisable(true);
        setByManualButton.setDisable(false);
        removeAllChoices();
        mainApplicationController.handleSetByRandom();
    }

    @FXML public void onSetClicked(ActionEvent actionEvent) {
        sendManualCodeToParentController();
        removeAllChoices();
    }

    private void sendManualCodeToParentController() {
        CodeObj code = new CodeObj();
        code.setRotorIDs(this.rotorIDs);
        code.setRotorPos(this.rotorPositions);
        code.setPlugs(plugsComponentController.getSelectedPlugs());
        code.setReflectorID(reflectorComboBox.valueProperty().getValue());

        mainApplicationController.handleManualSet(code);

        removeAllChoices();
        codeConfigurationAccordion.setDisable(true);
        setByManualButton.setDisable(false);
    }

    private void removeAllChoices() {
        rotorConfigComponentController.removeChoices();
        plugsComponentController.removeChoices();
        reflectorComboBox.valueProperty().set(null);
    }

    private void resetConfigComponent(){
        codeConfigurationAccordion.setDisable(true);
        setByManualButton.setDisable(false);

        plugsComponentController.reset();

        rotorConfigComponentController.reset();
        rotorIDs = null;
        rotorPositions = null;

        reflectorComboBox.setItems(FXCollections.observableArrayList(new ArrayList<>()));
        reflectorComboBox.valueProperty().set(null);
    }

//    public void onRotorsUnDone(){
//        rotorsFilled = false;
//        setButton.setDisable(true);
//    }

    public void onRotorsCompleted(List<Integer> rotorIDs, List<Character> rotorPositions){
        //this method will be called from within the rotors controller
        //when the rotors have all been chosen ("this is the listener")
        this.rotorIDs = rotorIDs;
        this.rotorPositions = rotorPositions;
//        rotorsFilled.setValue();
//        rotorsFilled = true;
//
//        if(!reflectorChoice.equals("")){ //reflector was chosen in the combo box
//            setButton.setDisable(false);
//        }
    }

    public void handleFileLoaded(List<Character> keys, int rotorsRequired,
                                 int totalRotorAmount, int reflectorAmount){
        setByRandomButton.setDisable(false);
        setByManualButton.setDisable(false);
        resetConfigComponent();
        reflectorComboBox.setItems(FXCollections
                .observableArrayList(ReflectorID.getListToLimit(reflectorAmount)));
        plugsComponentController.setComponent(keys);
        rotorConfigComponentController.setComponent(rotorsRequired, totalRotorAmount, keys);
    }

//    public void handleReflectorComboBox(ActionEvent e){
//        reflectorChoice.setValue(reflectorComboBox.getValue());
////        if(rotorsFilled){
////            setButton.setDisable(false);
////        }
//    }

    //TEST METHOD
    private ComboBox<HideableItem<String>> createComboBox(){
        List<String> test = new ArrayList<>();
        test.add("yalla");
        test.add("you are");
        test.add("bacon");
        test.add("band");
        ComboBox<HideableItem<String>> comboBox =
                AutoCompleteComboBox
                        .createComboBoxWithAutoCompletionSupport(test,
                            new StringConverter() {
            @Override
            public String toString(Object object) {
                return object.toString();
            }

            @Override
            public Object fromString(String string) {
                return string;
            }
        });
        return comboBox;
    }


}
