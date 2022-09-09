package components.keyBoardComponent;

import application.MainApplicationController;
import javafx.animation.FillTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;

public class KeyBoardComponentController {

    @FXML private FlowPane keyBoardFlowPane;

    @FXML private Button keybutton;
    private MainApplicationController mainApplicationController;

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
        /*FillTransition ft = new FillTransition(Duration.millis(3000), this, Color.RED, Color.BLUE);
        ft.setCycleCount(3);
        ft.setAutoReverse(true);
        ft.play();*/
    }

    public void setFlowPaneDisable(boolean disable){
        keyBoardFlowPane.setDisable(disable);
    }
    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }
}