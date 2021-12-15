package com.evanwahrmund.appointmentscheduler;

import java.time.LocalDate;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AppsByTypeAndMonthController {
    @FXML private TableView<ReportVal> typeAndMonthTable;
    @FXML private TableColumn<ReportVal, String> typeCol;

    @FXML private TableColumn<ReportVal, LocalDate> monthCol;
    @FXML private TableColumn<ReportVal, Integer> numAppsCol;

    public void initialize(){
        initializeTables();
        ObservableList <ReportVal> typelist = FXCollections.observableArrayList(ReportsDao.getInstance().getAppsByTypeAndMonth());
        typeAndMonthTable.setItems(typelist);
    }
    private void initializeTables(){
        monthCol.setCellValueFactory(cell -> {
            String month = cell.getValue().getFirstCol().toString();
            return new ReadOnlyObjectWrapper(month.substring(0,1) + month.substring(1).toLowerCase());
        });
        typeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getSecondCol()));
        numAppsCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getThirdCol()));

    }


}
