package services;

import model.Exam;
import utils.ConnectionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExamService {

    // Create
    public boolean createExam(Exam exam) {
        String sql = "INSERT INTO exam (exam_code, exam_type, exam_date, result, examiner_name, entity_code, driver_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setExamParameters(pstmt, exam);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error creating exam", e);
            return false;
        }
    }

    // Read All
    public List<Exam> getAllExams() {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM exam";
        
        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                exams.add(mapResultSetToExam(rs));
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving exams", e);
        }
        return exams;
    }

    // Read Single
    public Exam getExamByCode(String examCode) {
        String sql = "SELECT * FROM exam WHERE exam_code = ?";
        Exam exam = new Exam();
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, examCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    exam = mapResultSetToExam(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving exam", e);
        }
        return exam;
    }

    // Update
    public boolean updateExam(Exam exam) {
        String sql = "UPDATE exam SET "
                   + "exam_type = ?, exam_date = ?, result = ?, examiner_name = ?, "
                   + "entity_code = ?, driver_id = ? "
                   + "WHERE exam_code = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            setUpdateParameters(pstmt, exam);
            pstmt.setString(7, exam.getExamCode());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error updating exam", e);
            return false;
        }
    }

    // Delete
    public boolean deleteExam(String examCode) {
        String sql = "DELETE FROM exam WHERE exam_code = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, examCode);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            handleSQLException("Error deleting exam", e);
            return false;
        }
    }

    // Métodos auxiliares
    private void setExamParameters(PreparedStatement pstmt, Exam exam) throws SQLException {
        pstmt.setString(1, exam.getExamCode());
        pstmt.setString(2, exam.getExamType());
        pstmt.setDate(3, new java.sql.Date(exam.getExamDate().getTime()));
        pstmt.setString(4, exam.getResult());
        pstmt.setString(5, exam.getExaminerName());
        pstmt.setString(6, exam.getEntityCode());
        pstmt.setString(7, exam.getDriverId());
    }

    private void setUpdateParameters(PreparedStatement pstmt, Exam exam) throws SQLException {
        pstmt.setString(1, exam.getExamType());
        pstmt.setDate(2, new java.sql.Date(exam.getExamDate().getTime()));
        pstmt.setString(3, exam.getResult());
        pstmt.setString(4, exam.getExaminerName());
        pstmt.setString(5, exam.getEntityCode());
        pstmt.setString(6, exam.getDriverId());
    }

    private Exam mapResultSetToExam(ResultSet rs) throws SQLException {
        Exam exam = new Exam();
        exam.setExamCode(rs.getString("exam_code"));
        exam.setExamType(rs.getString("exam_type"));
        exam.setExamDate(rs.getDate("exam_date"));
        exam.setResult(rs.getString("result"));
        exam.setExaminerName(rs.getString("examiner_name"));
        exam.setEntityCode(rs.getString("entity_code"));
        exam.setDriverId(rs.getString("driver_id"));
        return exam;
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }

    // Método adicional para exámenes por conductor
    public List<Exam> getExamsByDriver(String driverId) {
        List<Exam> exams = new ArrayList<>();
        String sql = "SELECT * FROM exam WHERE driver_id = ?";
        
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, driverId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    exams.add(mapResultSetToExam(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException("Error retrieving exams by driver", e);
        }
        return exams;
    }
}