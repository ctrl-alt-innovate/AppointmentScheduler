package com.evanwahrmund.appointmentscheduler.daos;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.evanwahrmund.appointmentscheduler.models.Country;
import com.evanwahrmund.appointmentscheduler.models.Customer;
import com.evanwahrmund.appointmentscheduler.models.Division;
import com.evanwahrmund.appointmentscheduler.interfaces.CustomerDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Database implementation of CustomerDao
 */
public class CustomerDatabaseDao implements CustomerDao {
    /**
     * singleton instance
     */
    private static CustomerDatabaseDao INSTANCE;

    /**
     * Gets instance of CustomerDatabaseDao
     * @return singleton instace of CustomerDatabaseDao
     */
    public static CustomerDatabaseDao getInstance() {
       if (INSTANCE == null)
           INSTANCE = new CustomerDatabaseDao();
       return INSTANCE;
    }

    /**
     * Gets list of all Customers in database
     * @return ObservableList of all Customers
     */
   public ObservableList<Customer> getAllCustomers(){
       ObservableList<Customer> customers = FXCollections.observableArrayList();
        String selectSQL = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, f.Division_ID " +
               "FROM customers c INNER JOIN first_level_divisions f ON c.Division_ID = f.Division_ID;";
       try(var rs = DatabaseConnection.getConnection().prepareStatement(selectSQL).executeQuery()){
           while (rs.next()){
               int id = rs.getInt("Customer_ID");
               String name = rs.getString("Customer_Name");
               String address = rs.getString("Address");
               String postalCode = rs.getString("Postal_Code");
               String phone = rs.getString("Phone");
               Division div = ReadOnlyDatabaseDao.getInstance().getDivision(rs.getInt(6));
               Customer customer = new Customer(id, name, address, postalCode, phone, div);
               customers.add(customer);
           }
       }catch (SQLException ex){
            System.out.println("Error in retrieving all customers.");
       }
       return customers;
   }

    /**
     * Adds new Customer to the database
     * @param customer Customer to be added to database
     * @throws SQLException if any errors occur adding to the database
     */
    @Override
   public void createCustomer(Customer customer) throws SQLException {
       String insertSql = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Create_Date, " +
               "Created_By, Last_Update, Last_Updated_By, Division_ID)" +
               " VALUES(?,?,?,?,?,?,?,?,?);";
       try (var ps = DatabaseConnection.getConnection().prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)){
           ps.setString(1, customer.getName());
           ps.setString(2, customer.getAddress());
           ps.setString(3, customer.getPostalCode());
           ps.setString(4, customer.getPhone());
           ps.setTimestamp(5, Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())
                   .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
           ps.setString(6, "Application");
           ps.setTimestamp(7, Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())
                   .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
           ps.setString(8, "Application");
           ps.setInt(9, customer.getDivision().getId());
           ps.executeUpdate();
           try(var rs = ps.getGeneratedKeys()){
               if (rs.next()){
                  customer.setId(rs.getInt(1));

               }

           }
       }

   }

    /**
     * Updates given customer in the database
     * @param customer Customer to be updated
     * @throws SQLException if any errors occur updating Customer
     */
   public void updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ?, " +
                "Last_Update = ?, Last_Updated_By = ? WHERE Customer_ID = ?;";
        try (var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setInt(5, customer.getDivision().getId());
            ps.setTimestamp(6, Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
            ps.setString(7,"Application");
            ps.setInt(8, customer.getId());
            ps.executeUpdate();

        }
   }

    /**
     * Deletes given Customer from database
     * @param customer customer to be deleted
     * @throws SQLException if any errors occur while deleting Customer
     */
   public void deleteCustomer(Customer customer) throws SQLException{
       String deleteSQL = "DELETE FROM customers WHERE Customer_ID = ?;";
       try(var ps = DatabaseConnection.getConnection().prepareStatement(deleteSQL)) {
           ps.setInt(1, customer.getId());
           ps.executeUpdate();
       }
   }

    /**
     * Gets Customer with given id
     * @param id int id to search for
     * @return Customer with given id, null otherwise
     */
   public Customer getCustomer(int id){
        String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division, f.Division_ID, Country, c.Country_ID " +
                "FROM customers as customer INNER JOIN first_level_divisions as f ON customer.Division_ID = f.Division_ID " +
                "INNER JOIN countries as c ON f.COUNTRY_ID = c.Country_ID WHERE Customer_ID = ?;";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            try(var rs = ps.executeQuery()){
                if(rs.next()){
                    int cusId = rs.getInt("Customer_ID");
                    String name = rs.getString("Customer_Name");
                    String address = rs.getString("Address");
                    String postalCode = rs.getString("Postal_Code");
                    String phone = rs.getString("Phone");

                    Country country = new Country(rs.getString("Country"), rs.getInt("Country_ID"));
                    Division div = new Division(rs.getString("Division"), rs.getInt("Division_ID"), country);
                    Customer customer = new Customer(id, name, address, postalCode, phone, div);
                }
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
   }

}
