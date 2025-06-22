package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Exam;
import utils.ConnectionManager;

public class ExamService implements EntityService<Exam> {

	public boolean create(Exam exam) {
	    String sql = "INSERT INTO exam (exam_code, exam_date, examiner_name, entity_code, driver_id, result, exam_type, vehicle_category) "
	               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    try (Connection conn = ConnectionManager.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        setExamParameters(pstmt, exam);
	        return pstmt.executeUpdate() > 0;

	    } catch (SQLException e) {
	        throw new RuntimeException(e.getMessage(), e);
	    }
	}
    // Read All
    public List<Exam> getAll() {
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
    public Exam getById(String examCode) {
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
    public boolean update(Exam exam) {
        String sql = "UPDATE exam SET "
                   + "exam_type = ?, exam_date = ?, result = ?, examiner_name = ?, "
                   + "entity_code = ?, driver_id = ?, vehicle_category = ? "
                   + "WHERE exam_code = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setUpdateParameters(pstmt, exam);
            pstmt.setString(8, exam.getExamCode());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            handleSQLException("Error updating exam", e);
            return false;
        }
    }

    // Delete
    public boolean delete(String examCode) {
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

    private void setExamParameters(PreparedStatement pstmt, Exam exam) throws SQLException {
        pstmt.setString(1, exam.getExamCode());
        pstmt.setDate(2, new java.sql.Date(exam.getExamDate().getTime()));
        pstmt.setString(3, exam.getExaminerName());
        pstmt.setString(4, exam.getEntityCode());
        pstmt.setString(5, exam.getDriverId());
        pstmt.setString(6, exam.getResult()); // No uses setObject ni Types.OTHER aquí
        pstmt.setObject(7, exam.getExamType(), java.sql.Types.OTHER); // Enum de Postgres
        pstmt.setObject(8, exam.getVehicleCategory(), java.sql.Types.OTHER); // Enum de Postgres
    }

    private void setUpdateParameters(PreparedStatement pstmt, Exam exam) throws SQLException {
        pstmt.setObject(1, exam.getExamType(), java.sql.Types.OTHER); // ENUM exam_type
        pstmt.setDate(2, new java.sql.Date(exam.getExamDate().getTime()));
        pstmt.setObject(3, exam.getResult(), java.sql.Types.OTHER);   // <-- ENUM exam_result
        pstmt.setString(4, exam.getExaminerName());
        pstmt.setString(5, exam.getEntityCode());
        pstmt.setString(6, exam.getDriverId());
        pstmt.setObject(7, exam.getVehicleCategory(), java.sql.Types.OTHER); // New field added
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
        exam.setVehicleCategory(rs.getString("vehicle_category")); // New field added
        return exam;
    }

    private void handleSQLException(String message, SQLException e) {
        System.err.println(message + ": " + e.getMessage());
        e.printStackTrace();
    }

    // Additional method for exams by driver
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
    public int countMedicalExams() {
        String sql = "SELECT COUNT(*) FROM exam WHERE exam_type = 'Medical'";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error counting medical exams", e);
        }
        return 0;
    }
    public int countDriversWithoutMedicalExam() {
        String sql = "SELECT COUNT(*) FROM driver d WHERE NOT EXISTS " +
                     "(SELECT 1 FROM exam e WHERE e.driver_id = d.driver_id AND e.exam_type = 'Medical')";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error counting drivers without medical exam", e);
        }
        return 0;
    }
    public int countTheoryExams() {
        String sql = "SELECT COUNT(*) FROM exam WHERE exam_type = 'Theory'";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error counting theory exams", e);
        }
        return 0;
    }
    public int countPracticalExams() {
        String sql = "SELECT COUNT(*) FROM exam WHERE exam_type = 'Practical'";
        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException("Error counting practical exams", e);
        }
        return 0;
    }
}