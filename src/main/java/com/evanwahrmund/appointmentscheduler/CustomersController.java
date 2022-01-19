package com.evanwahrmund.appointmentscheduler;

import com.evanwahrmund.appointmentscheduler.daos.ReadOnlyDatabaseDao;
import com.evanwahrmund.appointmentscheduler.models.Countries;
import com.evanwahrmund.appointmentscheduler.models.Country;
import com.evanwahrmund.appointmentscheduler.models.Division;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

/**
 * Controller for adding, removing, and updating Customers. <p>
 * Used by: FXML/customers.fxml.
 */
public class CustomersController {
    /**
     * tableview representing all customers
     */
    @FXML private TableView<Customer> customersTable;
    /**
     * tablecolumn representing id in customers table
     */
    @FXML private TableColumn<Customer, Integer> idCol;
    /**
     * tablecolumn reprsenting name in customers table
     */
    @FXML private TableColumn<Customer, String> nameCol;
    /**
     * tablecolumn representing phone number in customers table
     */
    @FXML private TableColumn<Customer, String> phoneCol;
    /**
     * tablecolumn representing address in customers table
     */
    @FXML private TableColumn<Customer, String> addressCol;
    /**
     * tablecolumn representing postal code in customers table
     */
    @FXML private TableColumn<Customer, String> postalCodeCol;
    /**
     * tablecolumn representing first level division in customers table
     */
    @FXML private TableColumn<Customer, Division> divisionCol;
    /**
     * tablecolumn representing country in customers table
     */
    @FXML private TableColumn<Customer, Country> countryCol;
    @FXML private ComboBox<Country> countryComboBox;
    @FXML private ComboBox<Division> divisionComboBox;
    @FXML private TextField idTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField postalCodeTextField;
    @FXML private TextField phoneTextField;
    @FXML private Button addButton;
    @FXML private Button modifyButton;
    @FXML private Button deleteButton;
    @FXML private Button saveButton;
    @FXML private Button resetButton;
    @FXML private Label deleteLabel;


