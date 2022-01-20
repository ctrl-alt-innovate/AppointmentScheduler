package com.evanwahrmund.appointmentscheduler.controllers;

import java.time.LocalDate;

import com.evanwahrmund.appointmentscheduler.daos.ReportsDao;
import com.evanwahrmund.appointmentscheduler.util.ReportVal;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * Controller for FXML file: apps_by_type_and_month.fxml. Provides report for Appointments by Type and Month.
 */
public class AppsByTypeAndMonthController {
    /**
     * TableView for Appointments by Type and Month report
     */
    @FXML private TableView<ReportVal> typeAndMonthTable;
    /**
     * Column for type of appointment
     */
    @FXML private TableColumn<ReportVal, String> typeCol;
    /**
     * column for month of appointment
     */
    @FXML private TableColumn<ReportVal, LocalDate> monthCol;
    /**
     * column for number of appointments with month and type
     */
    @FXML private TableColumn<ReportVal, Integer> numAppsCol;

    /**
     * Initializes table and sets values
     */
    public void initialize(){
        initializeTables();
        ObservableList <ReportVal> typelist = FXCollections.observableArrayList(ReportsDao.getInstance().getAppsByTypeAndMonth());
        typeAndMonthTable.setItems(typelist);
    }

    /**
     * Initializes table columns
     */
    private void initializeTables(){
        monthCol.setCellValueFactory(cell -> {
            String month = cell.getValue().getFirstCol().toString();
            return new ReadOnlyObjectWrapper(month.substring(0,1) + month.substring(1).toLowerCase());
        });
        typeCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getSecondCol()));
        numAppsCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getThirdCol()));

    }


}
