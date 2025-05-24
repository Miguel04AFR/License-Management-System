package services;


import model.AssociatedEntity;
import utils.ConnectionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssociatedEntityService {

    // Create
    public boolean createAssociatedEntity(AssociatedEntity entity) {
        String sql = "INSERT INTO associated_entity (entity_code, entity_name, entity_type, address, "
                   + "phone_number, contact_email, director_name, center_code) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setEntityParameters(pstmt, entity);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error creating associated entity", e);
            return false;
        }
    }

    // Read All
    public List<AssociatedEntity> getAllAssociatedEntities() {
        List<AssociatedEntity> entities = new ArrayList<>();
        String sql = "SELECT * FROM associated_entity";
        
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving entities", e);
        }
        return entities;
    }

    // Read Single
    public AssociatedEntity getEntityByCode(String entityCode) {
        String sql = "SELECT * FROM associated_entity WHERE entity_code = ?";
        AssociatedEntity entity = new AssociatedEntity();
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entityCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    entity = mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving entity", e);
        }
        return entity;
    }

    // Update
    public boolean updateAssociatedEntity(AssociatedEntity entity) {
        String sql = "UPDATE associated_entity SET "
                   + "entity_name = ?, entity_type = ?, address = ?, phone_number = ?, "
                   + "contact_email = ?, director_name = ?, center_code = ? "
                   + "WHERE entity_code = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setUpdateParameters(pstmt, entity);
            pstmt.setString(8, entity.getEntityCode());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error updating entity", e);
            return false;
        }
    }

    // Delete
    public boolean deleteAssociatedEntity(String entityCode) {
        String sql = "DELETE FROM associated_entity WHERE entity_code = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, entityCode);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error deleting entity", e);
            return false;
        }
    }

    // Métodos auxiliares
    private void setEntityParameters(PreparedStatement pstmt, AssociatedEntity entity) throws SQLException {
        pstmt.setString(1, entity.getEntityCode());
        pstmt.setString(2, entity.getEntityName());
        pstmt.setString(3, entity.getEntityType());
        pstmt.setString(4, entity.getAddress());
        pstmt.setString(5, entity.getPhoneNumber());
        pstmt.setString(6, entity.getContactEmail());
        pstmt.setString(7, entity.getDirectorName());
        pstmt.setString(8, entity.getCenterCode());
    }

    private void setUpdateParameters(PreparedStatement pstmt, AssociatedEntity entity) throws SQLException {
        pstmt.setString(1, entity.getEntityName());
        pstmt.setString(2, entity.getEntityType());
        pstmt.setString(3, entity.getAddress());
        pstmt.setString(4, entity.getPhoneNumber());
        pstmt.setString(5, entity.getContactEmail());
        pstmt.setString(6, entity.getDirectorName());
        pstmt.setString(7, entity.getCenterCode());
    }

    private AssociatedEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        AssociatedEntity entity = new AssociatedEntity();
        entity.setEntityCode(rs.getString("entity_code"));
        entity.setEntityName(rs.getString("entity_name"));
        entity.setEntityType(rs.getString("entity_type"));
        entity.setAddress(rs.getString("address"));
        entity.setPhoneNumber(rs.getString("phone_number"));
        entity.setContactEmail(rs.getString("contact_email"));
        entity.setDirectorName(rs.getString("director_name"));
        entity.setCenterCode(rs.getString("center_code"));
        return entity;
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }

    // Método adicional para obtener entidades por centro
    public List<AssociatedEntity> getEntitiesByCenter(String centerCode) {
        List<AssociatedEntity> entities = new ArrayList<>();
        String sql = "SELECT * FROM associated_entity WHERE center_code = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, centerCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entities.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving entities by center", e);
        }
        return entities;
    }
}