    public void initialize() {
        initializeTable();
        initializeComboBoxes();
        loadTestData();
        addButton.setOnAction(event -> {
            createCustomer();
        });
        modifyButton.setOnAction(event -> modifyCustomer());
        deleteButton.setOnAction(event -> deleteCustomer());
        resetButton.setOnAction(event -> resetFields());
        saveButton.setOnAction(event -> updateCustomer());

        customersTable.setItems(Customers.getCustomers());
        customersTable.itemsProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Activated");
        });


    }
    private void initializeComboBoxes(){
        countryComboBox.getItems().addAll(Countries.getCountries());
        countryComboBox.setOnAction((event) -> {
            if (countryComboBox.getValue() != null){
                divisionComboBox.setItems(ReadOnlyDatabaseDao.getInstance().listDivisions(countryComboBox.getValue()));
                divisionComboBox.setValue(null);

            }
            //divisionComboBox.setItems(Data)
        });
        countryComboBox.setValue(ReadOnlyDatabaseDao.getInstance().getCountry(1));
        divisionComboBox.setItems(ReadOnlyDatabaseDao.getInstance().listDivisions(countryComboBox.getValue()));
        divisionComboBox.setValue(ReadOnlyDatabaseDao.getInstance().getDivision(3));
        //divisionComboBox.getItems().addAll(DatabaseReadOnlyDao.getInstance().listDivisions(new Country("United States")));

    }
    /**
     * Initializes customers table with columns representing customer attributes
     */
    @FXML
    private void initializeTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));

        divisionCol.setCellValueFactory( cell -> {
                return new ReadOnlyObjectWrapper(cell.getValue().getDivision().getName());
        });
        //divisionCol.setCellValueFactory( new PropertyValueFactory<>("division"));
        countryCol.setCellValueFactory( cell -> {
            return new ReadOnlyObjectWrapper(cell.getValue().getDivision().getCountry().getName());
        });
    }
    public void createCustomer(){
        try {
            String name = nameTextField.getText();
            String address = addressTextField.getText();
            String postalCode = postalCodeTextField.getText();
            String phone = phoneTextField.getText();
            Division div = divisionComboBox.getValue();
            if(name.isBlank() || address.isBlank() || postalCode.isBlank() || phone.isBlank()){
                throw new IllegalArgumentException("Fields can not be blank");
            }
            if (div == null) {
                throw new NullPointerException("No Division Selected. Please Choose a Country with an operating office. ");
            }
            Customer cus = new Customer(name, address, postalCode, phone, div);
            Customers.createCustomer(cus);
            Alert confirmation = new Alert(Alert.AlertType.INFORMATION, "Customer: " + cus.getName() + " created successfully!");
            confirmation.setHeaderText("Customer Created");
            confirmation.show();
            resetFields();
            //customersTable.refresh();

        } catch(NullPointerException | SQLException | IllegalArgumentException ex){
            ex.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "Error creating customer. Please ensure fields are filled and valid.");
            error.setContentText(error.getContentText() + "\n" + ex.getMessage());
            error.setHeaderText("Add Customer Error");
            error.show();


        }
    }
    public void deleteCustomer(){
        Customer cusToDelete = customersTable.getSelectionModel().getSelectedItem();
        if(cusToDelete == null) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Please Select a customer to delete.");
            error.setHeaderText("Customer Deletion Error");
            error.show();
            return;
        }
        for(Appointment a: Appointments.getAppointments()){
            if(a.getCustomer() == cusToDelete) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Customer Deletion Error");
                alert.setContentText("Customer " + cusToDelete.getName() + " can not be deleted due to having exisiting appointments." +
                        " Please delete appointments for this customer and try again.");
                alert.show();
                //deleteLabel.setText("Customer " + cusToDelete.getName() + " can not be deleted due to having existinng appointments.");
                return;
            }
        }
        try{
            Customers.deleteCustomer(cusToDelete);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer: " + cusToDelete.getName() + " deleted successfully.");
            alert.setHeaderText("Customer Deleted");
            alert.show();
        } catch (SQLException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error in deleting customer.");
            alert.setHeaderText("Customer Deletion Unexpected Error");
        }

    }
    public void resetFields(){
        idTextField.clear();
        nameTextField.clear();
        addressTextField.clear();
        postalCodeTextField.clear();
        phoneTextField.clear();
        countryComboBox.setValue(null);
        divisionComboBox.setValue(null);

    }

    public void modifyCustomer(){
        Customer cusToMod = customersTable.getSelectionModel().getSelectedItem();
        idTextField.setText(String.valueOf(cusToMod.getId()));
        nameTextField.setText(cusToMod.getName());
        addressTextField.setText(cusToMod.getAddress());
        postalCodeTextField.setText(cusToMod.getPostalCode());
        phoneTextField.setText(cusToMod.getPhone());
        countryComboBox.setValue(cusToMod.getDivision().getCountry());
        divisionComboBox.setValue(cusToMod.getDivision());
    }

    public void updateCustomer(){
        try {
            if(nameTextField.getText().isBlank() || addressTextField.getText().isBlank() || postalCodeTextField.getText().isBlank() ||
                    phoneTextField.getText().isBlank()){
                throw new IllegalArgumentException("Fields can not be left blank.");
            }
            if (divisionComboBox.getValue() == null) {
                throw new IllegalArgumentException("Please select a valid First Level Division.");
            }

            Customer cusToUpdate = customersTable.getSelectionModel().getSelectedItem();

            cusToUpdate.setName(nameTextField.getText());
            cusToUpdate.setAddress(addressTextField.getText());
            cusToUpdate.setPostalCode(postalCodeTextField.getText());
            cusToUpdate.setPhone(phoneTextField.getText());
            cusToUpdate.setDivision(divisionComboBox.getValue());
            Customers.updateCustomer(cusToUpdate);
            Alert confirm = new Alert(Alert.AlertType.INFORMATION, "Customer: " + cusToUpdate.getName() + " updated successfully.");
            confirm.setHeaderText("Customer Updated");
            confirm.show();
        } catch (IllegalArgumentException | SQLException ex) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Error in updating customer." + "\n" + ex.getMessage());
            error.setHeaderText("Customer Update Error");
            error.show();
            System.out.println(ex.getMessage());
        }
    }
    /**
     * Loads test data for the customers table
     */
    public void loadTestData(){
        nameTextField.setText("Harvey Smith");
        addressTextField.setText("123 Hickory Street, Plymouth");
        postalCodeTextField.setText("78954");
        phoneTextField.setText("1-578-323-7893");
    }
}

