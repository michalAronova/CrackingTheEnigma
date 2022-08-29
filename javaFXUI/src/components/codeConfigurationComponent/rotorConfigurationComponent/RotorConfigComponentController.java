package components.codeConfigurationComponent.rotorConfigurationComponent;

import components.codeConfigurationComponent.CodeConfigComponentController;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RotorConfigComponentController {

    @FXML private Label rotorIDChosenLabel;
    @FXML private ComboBox<Integer> rotorIDComboBox;
    @FXML private HBox rotorConfigHBox;
    @FXML private VBox rotorChoiceVBBox;

    private final List<VBox> singleRotorComponentList;
    private List<Integer> rotorIDs;
    private List<Character> rotorPos;
    private int rotorsSet = 0;
    private CodeConfigComponentController codeConfigComponentController;
    private final String DEFAULT_ROTOR_ID = "ID";

    public RotorConfigComponentController(){
        singleRotorComponentList = new ArrayList<>();
    }

    @FXML public void initialize(){
        rotorIDComboBox.setOnAction(e -> onComboBoxChoice());
    }

    public void setCodeConfigController(CodeConfigComponentController codeConfigComponentController){
        this.codeConfigComponentController = codeConfigComponentController;
    }

    public void setComponent(int rotorsRequired, int totalRotorAmount, List<Character> keys){
        rotorIDs = Stream.generate(() -> 0)
                            .limit(rotorsRequired)
                            .collect(Collectors.toList());
        rotorPos = Stream.generate(() -> keys.get(0))
                            .limit(rotorsRequired)
                            .collect(Collectors.toList());

        for(int i = 0; i < rotorsRequired; i++) {
            VBox vBox = createRotorConfigVBox(keys, i);
            rotorConfigHBox.getChildren().add(vBox);
            singleRotorComponentList.add(i, vBox);
        }
        setRotorsComboBox(totalRotorAmount);
    }
    private void setRotorsComboBox(int totalRotorAmount) {
        List<Integer> comboBoxValues = new ArrayList<>();
        for(int i = 1; i <= totalRotorAmount; i++) {
            comboBoxValues.add(i);
        }

        ObservableList<Integer> obList = FXCollections.observableList(comboBoxValues);
        rotorIDComboBox.getItems().clear();
        rotorIDComboBox.getItems().addAll(comboBoxValues);
    }

    private void onComboBoxChoice(){
        setOptionRotorLabel(rotorIDComboBox.getValue());
        rotorIDChosenLabel.setDisable(false);
    }
    private VBox createRotorConfigVBox(List<Character> keys, int index){
        //rotor ID label
        Label rotorIDLabel = createRotorIDDragBox(index);

        //undoBtn
        AnchorPane buttonAnchorPane = createButtonAnchorPane(rotorIDLabel, index);

        //spinner
        Spinner<Character> positionSpinner = createPositionSpinner(keys, index);

        //VBox
        VBox vBox = new VBox(buttonAnchorPane, rotorIDLabel, positionSpinner);
        setRotorVBoxLayout(rotorIDLabel, positionSpinner, vBox);

        return vBox;
    }

    private void setRotorVBoxLayout(Label rotorIDLabel, Spinner<Character> positionSpinner, VBox vBox) {
        vBox.setAlignment(Pos.CENTER);
        vBox.setLayoutX(12.0);
        vBox.setLayoutY(12.0);
        vBox.setPadding(new Insets(5, 5, 5, 5));
        VBox.setMargin(rotorIDLabel,new Insets(10, 10, 10, 10));
        VBox.setMargin(positionSpinner,new Insets(5, 5, 5, 5));
    }

    private Spinner<Character> createPositionSpinner(List<Character> keys, int index) {
        Spinner<Character> positionSpinner = new Spinner<>();
        Collections.reverse(keys);
        ObservableList<Character> values = FXCollections.observableArrayList(keys);
        Collections.reverse(keys);
        SpinnerValueFactory<Character> valueFactory =
                new SpinnerValueFactory.ListSpinnerValueFactory<Character>(values);

        //positionSpinner.setEditable(true);

        positionSpinner.valueProperty().addListener(((observable, oldValue, newValue) -> {
            onPosSpinnerRotated(index, newValue);
        }));

        rotorPos.set(index, positionSpinner.getValue());

        positionSpinner.setValueFactory(valueFactory);
        positionSpinner.setPrefHeight(40);
        positionSpinner.setPrefWidth(70);
        positionSpinner.setMaxHeight(40);
        positionSpinner.setMaxWidth(70);
        positionSpinner.setMinHeight(40);
        positionSpinner.setMinWidth(70);
        positionSpinner.setPadding(new Insets(5, 5, 5, 5));
        return positionSpinner;
    }

    private AnchorPane createButtonAnchorPane(Label rotorIDLabel, int index) {
        AnchorPane buttonAnchorPane = new AnchorPane();
        Button undoButton = new Button("x");
        undoButton.setOnAction(e -> handleUndoButton(rotorIDLabel, index));
        undoButton.setLayoutX(55.0);
        undoButton.setMaxHeight(22.0);
        undoButton.setMaxWidth(22.0);
        undoButton.setMinHeight(22.0);
        undoButton.setMinWidth(22.0);
        undoButton.setPrefHeight(22.0);
        undoButton.setPrefWidth(22.0);
        undoButton.setMnemonicParsing(false);
        buttonAnchorPane.getChildren().add(undoButton);
        return buttonAnchorPane;
    }

    private void onPosSpinnerRotated(int index, Character value) {
        rotorPos.set(index, value);
    }

    private void handleUndoButton(Label rotorID, int index) {
        if(!rotorID.getText().equals(DEFAULT_ROTOR_ID)) {
            setOptionRotorLabel(Integer.parseInt(rotorID.getText())); //undo rotor id choice
            rotorRemoved(index, Integer.parseInt(rotorID.getText()));
        }
        rotorID.setText(DEFAULT_ROTOR_ID); //return the chosen id to chosen id label
        rotorIDChosenLabel.setDisable(false);
    }

    public void reset(){
        singleRotorComponentList.forEach(component ->
                rotorConfigHBox.getChildren().remove(component));
        rotorIDComboBox.setItems(FXCollections.observableArrayList(new ArrayList<>()));
        rotorIDComboBox.getItems().clear();
        singleRotorComponentList.clear();

        rotorIDChosenLabel.setText(DEFAULT_ROTOR_ID);
        rotorIDChosenLabel.setOnDragDetected(Event::consume);
        rotorIDChosenLabel.setOnDragDone(Event::consume);

        rotorsSet = 0;
    }

    private void setOptionRotorLabel(int number) {
        rotorIDChosenLabel.setText(number +"");

        rotorIDChosenLabel.setOnDragDetected((event) -> {
            WritableImage snapshot = rotorIDChosenLabel.snapshot(new SnapshotParameters(), null);
            Dragboard db = rotorIDChosenLabel.startDragAndDrop(TransferMode.ANY);

            ClipboardContent content = new ClipboardContent();
            content.putString(number + "");
            db.setContent(content);
            db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);

            event.consume();
        });

        rotorIDChosenLabel.setOnDragDone((event) -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                rotorIDChosenLabel.setText("");
                rotorIDChosenLabel.setDisable(true);
            }
            event.consume();
        });
    }

    private Label createRotorIDDragBox(int index) {
        Label dragBox = new Label(DEFAULT_ROTOR_ID);
        dragBox.setCenterShape(true);
        dragBox.setPadding(new Insets(10, 10, 10, 10));
        dragBox.setAlignment(Pos.CENTER_RIGHT);
        dragBox.getStyleClass().add("rotorIDDragBox");

        dragBox.setOnDragOver((event) -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });

        dragBox.setOnDragEntered((event) -> {
            if (event.getDragboard().hasString()) {
                dragBox.getStyleClass().remove("rotorIDDragBox");
                dragBox.getStyleClass().add("onDragEntered");
            }
            event.consume();
        });

        dragBox.setOnDragExited((event) -> {
            dragBox.getStyleClass().remove("onDragEntered");
            event.consume();
        });

        dragBox.setOnDragDropped((event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                handleUndoButton(dragBox, index);
                dragBox.setText(db.getString());
                success = true;
                rotorSet(index, Integer.parseInt(db.getString()));
            }
            event.setDropCompleted(success);
            event.consume();
        });

        return dragBox;
    }

    private void rotorRemoved(int index, int rotorID){

        rotorIDs.set(index, 0);
        rotorsSet--;
        codeConfigComponentController.getRotorsFilledProperty().setValue(false);
    }
    private void rotorSet(int index, int rotorID){

        rotorIDs.set(index, rotorID);
        rotorsSet++;
        if(rotorsSet == singleRotorComponentList.size()){
            codeConfigComponentController.onRotorsCompleted(rotorIDs, rotorPos);
            codeConfigComponentController.getRotorsFilledProperty().setValue(true);
        }
    }

    public void removeChoices() {
        rotorsSet = 0;

        singleRotorComponentList.forEach(vBox -> {
            for (int i = 0; i < vBox.getChildren().size(); i++) {
                if(vBox.getChildren().get(i) instanceof Label){
                    ((Label) vBox.getChildren().get(i)).setText(DEFAULT_ROTOR_ID);
                }
            }
        });

        codeConfigComponentController.getRotorsFilledProperty().setValue(false);

        rotorIDChosenLabel.setText(DEFAULT_ROTOR_ID);
        rotorIDChosenLabel.setOnDragDetected(Event::consume);
        rotorIDChosenLabel.setOnDragDone(Event::consume);
        rotorIDComboBox.setValue(1);
    }
}
