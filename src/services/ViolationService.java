package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Violation;
import utils.ConnectionManager;

public class ViolationService implements EntityService<Violation>{

    // Create
    public boolean create(Violation violation) {
    	String sql = "INSERT INTO violation (violation_code, violation_type, violation_date, "
    	           + "location, description, deducted_points, is_paid, license_code) "
    	           + "VALUES (?, CAST(? AS violation_type), ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	System.out.println("--- Parámetros INSERT ---");
        	System.out.println("violation_code = [" + violation.getViolationCode() + "]");
        	System.out.println("violation_type = [" + violation.getViolationType() + "]");
        	System.out.println("violation_date = [" + violation.getDate() + "]");
        	System.out.println("location = [" + violation.getLocation() + "]");
        	System.out.println("description = [" + violation.getDescription() + "]");
        	System.out.println("deducted_points = [" + violation.getDeductedPoints() + "]");
        	System.out.println("is_paid = [" + violation.isPaid() + "]");
        	System.out.println("license_code = [" + violation.getLicenseCode() + "]");
            setViolationParameters(pstmt, violation);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error creating violation", e);
            return false;
        }
    }

    // Read All
    public List<Violation> getAll() {
        List<Violation> violations = new ArrayList<>();
        String sql = "SELECT * FROM violation";
        
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                violations.add(mapResultSetToViolation(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving violations", e);
        }
        return violations;
    }

    // Read Single
    public Violation getById(String violationCode) {
        String sql = "SELECT * FROM violation WHERE violation_code = ?";
        Violation violation = new Violation();
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, violationCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    violation = mapResultSetToViolation(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving violation", e);
        }
        return violation;
    }

    // Update
    public boolean update(Violation violation) {
    	String sql = "UPDATE violation SET "
    	           + "violation_type = CAST(? AS violation_type), violation_date = ?, location = ?, "
    	           + "description = ?, deducted_points = ?, is_paid = ?, license_code = ? "
    	           + "WHERE violation_code = ?";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setUpdateParameters(pstmt, violation);
            pstmt.setString(8, violation.getViolationCode());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error updating violation", e);
            return false;
        }
    }
    public int countUnpaidViolations() {
        String sql = "SELECT COUNT(*) FROM violation WHERE is_paid = false";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error counting unpaid violations", e);
        }
        return 0;
    }
    public int countPaidViolations() {
        String sql = "SELECT COUNT(*) FROM violation WHERE is_paid = true";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error counting paid violations", e);
        }
        return 0;
    }
    // Delete
    public boolean delete(String violationCode) {
        String sql = "DELETE FROM violation WHERE violation_code = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, violationCode);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error deleting violation", e);
            return false;
        }
    }

    // Métodos auxiliares
    private void setViolationParameters(PreparedStatement pstmt, Violation violation) throws SQLException {
    	System.out.println("deductedPoints = " + violation.getDeductedPoints());
    	System.out.println("VIOLATION TYPE QUE SE ENVÍA: [" + violation.getViolationType() + "]");
        pstmt.setString(1, violation.getViolationCode());
        pstmt.setString(2, violation.getViolationType());
        pstmt.setDate(3, new java.sql.Date(violation.getDate().getTime()));
        pstmt.setString(4, violation.getLocation());
        pstmt.setString(5, violation.getDescription());
        pstmt.setInt(6, violation.getDeductedPoints());
        pstmt.setBoolean(7, violation.isPaid());
        pstmt.setString(8, violation.getLicenseCode());
        System.out.println("--- Parámetros INSERT ---");
        System.out.println("violation_code = [" + violation.getViolationCode() + "]");
        System.out.println("violation_type = [" + violation.getViolationType() + "]");
        System.out.println("violation_date = [" + violation.getDate() + "]");
        System.out.println("location = [" + violation.getLocation() + "]");
        System.out.println("description = [" + violation.getDescription() + "]");
        System.out.println("deducted_points = [" + violation.getDeductedPoints() + "]");
        System.out.println("is_paid = [" + violation.isPaid() + "]");
        System.out.println("license_code = [" + violation.getLicenseCode() + "]");
    }

    private void setUpdateParameters(PreparedStatement pstmt, Violation violation) throws SQLException {
        pstmt.setString(1, violation.getViolationType());
        pstmt.setDate(2, new java.sql.Date(violation.getDate().getTime()));
        pstmt.setString(3, violation.getLocation());
        pstmt.setString(4, violation.getDescription());
        pstmt.setInt(5, violation.getDeductedPoints());
        pstmt.setBoolean(6, violation.isPaid());
        pstmt.setString(7, violation.getLicenseCode());
    }

    private Violation mapResultSetToViolation(ResultSet rs) throws SQLException {
        Violation violation = new Violation();
        violation.setViolationCode(rs.getString("violation_code"));
        violation.setViolationType(rs.getString("violation_type"));
        violation.setDate(rs.getDate("violation_date"));
        violation.setLocation(rs.getString("location"));
        violation.setDescription(rs.getString("description"));
        violation.setDeductedPoints(rs.getInt("deducted_points"));
        violation.setPaid(rs.getBoolean("is_paid"));
        violation.setLicenseCode(rs.getString("license_code"));
        return violation;
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }

    // Método adicional para multas por licencia
    public List<Violation> getViolationsByLicense(String licenseCode) {
        List<Violation> violations = new ArrayList<>();
        String sql = "SELECT * FROM violation WHERE license_code = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, licenseCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    violations.add(mapResultSetToViolation(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving violations by license", e);
        }
        return violations;
    }

    // Método para multas no pagadas
    public List<Violation> getUnpaidViolations() {
        List<Violation> violations = new ArrayList<>();
        String sql = "SELECT * FROM violation WHERE is_paid = false";
        
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                violations.add(mapResultSetToViolation(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving unpaid violations", e);
        }
        return violations;
    }
}