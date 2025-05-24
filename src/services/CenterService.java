package services;

import model.Center;
import utils.ConnectionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CenterService {

    // Create
    public boolean createCenter(Center center) {
        String sql = "INSERT INTO center (center_code, center_name, postal_address, phone_number, "
                   + "general_director, hr_manager, accounting_manager, union_secretary, logo, contact_email) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setCenterParameters(pstmt, center);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error creating center", e);
            return false;
        }
    }

    // Read All
    public List<Center> getAllCenters() {
        List<Center> centers = new ArrayList<>();
        String sql = "SELECT * FROM center";
        
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                centers.add(mapResultSetToCenter(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving centers", e);
        }
        return centers;
    }

    // Read Single
    public Center getCenterByCode(String centerCode) {
        String sql = "SELECT * FROM center WHERE center_code = ?";
        Center center = new Center();
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, centerCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    center = mapResultSetToCenter(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving center", e);
        }
        return center;
    }

    // Update
    public boolean updateCenter(Center center) {
        String sql = "UPDATE center SET "
                   + "center_name = ?, postal_address = ?, phone_number = ?, general_director = ?, "
                   + "hr_manager = ?, accounting_manager = ?, union_secretary = ?, logo = ?, contact_email = ? "
                   + "WHERE center_code = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setCenterParameters(pstmt, center);
            pstmt.setString(10, center.getCenterCode());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error updating center", e);
            return false;
        }
    }

    // Delete
    public boolean deleteCenter(String centerCode) {
        String sql = "DELETE FROM center WHERE center_code = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, centerCode);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error deleting center", e);
            return false;
        }
    }

    // Métodos auxiliares
    private void setCenterParameters(PreparedStatement pstmt, Center center) throws SQLException {
        pstmt.setString(1, center.getCenterCode());
        pstmt.setString(2, center.getCenterName());
        pstmt.setString(3, center.getPostalAddress());
        pstmt.setString(4, center.getPhoneNumber());
        pstmt.setString(5, center.getGeneralDirector());
        pstmt.setString(6, center.getHrManager());
        pstmt.setString(7, center.getAccountingManager());
        pstmt.setString(8, center.getUnionSecretary());
        pstmt.setBytes(9, center.getLogo());
        pstmt.setString(10, center.getContactEmail());
    }

    private Center mapResultSetToCenter(ResultSet rs) throws SQLException {
        Center center = new Center();
        center.setCenterCode(rs.getString("center_code"));
        center.setCenterName(rs.getString("center_name"));
        center.setPostalAddress(rs.getString("postal_address"));
        center.setPhoneNumber(rs.getString("phone_number"));
        center.setGeneralDirector(rs.getString("general_director"));
        center.setHrManager(rs.getString("hr_manager"));
        center.setAccountingManager(rs.getString("accounting_manager"));
        center.setUnionSecretary(rs.getString("union_secretary"));
        center.setLogo(rs.getBytes("logo"));
        center.setContactEmail(rs.getString("contact_email"));
        return center;
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace(); // En producción usar un logger
    }
}