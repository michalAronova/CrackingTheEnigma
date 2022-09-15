package components.keyBoardComponent;

import application.MainApplicationController;
import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.util.List;

public class KeyBoardComponentController {

    @FXML private FlowPane keyBoardFlowPane;

    @FXML private Button keybutton;
    private MainApplicationController mainApplicationController;

    public void activateKeyAnimation(Character lastProcessedChar) {
        Button b =  getButtonByChar(lastProcessedChar);
        Rectangle rect = new Rectangle(400, 200, Color.CYAN);

        /*FillTransition fillTransition = new FillTransition(Duration.seconds(2), rect);
        fillTransition.setFromValue(Color.BLUEVIOLET);
        fillTransition.setToValue(Color.AZURE);
        fillTransition.setCycleCount(FillTransition.INDEFINITE);
        fillTransition.play();*/
        //String style = b.getStyle();
        //b.setStyle("-fx-background-color: #fffb00");
        //b.setStyle(style);
        //Shape buttonShape = getButtonByChar(lastProcessedChar).getShape();
        /*FillTransition ft = new FillTransition(Duration.millis(3000), buttonShape , Color.RED, Color.BLUE);
        ft.setCycleCount(3);
        ft.setAutoReverse(true);
        ft.play();*/
    }
    public Button getButtonByChar(Character lastProcessedChar){
        ObservableList<Node> buttonsList = keyBoardFlowPane.getChildren();
        for (int i = 0; i < buttonsList.size(); i++) {
            Button b = (Button)keyBoardFlowPane.getChildren().get(i);
            if(b.getText().equals(lastProcessedChar.toString())){
                return b;
            }
        }
        return null;
    }
    @FXML private void initialize() {}
    @FXML public void setComponent(List<Character> ABC) {
        if(!keyBoardFlowPane.getChildren().isEmpty()){
            keyBoardFlowPane.getChildren().clear();
        }
        for (Character c: ABC) {
            Button button = createNewButton(c.toString());
            button.setOnAction(e -> onKeyButtonPressed(button.getText()));
            keyBoardFlowPane.getChildren().add(button);
        }
        setFlowPaneDisable(true);
    }

    private Button createNewButton(String let){
        Button button = new Button(let);
        button.setPadding(new Insets(3, 3, 3, 3));
        button.setText(let);
        button.setId("keyButton"+let);
        button.setPrefWidth(32);
        button.setPrefHeight(32);
        return button;
    }
    private void onKeyButtonPressed(String text){
        mainApplicationController.keyBoardButtonPressed(text);
    }

    public void setFlowPaneDisable(boolean disable){
        keyBoardFlowPane.setDisable(disable);
    }
    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }
}