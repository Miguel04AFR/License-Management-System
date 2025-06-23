package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Driver;
import utils.ConnectionManager;

public class DriverService implements EntityService<Driver>{

    // Create (Incluye driver_id)
    public boolean create(Driver driver) {
    	String sql = "INSERT INTO driver (driver_id, first_name, last_name, birth_date, address, phone_number, email, license_status) "
    	           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?::license_status)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setDriverParameters(pstmt, driver);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error creating driver", e);
            return false;
        }
    }

    // Read All
    public List<Driver> getAll() {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM driver";
        
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                drivers.add(mapResultSetToDriver(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving drivers", e);
        }
        return drivers;
    }

    // Read Single
    public Driver getById(String driverId) {
        String sql = "SELECT * FROM driver WHERE driver_id = ?";
        Driver driver = new Driver();
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, driverId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    driver = mapResultSetToDriver(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving driver", e);
        }
        return driver;
    }

    // Update
    public boolean update(Driver driver) {
        String sql = "UPDATE driver SET "
                   + "first_name = ?, last_name = ?, birth_date = ?, address = ?, "
                   + "phone_number = ?, email = ?, license_status = CAST(? AS license_status) "
                   + "WHERE driver_id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

           
        	setUpdateParameters(pstmt,driver);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("UPDATE driver_id = '" + driver.getDriverId() + "', filas afectadas: " + affectedRows);
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete
    public boolean delete(String driverId) {
        String sql = "DELETE FROM driver WHERE driver_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, driverId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error deleting driver", e);
            return false;
        }
    }

    // Métodos auxiliares
    private void setDriverParameters(PreparedStatement pstmt, Driver driver) throws SQLException {
        pstmt.setString(1, driver.getDriverId());
        pstmt.setString(2, driver.getFirstName());
        pstmt.setString(3, driver.getLastName());
        pstmt.setDate(4, new java.sql.Date(driver.getBirthDate().getTime()));
        pstmt.setString(5, driver.getAddress());
        pstmt.setString(6, driver.getPhoneNumber());
        pstmt.setString(7, driver.getEmail());
        pstmt.setString(8, driver.getLicenseStatus());
    }

    private void setUpdateParameters(PreparedStatement pstmt, Driver driver) throws SQLException {
    	 pstmt.setString(1, driver.getFirstName());
         pstmt.setString(2, driver.getLastName());
         pstmt.setDate(3, new java.sql.Date(driver.getBirthDate().getTime()));
         pstmt.setString(4, driver.getAddress());
         pstmt.setString(5, driver.getPhoneNumber());
         pstmt.setString(6, driver.getEmail());
         pstmt.setString(7, driver.getLicenseStatus());
         pstmt.setString(8, driver.getDriverId());
    }

    private Driver mapResultSetToDriver(ResultSet rs) throws SQLException {
        Driver driver = new Driver();
        driver.setDriverId(rs.getString("driver_id"));  // Usa getString
        driver.setFirstName(rs.getString("first_name"));
        driver.setLastName(rs.getString("last_name"));
        driver.setBirthDate(rs.getDate("birth_date"));
        driver.setAddress(rs.getString("address"));
        driver.setPhoneNumber(rs.getString("phone_number"));
        driver.setEmail(rs.getString("email"));
        driver.setLicenseStatus(rs.getString("license_status"));
        return driver;
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }
}