package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.License;
import utils.ConnectionManager;

public class LicenseService implements EntityService<License> {

    // Create
    public boolean create(License license)  {
        String sql = "INSERT INTO license (license_code, license_type, issue_date, expiration_date, "
                   + "vehicle_category, restrictions, is_renewed, driver_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setLicenseParameters(pstmt, license);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error creating license", e);
            return false;
        }
    }

    // Read All
    public List<License> getAll() {
        List<License> licenses = new ArrayList<>();
        String sql = "SELECT * FROM license";
        
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                licenses.add(mapResultSetToLicense(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving licenses", e);
        }
        return licenses;
    }

    // Read Single
    public License getById(String licenseCode) {
        String sql = "SELECT * FROM license WHERE license_code = ?";
        License license = new License();
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, licenseCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    license = mapResultSetToLicense(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving license", e);
        }
        return license;
    }

    // Update
    public boolean update(License license) {
        String sql = "UPDATE license SET "
                   + "license_type = ?, issue_date = ?, expiration_date = ?, "
                   + "vehicle_category = ?, restrictions = ?, is_renewed = ?, driver_id = ? "
                   + "WHERE license_code = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setUpdateParameters(pstmt, license);
            pstmt.setString(8, license.getLicenseCode());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error updating license", e);
            return false;
        }
    }

    // Delete
    public boolean delete(String licenseCode) {
        String sql = "DELETE FROM license WHERE license_code = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, licenseCode);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error deleting license", e);
            return false;
        }
    }

    // Métodos auxiliares
    private void setLicenseParameters(PreparedStatement pstmt, License license) throws SQLException {
        pstmt.setString(1, license.getLicenseCode());
        pstmt.setString(2, license.getLicenseType());
        pstmt.setDate(3, new java.sql.Date(license.getIssueDate().getTime()));
        pstmt.setDate(4, new java.sql.Date(license.getExpirationDate().getTime()));
        pstmt.setString(5, license.getVehicleCategory());
        pstmt.setString(6, license.getRestrictions());
        pstmt.setBoolean(7, license.isRenewed());
        pstmt.setString(8, license.getDriverId());
    }

    private void setUpdateParameters(PreparedStatement pstmt, License license) throws SQLException {
        pstmt.setString(1, license.getLicenseType());
        pstmt.setDate(2, new java.sql.Date(license.getIssueDate().getTime()));
        pstmt.setDate(3, new java.sql.Date(license.getExpirationDate().getTime()));
        pstmt.setString(4, license.getVehicleCategory());
        pstmt.setString(5, license.getRestrictions());
        pstmt.setBoolean(6, license.isRenewed());
        pstmt.setString(7, license.getDriverId());
    }

    private License mapResultSetToLicense(ResultSet rs) throws SQLException {
        License license = new License();
        license.setLicenseCode(rs.getString("license_code"));
        license.setLicenseType(rs.getString("license_type"));
        license.setIssueDate(rs.getDate("issue_date"));
        license.setExpirationDate(rs.getDate("expiration_date"));
        license.setVehicleCategory(rs.getString("vehicle_category"));
        license.setRestrictions(rs.getString("restrictions"));
        license.setRenewed(rs.getBoolean("is_renewed"));
        license.setDriverId(rs.getString("driver_id"));
        return license;
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }

    // Método adicional para licencias por conductor
    public List<License> getLicensesByDriver(String driverId) {
        List<License> licenses = new ArrayList<>();
        String sql = "SELECT * FROM license WHERE driver_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, driverId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    licenses.add(mapResultSetToLicense(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving licenses by driver", e);
        }
        return licenses;
    }
    public int countActiveLicenses() {
        String sql = "SELECT COUNT(*) FROM license l JOIN driver d ON l.driver_id = d.driver_id WHERE d.license_status = 'Active'";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error counting active licenses", e);
        }
        return 0;
    }
    public int countInactiveLicenses() {
        String sql = "SELECT COUNT(*) FROM license l JOIN driver d ON l.driver_id = d.driver_id WHERE d.license_status = 'Expired'";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error counting inactive licenses", e);
        }
        return 0;
    }
    public int countSoonToExpireLicenses() {
        String sql = "SELECT COUNT(*) FROM license " +
                     "WHERE is_renewed = false " +
                     "AND expiration_date > CURRENT_DATE " +
                     "AND expiration_date <= CURRENT_DATE + INTERVAL '1 month'";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error counting soon-to-expire licenses", e);
        }
        return 0;
    }
    public int countRenewedLicenses() {
        String sql = "SELECT COUNT(*) FROM license WHERE is_renewed = true";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error counting renewed licenses", e);
        }
        return 0;
    }
    public int countSuspendedLicenses() {
        String sql = "SELECT COUNT(*) FROM license l JOIN driver d ON l.driver_id = d.driver_id WHERE d.license_status = 'Suspended'";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error counting suspended licenses", e);
        }
        return 0;
    }
    // Contar licencias en renovación pendiente
    public int countPendingRenewals() {
        String sql = "SELECT COUNT(*) FROM license WHERE is_renewed = false AND issue_date <= (CURRENT_DATE - INTERVAL '10 years')";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error counting pending renewals", e);
        }
        return 0;
    }

    // Método para licencias próximas a expirar
    public List<License> getExpiringLicenses(int daysThreshold) {
        List<License> licenses = new ArrayList<>();
        String sql = "SELECT * FROM license WHERE expiration_date BETWEEN CURRENT_DATE AND CURRENT_DATE + ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, daysThreshold);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    licenses.add(mapResultSetToLicense(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving expiring licenses", e);
        }
        return licenses;
    }
}