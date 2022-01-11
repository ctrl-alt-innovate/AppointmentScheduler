package com.evanwahrmund.appointmentscheduler;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AppsAndCusByLocController {


    @FXML private TableView<ReportVal> appointmentsTableCountry;
    @FXML private TableColumn<ReportVal, String> countryAppCol;
    @FXML private TableColumn<ReportVal, Integer> countryNumAppCol;

    @FXML private TableView<ReportVal> customersTableCountry;
    @FXML private TableColumn<ReportVal, String> countryCusCol;
    @FXML private TableColumn<ReportVal, Integer> countryNumCusCol;


    @FXML private TableView<ReportVal> appointmentsTableDiv;
    @FXML private TableColumn<ReportVal, String> divAppCol;
    @FXML private TableColumn<ReportVal, Integer> divNumAppCol;

    @FXML private TableView<ReportVal> customersTableDiv;
    @FXML private TableColumn<ReportVal, String> divCusCol;
    @FXML private TableColumn<ReportVal, Integer> divNumCusCol;


    public void initialize(){
        appointmentsTableCountry.setItems(ReportsDao.getInstance().getAppsByCountry());
        customersTableCountry.setItems(ReportsDao.getInstance().getCusByCountry());

        initializeTables();
    }

    private void initializeTables() {
        countryAppCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getFirstCol()));
        countryNumAppCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getSecondCol()));

        countryCusCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getFirstCol()));
        countryNumCusCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getSecondCol()));

        divAppCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getFirstCol()));
        divNumAppCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getSecondCol()));

        divCusCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getFirstCol()));
        divNumCusCol.setCellValueFactory(cell -> new ReadOnlyObjectWrapper(cell.getValue().getSecondCol()));
    }


}
