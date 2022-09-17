package components.codeHistoryComponent;

import application.MainApplicationController;
import etc.columnFormatter.ColumnFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.text.*;

public class CodeHistoryComponentController {
    @FXML private TableView<StatisticData> codeHistoryTableView;

    @FXML private TableColumn<StatisticData, String> codeColumn;

    @FXML private TableColumn<StatisticData, String> inputColumn;

    @FXML private TableColumn<StatisticData, String> outputColumn;
    private String currentCode;
    ObservableList<StatisticData> codes = FXCollections.observableArrayList();
    private MainApplicationController mainApplicationController;

    @FXML private TableColumn<StatisticData, String> timeElapsedColumn;

    public void initialize() {
        codeHistoryTableView.setEditable(false);
        //codeHistoryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        codeColumn.setEditable(false);
        codeColumn.setSortable(false);
        inputColumn.setCellValueFactory(new PropertyValueFactory<>("input"));
        inputColumn.setEditable(false);
        inputColumn.setSortable(false);
        outputColumn.setCellValueFactory(new PropertyValueFactory<>("output"));
        outputColumn.setEditable(false);
        outputColumn.setSortable(false);
        //timeElapsedColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeElapsedColumn.setCellValueFactory(cellData -> {
            String formattedData = new DecimalFormat("#,###").format(cellData.getValue().getTime());
            return new SimpleStringProperty(formattedData);
        });
        timeElapsedColumn.setEditable(false);
        timeElapsedColumn.setSortable(false);
    }
    public void setMainApplicationController(MainApplicationController mainApplicationController){
        this.mainApplicationController = mainApplicationController;
    }
    public void addNewProcess(StatisticData process) {
        if(codes.isEmpty()){
            currentCode = process.getCode();
        }
        else{
            if(process.getCode().equals(currentCode)){
                process.setCode("---");
            }
            else{
                currentCode = process.getCode();
            }
        }
        codes.add(process);
        codeHistoryTableView.getItems().add(process);
    }
}
