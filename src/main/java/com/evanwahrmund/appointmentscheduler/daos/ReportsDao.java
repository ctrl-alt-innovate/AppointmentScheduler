package com.evanwahrmund.appointmentscheduler.daos;

import java.sql.SQLException;

import com.evanwahrmund.appointmentscheduler.models.Appointment;
import com.evanwahrmund.appointmentscheduler.models.Appointments;
import com.evanwahrmund.appointmentscheduler.models.Contact;
import com.evanwahrmund.appointmentscheduler.util.ReportVal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReportsDao {

    private static ReportsDao INSTANCE;
    public static ReportsDao getInstance(){
        if(INSTANCE == null)
            INSTANCE = new ReportsDao();
        return INSTANCE;
    }
    public ObservableList<ReportVal> getAppsByTypeAndMonth(){
        ObservableList<ReportVal>typeAndMonthToCount = FXCollections.observableArrayList();
        String sql = "SELECT MONTHNAME(Start), Type, COUNT(*) FROM appointments GROUP BY MONTHNAME(Start), Type;";
        try(var rs = DatabaseConnection.getConnection().prepareStatement(sql).executeQuery()){
            while(rs.next()){
                ReportVal reportVal = new ReportVal<>(rs.getString(1), rs.getString(2), rs.getInt(3));
                typeAndMonthToCount.add(reportVal);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return typeAndMonthToCount;
    }

    public ObservableList<Appointment> getAppsByContact(Contact contact){
        ObservableList<Appointment> appsByContact = FXCollections.observableArrayList();
        for(Appointment app: Appointments.getAppointments()){
            if (app.getContact().getId() == contact.getId())
                appsByContact.add(app);
        }
        return appsByContact;
    }
    public ObservableList<ReportVal> getAppsByCountry(){
        ObservableList<ReportVal> appsByCountry = FXCollections.observableArrayList();
        String sql = "SELECT Country, COUNT(Country) FROM countries c INNER JOIN first_level_divisions fld " +
                        "ON c.Country_ID = fld.COUNTRY_ID INNER JOIN customers c2 on fld.Division_ID = c2.Division_ID " +
                        "INNER JOIN appointments a on c2.Customer_ID = a.Customer_ID GROUP BY Country;";
        try(var rs = DatabaseConnection.getConnection().prepareStatement(sql).executeQuery()){
            while(rs.next()){
                ReportVal reportVal = new ReportVal(rs.getString("Country"),
                                    rs.getInt("Count(Country)"));
                        appsByCountry.add(reportVal);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return appsByCountry;
    }
    public ObservableList<ReportVal> getAppsByFirstLevelDiv(){
        ObservableList<ReportVal> appsByDiv = FXCollections.observableArrayList();
        String sql = "SELECT Division, COUNT(Division) FROM first_level_divisions as f INNER JOIN customers as c " +
                        "ON f.Division_ID = c.Division_ID INNER JOIN appointments as a ON a.Customer_ID = c.Customer_ID " +
                        "GROUP BY Division;";
        try(var rs = DatabaseConnection.getConnection().prepareStatement(sql).executeQuery()){
            while(rs.next()){
                ReportVal reportVal = new ReportVal<>(rs.getString("Division"),
                        rs.getInt("Count(Division)"));
                appsByDiv.add(reportVal);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appsByDiv;
    }
    public ObservableList<ReportVal> getCusByCountry(){
        ObservableList<ReportVal> cusByCountry = FXCollections.observableArrayList();
        String sql ="SELECT Country, COUNT(Country) FROM countries c INNER JOIN first_level_divisions fld " +
                "ON c.Country_ID = fld.COUNTRY_ID INNER JOIN customers cus ON fld.Division_ID = cus.Division_ID " +
                "GROUP BY Country;";
        try(var rs = DatabaseConnection.getConnection().prepareStatement(sql).executeQuery()){
            while (rs.next()){
                ReportVal reportVal = new ReportVal<>(rs.getString("Country"),
                        rs.getInt("COUNT(Country)"));
                cusByCountry.add(reportVal);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return cusByCountry;
    }
    public ObservableList<ReportVal> getCusByFirstLevelDiv(){
        ObservableList<ReportVal> cusByDiv = FXCollections.observableArrayList();
        String sql = "SELECT Division, COUNT(Division) FROM first_level_divisions f INNER JOIN customers c " +
                "ON c.Division_ID = f.Division_ID GROUP BY Division;";
        try(var rs = DatabaseConnection.getConnection().prepareStatement(sql).executeQuery()){
            while(rs.next()){
                ReportVal reportVal = new ReportVal<>(rs.getString("Division"),
                        rs.getInt("COUNT(Division)"));
                cusByDiv.add(reportVal);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return cusByDiv;
    }
}
