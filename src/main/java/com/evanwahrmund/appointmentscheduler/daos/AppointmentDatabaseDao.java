package com.evanwahrmund.appointmentscheduler.daos;



import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.evanwahrmund.appointmentscheduler.interfaces.AppointmentDao;
import com.evanwahrmund.appointmentscheduler.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Database implementation of AppointmentDao
 */
public class AppointmentDatabaseDao implements AppointmentDao {
    /**
     * singleton instance
     */
    private static AppointmentDatabaseDao INSTANCE;

    /**
     * Gets instance of AppointmentDatabaseDao
     * @return singleton instance of AppointmentDatabaseDao
     */
    public static AppointmentDatabaseDao getInstance(){
        if (INSTANCE == null)
            INSTANCE = new AppointmentDatabaseDao();
        return INSTANCE;
    }

    /**
     * Gets all Appointments from database
     * @return ObservableList of all Appointments from database
     */
    @Override
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
                ZonedDateTime start = ZonedDateTime.of(rs.getTimestamp(6).toLocalDateTime(), ZoneId.of("UTC-00:00"));
                ZonedDateTime end = ZonedDateTime.of(rs.getTimestamp(7).toLocalDateTime(), ZoneId.of("UTC-00:00"));
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

    /**
     * Gets Appointment with given id
     * @param id int id to search for
     * @return Appointment with given id, null otherwise
     */
    @Override
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
                    ZonedDateTime start = ZonedDateTime.of(rs.getTimestamp(6).toLocalDateTime(), ZoneId.of("UTC-00:00"));
                    ZonedDateTime end = ZonedDateTime.of(rs.getTimestamp(7).toLocalDateTime(), ZoneId.of("UTC-00:00"));
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

    /**
     * Adds new Appointment to database
     * @param appointment Appointment to be added
     * @throws SQLException if any errors occur while adding to database
     */
    @Override
    public void createAppointment(Appointment appointment) throws SQLException{
        String sql = "INSERT INTO appointments(Title, Description, Location, Type, Start, End, Create_Date, Created_By," +
                "Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getStartDateTime().toLocalDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getEndDateTime().toLocalDateTime()));
            ps.setTimestamp(7, Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
            ps.setString(8, "Application");
            ps.setTimestamp(9, Timestamp.valueOf(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
            ps.setString(10, "Application");
            ps.setInt(11, appointment.getCustomer().getId());
            ps.setInt(12, appointment.getUser().getUserId());
            ps.setInt(13, appointment.getContact().getId());
            ps.executeUpdate();
            try(var rs = ps.getGeneratedKeys()){
                if (rs.next()){
                    appointment.setId(rs.getInt(1));
                    System.out.println("Appointment added");
                    return;
                }
            }
        }
    }

    /**
     * Updates given Appointment in the database
     * @param appointment Appointment to be updated
     * @throws SQLException if any erros occur while updating
     */
    @Override
    public void updateAppointment(Appointment appointment) throws SQLException{
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                        "Customer_ID = ?, User_ID = ?, Contact_ID = ?, Last_Update = ?, Last_Updated_By = ? WHERE Appointment_ID = ?;";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setString(1, appointment.getTitle());
            ps.setString(2, appointment.getDescription());
            ps.setString(3, appointment.getLocation());
            ps.setString(4, appointment.getType());
            ps.setTimestamp(5, Timestamp.valueOf(appointment.getStartDateTime().toLocalDateTime()));
            ps.setTimestamp(6, Timestamp.valueOf(appointment.getEndDateTime().toLocalDateTime()));
            ps.setInt(7, appointment.getCustomer().getId());
            ps.setInt(8, appointment.getUser().getUserId());
            ps.setInt(9, appointment.getContact().getId());
            ps.setTimestamp(10, Timestamp.valueOf(
                    ZonedDateTime.now(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime()));
            ps.setString(11, "Application");
            ps.setInt(12, appointment.getId());
            ps.executeUpdate();
            //return true;
        } /*catch( SQLException ex){
            System.out.println("Error: Appointment with ID - " + appointment.getId() + " not updated.");
        }
        return false;*/
    }

    /**
     * Deletes given Appointment from database
     * @param appointment Appointment to be deleted
     * @throws SQLException if any errors occur while deleting
     */
    @Override
    public void deleteAppointment(Appointment appointment) throws SQLException{
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?;";
        try (var ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, appointment.getId());
            ps.executeUpdate();
            //return true;
        } /*catch (SQLException ex){
            System.out.println("Error: Appointment with ID - " + appointment.getId() + " not deleted.");

        return false;*/
    }

    /**
     * Gets Appointments by month and year
     * @param month int of month
     * @param year int of year
     * @return ObservableList of Apopintments
     */
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

    /**
     * Gets Apointments by Week
     * @param start String start day
     * @param end String end day
     * @return ObservableList of Appointments
     */
    public ObservableList<Appointment> getAppsByWeek(String start, String end){
        ObservableList<Appointment> appsByWeek = FXCollections.observableArrayList();
        String sql = "SELECT Appointment_ID FROM appointments WHERE DATE(start) BETWEEN ? AND ?;";
        try( var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setString(1, start);
            ps.setString(2, end);
            try( var rs = ps.executeQuery()){
                while(rs.next()){
                    appsByWeek.add(Appointments.getAppointment(rs.getInt(1)));
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return appsByWeek;
    }

}
