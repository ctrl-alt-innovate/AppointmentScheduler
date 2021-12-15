package com.evanwahrmund.appointmentscheduler;
/*
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AppsAndCusByLocController {

    @FXML private RadioButton appCountryRadioButton;
    @FXML private RadioButton appFirstLevelDivRadioButton;
    @FXML private RadioButton cusCountryRadioButton;
    @FXML private RadioButton cusFirstLevelDivRadioButton;

    @FXML private TableView<ReportVal<String, Integer>> appointmentsTable;
    @FXML private TableColumn<ReportVal<String, Integer>, String> appCountryOrDivCol;
    @FXML private TableColumn<ReportVal<String, Integer>, Integer> appNumCol;
    @FXML private TableColumn<ReportVal<String, Integer>, ?> cusCountryOrDivCol;
    @FXML private TableColumn<ReportVal<String,Integer>, Integer> cusNumCol;
    @FXML private TableView<ReportVal<String, Integer>> customersTable;

    public void initialize(){
        appointmentsTable.setItems(ReportsDao.getInstance().getAppsByCountry());
        customersTable.setItems(ReportsDao.getInstance().getCusByCountry());
        appCountryRadioButton.setSelected(true);
        appFirstLevelDivRadioButton.setOnAction(event -> {
            appointmentsTable.setItems(ReportsDao.getInstance().getAppsByFirstLevelDiv());
            appCountryOrDivCol.setText("First_Level_Division");
        });
        appCountryRadioButton.setOnAction(event -> {
            appointmentsTable.setItems(ReportsDao.getInstance().getAppsByCountry());
            appCountryOrDivCol.setText("Country");
        });
        cusCountryRadioButton.setOnAction(event -> {
            customersTable.setItems(ReportsDao.getInstance().getCusByCountry());
            cusCountryOrDivCol.setText("Country");
        });
        cusFirstLevelDivRadioButton.setOnAction(event -> {
            customersTable.setItems(ReportsDao.getInstance().getCusByFirstLevelDiv());
            cusCountryOrDivCol.setText("First_Level_Division");
        });
        cusCountryRadioButton.setSelected(true);
        initializeTables();
    }

    private void initializeTables() {
        appNumCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getValue()));
        appCountryOrDivCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getItem()));
        cusNumCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getValue()));
        cusCountryOrDivCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getItem()));
    }


}
*/