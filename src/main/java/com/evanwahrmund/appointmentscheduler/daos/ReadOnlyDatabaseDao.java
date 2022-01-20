package com.evanwahrmund.appointmentscheduler.daos;

import java.sql.SQLException;

import com.evanwahrmund.appointmentscheduler.interfaces.ReadyOnlyDao;
import com.evanwahrmund.appointmentscheduler.models.Countries;
import com.evanwahrmund.appointmentscheduler.models.Country;
import com.evanwahrmund.appointmentscheduler.models.Division;
import com.evanwahrmund.appointmentscheduler.models.Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Database implementation of ReadOnlyDao
 */
public class ReadOnlyDatabaseDao implements ReadyOnlyDao {
    /**
     * singleton instance
     */
    private static ReadOnlyDatabaseDao INSTANCE;

    /**
     * Gets instance of ReadOnlyDao
     * @return singleton instance of ReadOnlyDao
     */
    public static ReadOnlyDatabaseDao getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ReadOnlyDatabaseDao();
        return INSTANCE;
    }

    /**
     * Gets all Countries from database
     * @return ObservableList of Countries
     */
    public ObservableList<Country> getAllCountries() {
        ObservableList<Country> countries = FXCollections.observableArrayList();
        String sql = "SELECT Country, Country_ID FROM countries;";
        try (var rs = DatabaseConnection.getConnection().prepareStatement(sql).executeQuery()) {
            while (rs.next()) {
                countries.add(new Country(rs.getString("Country"), rs.getInt("Country_ID")));
            }
        } catch (SQLException ex) {
            System.out.println("Error in retrieving all countries.");
        }
        return countries;
    }

    /**
     * Gets all first level divisions from the database
     * @return ObservableList of all Divisions
     */
    public ObservableList<Division> getAllDivisions() {
        ObservableList<Division> divisions = FXCollections.observableArrayList();
        String sql = "SELECT Division, Division_ID, Country, c.Country_ID FROM first_level_divisions AS f " +
                "INNER JOIN countries AS c on f.COUNTRY_ID = c.Country_ID;";
        try (var rs = DatabaseConnection.getConnection().prepareStatement(sql).executeQuery()) {
            while (rs.next()) {
                divisions.add(new Division(rs.getString("Division"), rs.getInt("Division_ID"),
                                                    Countries.getCountry(rs.getInt(4))));
            }
        } catch (SQLException ex) {
            System.out.println("Error in retrieving all divisions.");
        }
        return divisions;
    }

    /**
     * Gets Divisions by given Country
     * @param country Country to find Divisions for
     * @return ObservableList of Divisions by given Country
     */
    public ObservableList<Division> listDivisions(Country country) {
        String sql = "SELECT Division, Division_ID, Country, c.Country_ID FROM first_level_divisions AS f " +
                "INNER JOIN countries AS c " +
                "ON f.COUNTRY_ID = c.Country_ID " +
                "WHERE Country = ?;";
        ObservableList<Division> divsByCountry = FXCollections.observableArrayList();
        try (var ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, country.getName());
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    divsByCountry.add(Divisions.getDivision(rs.getInt("Division_ID")));
                }
            }
        } catch (SQLException ex) {
            System.out.println("ERROR in listing countries.");
        }
        return divsByCountry;
    }

    /**
     * Gets Country with given id
     * @param id int id to search for
     * @return Country with given id, null otherwise
     */
    public Country getCountry(int id){
        String sql = "SELECT Country, Country_ID FROM countries WHERE Country_ID = ?;";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setInt(1,id);
            try(var rs = ps.executeQuery()){
                if (rs.next()){
                   Country country = Countries.getCountry(rs.getInt("Country_ID"));
                   return country;
                }
            }
        } catch(SQLException ex){
            System.out.println("Error: Could not get country.");
        }
        return null;
    }

    /**
     * Gets Division with selected id
     * @param id int id to search for
     * @return Division with selected id, otherwise null
     */
    public Division getDivision(int id){
        String sql = "SELECT Division, Division_ID FROM first_level_divisions WHERE Division_ID = ?";
        try(var ps = DatabaseConnection.getConnection().prepareStatement(sql)){
            ps.setInt(1, id);
            try(var rs = ps.executeQuery()){
                if(rs.next()){
                   Division div = Divisions.getDivision(rs.getInt("Division_ID"));
                   return div;
                }
            }
        }catch(SQLException ex){
           System.out.println("Error: Could not get division.");
        }
        return null;
    }


}