package components.keyBoardComponent;

import application.MainApplicationController;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import java.util.concurrent.Callable;

public class KeyBoardComponentController {

    @FXML private FlowPane keyBoardFlowPane;

    @FXML private Button keybutton;
    private MainApplicationController mainApplicationController;

    public void activateKeyAnimation(Character lastProcessedChar) {
        Button b =  getButtonByChar(lastProcessedChar);
        Rectangle rect = new Rectangle(400, 200, Color.CYAN);

        final Color startColor = Color.web("#ececec");
        final Color endColor = Color.web("#fdee00");

        final ObjectProperty<Color> color = new SimpleObjectProperty<Color>(startColor);

        // String that represents the color above as a JavaFX CSS function:
        // -fx-body-color: rgb(r, g, b);
        // with r, g, b integers between 0 and 255
        final StringBinding cssColorSpec = Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return String.format("-fx-body-color: rgb(%d, %d, %d);",
                        (int) (256*color.get().getRed()),
                        (int) (256*color.get().getGreen()),
                        (int) (256*color.get().getBlue()));
            }
        }, color);

        // bind the button's style property
        b.styleProperty().bind(cssColorSpec);

        final Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(color, startColor)),
                new KeyFrame(Duration.seconds(1), new KeyValue(color, endColor)));
        timeline.setAutoReverse(true);
        timeline.setCycleCount(2);

        timeline.play();
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