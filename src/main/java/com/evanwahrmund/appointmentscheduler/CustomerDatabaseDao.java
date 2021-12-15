package com.evanwahrmund.appointmentscheduler;

import java.sql.SQLException;
import java.sql.PreparedStatement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomerDatabaseDao implements CustomerDao {

    private static CustomerDatabaseDao INSTANCE;
    public static CustomerDatabaseDao getInstance() {
       if (INSTANCE == null)
           INSTANCE = new CustomerDatabaseDao();
       return INSTANCE;
    }

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

   public boolean createCustomer(Customer customer){
       String insertSql = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Created_By, Last_Updated_By, Division_ID)" +
               " VALUES(?,?,?,?,?,?,?,?,?);";
       try (var ps = DatabaseConnection.getConnection().prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)){
           ps.setString(1, customer.getName());
           ps.setString(2, customer.getAddress());
           ps.setString(3, customer.getPostalCode());
           ps.setString(4, customer.getPhone());
           ps.setString(6, "Application");
           ps.setString(8, "Application");
           ps.setInt(9, customer.getDivision().getId());
           ps.executeUpdate();
           try(var rs = ps.getGeneratedKeys()){
               if (rs.next()){
                  customer.setId(rs.getInt(1));
                  return true;
               }
               System.out.println("Error: Customer id not set.");
           }
       } catch(SQLException ex){
            System.out.println("Error: Customer not created.");
       }
       return false;
   }

   public boolean updateCustomer(Customer customer){
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? " +
                "WHERE Customer_ID = ?;";
        try (var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getAddress());
            ps.setString(3, customer.getPostalCode());
            ps.setString(4, customer.getPhone());
            ps.setInt(5, customer.getDivision().getId());
            ps.setInt(6, customer.getId());
            ps.executeUpdate();
            return true;
        }catch (SQLException ex){
            System.out.println("Error: Customer with ID - " + customer.getId() + " not updated");
            return false;
        }
   }
   public boolean deleteCustomer(Customer customer){
       String deleteSQL = "DELETE FROM customers WHERE Customer_ID = ?;";
       try(var ps = DatabaseConnection.getConnection().prepareStatement(deleteSQL)){
           ps.setInt(1,customer.getId());
           ps.executeUpdate();
           return true;
       } catch(SQLException ex){
           System.out.println("Error: Customer with ID - " + customer.getId() + " not deleted");
           return false;
       }
   }
   //HAVE NOT CHECKED
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
