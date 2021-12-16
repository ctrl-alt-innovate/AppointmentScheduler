package com.evanwahrmund.appointmentscheduler;



import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AppointmentDatabaseDao implements AppointmentDao {

    private static AppointmentDatabaseDao INSTANCE;
    public static AppointmentDatabaseDao getInstance(){
        if (INSTANCE == null)
            INSTANCE = new AppointmentDatabaseDao();
        return INSTANCE;
    }
    public ObservableList<Appointment> getAllAppointments(){
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, " +
                "Contact_ID FROM appointments ORDER BY Start;";
        try (var rs = DatabaseConnection.getConnection().prepareStatement(sql).executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String description = rs.getString(3);
                String location = rs.getString(4);
                String type = rs.getString(5);
                LocalDateTime start = rs.getTimestamp(6).toLocalDateTime();
                LocalDateTime end = rs.getTimestamp(7).toLocalDateTime();
                Customer customer = Customers.getCustomer(rs.getInt(8));
                User user = Users.getUser(rs.getInt(9));
                Contact contact = Contacts.getContact(rs.getInt(10));
                Appointment appointment = new Appointment(id, title, description, location, type, start, end,
                        customer, user, contact);
                appointments.add(appointment);
            }
        } catch (SQLException ex) {
            System.out.println("Error in retrieving all appointments.");
        }
        return appointments;
    }
    //HAVE NOT CHECKED
    public Appointment getAppointment(int id){
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, User_ID, Contact_ID, Customer_ID " +
                "FROM appointments WHERE User_ID = ?;";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            try(var rs = ps.executeQuery()) {
                if (rs.next()) {
                    int appointmentId = rs.getInt(1);
                    String title = rs.getString(2);
                    String description = rs.getString(3);
                    String location = rs.getString(4);
                    String type = rs.getString(5);
                    LocalDateTime start = rs.getTimestamp(6).toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp(7).toLocalDateTime();
                    User user = UserDatabaseDao.getInstance().getUser(rs.getInt(8));
                    Contact contact = ContactDatabaseDao.getInstance().getContact(rs.getInt(9));
                    Customer customer = CustomerDatabaseDao.getInstance().getCustomer(rs.getInt(10));
                    Appointment appointment = new Appointment(appointmentId, title, description, location, type, start, end,
                            customer, user, contact );


                }
            }
        }catch (SQLException ex){
            //ent
        }
        return null;
    }

    public boolean createAppointment(Appointment appointment){
        String sql = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Created_By, Last_Updated_By, " +
                        "Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getStartDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getEndDateTime()));
            ps.setString(7, "Application");
            ps.setString(8, "Application");
            ps.setInt(9, appointment.getCustomer().getId());
            ps.setInt(10, appointment.getUser().getUserId());
            ps.setInt(11, appointment.getContact().getId());
            ps.executeUpdate();
            try(var rs = ps.getGeneratedKeys()){
                if (rs.next()){
                    appointment.setId(rs.getInt("Appointment_ID"));
                    return true;
                }
                System.out.println("Error: Customer id not set.");
            }
        } catch(SQLException ex){
            System.out.println("Error in adding appointment.");
        }
        return false;
    }
    public boolean updateAppointment(Appointment appointment){
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                        "Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?;";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getStartDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getEndDateTime()));
            ps.setInt(7, appointment.getCustomer().getId());
            ps.setInt(8, appointment.getUser().getUserId());
            ps.setInt(9, appointment.getContact().getId());
            ps.setInt(10, appointment.getId());
            ps.executeUpdate();
            return true;
        } catch( SQLException ex){
            System.out.println("Error: Appointment with ID - " + appointment.getId() + " not updated.");
        }
        return false;
    }
    public boolean deleteAppointment(Appointment appointment){
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?;";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setInt(1, appointment.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex){
            System.out.println("Error: Appointment with ID - " + appointment.getId() + " not deleted.");
        }
        return false;
    }
    public ObservableList<Appointment> getFilteredAppointments(LocalDate start, LocalDate end){
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, " +
                "Contact_ID FROM appointments WHERE (Start BETWEEN ? AND ?) AND (End BETWEEN ? AND ?);";
        ObservableList<Appointment> filteredApps = FXCollections.observableArrayList();
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setDate(1, Date.valueOf(start));
            ps.setDate(2, Date.valueOf(end));
            ps.setDate(3, Date.valueOf(start));
            ps.setDate(4, Date.valueOf(end));
            try(var rs = ps.executeQuery()){
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String title = rs.getString(2);
                    String description = rs.getString(3);
                    String location = rs.getString(4);
                    String type = rs.getString(5);
                    LocalDateTime startTime = rs.getTimestamp(6).toLocalDateTime();
                    LocalDateTime endTime = rs.getTimestamp(7).toLocalDateTime();
                    Customer customer = CustomerDatabaseDao.getInstance().getCustomer(rs.getInt(8));
                    User user = UserDatabaseDao.getInstance().getUser(rs.getInt(9));
                    Contact contact = ContactDatabaseDao.getInstance().getContact(rs.getInt(10));
                    Appointment appointment = new Appointment(id, title, description, location, type, startTime, endTime,
                            customer, user, contact );
                    filteredApps.add(appointment);

                }
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return filteredApps;
    }
    public void updateAppTime(Appointment app){
        String sql = "UPDATE appointments SET Start = ?, End = ? WHERE Appointment_ID = ?;";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setTimestamp(1, Timestamp.valueOf(app.getStartDateTime()));
            ps.setTimestamp(2,Timestamp.valueOf(app.getEndDateTime()));
            ps.setInt(3, app.getId());
            ps.executeUpdate();
            /* --Check and Update this
            for(Appointment appointment: appointments){
                if(appointment.getId() == app.getId())
                    appointments.set(appointments.indexOf(appointment), app);
            }*/

        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    public ObservableList<Appointment> getAppsByMonth(int month, int year ){
        ObservableList<Appointment> appsByMonth = FXCollections.observableArrayList();
        String sql = "SELECT Appointment_ID FROM appointments WHERE MONTH(Start) = ? and YEAR(start) = ?;";
        try (var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setInt(1, month);
            ps.setInt(2, year);
            try(var rs = ps.executeQuery()){
                while(rs.next()){
                    appsByMonth.add(Appointments.getAppointment(rs.getInt(1)));
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return appsByMonth;
    }

    public ObservableList<Appointment> getAppsByWeek(LocalDate start, LocalDate end){
        ObservableList<Appointment> appsByWeek = FXCollections.observableArrayList();
        String sql = "SELECT Appointment"
        try( var )
    }

}
