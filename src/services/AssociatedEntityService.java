package services;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.AssociatedEntity;
import utils.ConnectionManager;

public class AssociatedEntityService implements EntityService<AssociatedEntity>{

    // Create
    public boolean create(AssociatedEntity entity) {
        String sql = "INSERT INTO associated_entity (entity_Code, entity_name, entity_type, address, "
                   + "phone_number, contact_email, director_name) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
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
    public List<AssociatedEntity> getAll() {
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
    public AssociatedEntity getById(String entityCode) {
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
    public boolean update(AssociatedEntity entity) {
        String sql = "UPDATE associated_entity SET "
                   + "entity_name = ?, entity_type = ?, address = ?, phone_number = ?, "
                   + "contact_email = ?, director_name = ? "
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
    public boolean delete(String entityCode) {
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
       
    }

    private void setUpdateParameters(PreparedStatement pstmt, AssociatedEntity entity) throws SQLException {
        pstmt.setString(1, entity.getEntityName());
        pstmt.setString(2, entity.getEntityType());
        pstmt.setString(3, entity.getAddress());
        pstmt.setString(4, entity.getPhoneNumber());
        pstmt.setString(5, entity.getContactEmail());
        pstmt.setString(6, entity.getDirectorName());
  
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