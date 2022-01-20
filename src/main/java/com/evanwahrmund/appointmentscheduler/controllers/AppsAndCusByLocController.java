package com.evanwahrmund.appointmentscheduler.controllers;

import com.evanwahrmund.appointmentscheduler.daos.ReportsDatabaseDao;
import com.evanwahrmund.appointmentscheduler.util.ReportVal;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controller for FXML file: apps_and_cus_by_loc_report.fxml. Provides 4 reports with respective TableViews:
 * 1. Apointments by Country 2. Appointments by Division 3. Customers by Country 4. Customers by Division.
 */
public class AppsAndCusByLocController {

    /**
     * TableView For Appointments by Country
     */
    @FXML private TableView<ReportVal> appointmentsTableCountry;
    /**
     * Country table column (appointments)
     */
    @FXML private TableColumn<ReportVal, String> countryAppCol;
    /**
     * number of apps by country col
     */
    @FXML private TableColumn<ReportVal, Integer> countryNumAppCol;
    /**
     * TableView for Customers by Country
     */
    @FXML private TableView<ReportVal> customersTableCountry;
    /**
     * country table column (customers)
     */
    @FXML private TableColumn<ReportVal, String> countryCusCol;
    /**
     * number of customers by country col
     */
    @FXML private TableColumn<ReportVal, Integer> countryNumCusCol;

    /**
     * TableView for Appointments by Division
     */
    @FXML private TableView<ReportVal> appointmentsTableDiv;
    /**
     * division table column (appointments)
     */
    @FXML private TableColumn<ReportVal, String> divAppCol;
    /**
     * number of apps by division col
     */
    @FXML private TableColumn<ReportVal, Integer> divNumAppCol;
    /**
     * TableView for Customers by Division
     */
    @FXML private TableView<ReportVal> customersTableDiv;
    /**
     * division table col (customers)
     */
    @FXML private TableColumn<ReportVal, String> divCusCol;
    /**
     * number of customers by division col
     */
    @FXML private TableColumn<ReportVal, Integer> divNumCusCol;

    /**
     * Initializes tables and sets values
     */
    public void initialize(){
        initializeTables();
        appointmentsTableCountry.setItems(ReportsDatabaseDao.getInstance().getAppsByCountry());
        customersTableCountry.setItems(ReportsDatabaseDao.getInstance().getCusByCountry());
        appointmentsTableDiv.setItems(ReportsDatabaseDao.getInstance().getAppsByFirstLevelDiv());
        customersTableDiv.setItems(ReportsDatabaseDao.getInstance().getCusByFirstLevelDiv());

    }

    /**
     * Initializes table columns for all tables
     */
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
