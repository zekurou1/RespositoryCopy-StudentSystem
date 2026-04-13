package dao;

import config.DBConnection;
import model.Enrollment;

import java.sql.*;
import java.util.Date;

/**
 * Enrollment Data Access Object (DAO)
 *
 * Responsibility: Handle all SQL queries for the enrollments table
 * Rule: ONLY this class should write SQL for enrollments
 * BE2 - DAO & CRUD Operations
 */
public class EnrollmentDAO {

	/**
	 * Enroll a student in a course
	 *
	 * @param studentId the student ID
	 * @param courseId the course ID
	 * @param term the term
	 * @param schoolYear the school year
	 * @param category the category
	 * @param dateAdmitted the date student was admitted
	 * @throws SQLException if database operation fails
	 */
	public void enrollStudent(int studentId, int courseId, String term,
							  String schoolYear, String category,
							  Date dateAdmitted) throws SQLException {
		String sql = "INSERT INTO enrollments (student_id, course_id, category, educational_attainment, date_admitted) " +
					 "VALUES (?, ?, ?, ?, ?)";
		
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setInt(1, studentId);
			pstmt.setInt(2, courseId);
			pstmt.setString(3, category);
			pstmt.setString(4, "Undergraduate");  // Default educational attainment
			pstmt.setDate(5, new java.sql.Date(dateAdmitted.getTime()));
			
			int rowsAffected = pstmt.executeUpdate();
			System.out.println("[EnrollmentDAO] Enrolled " + rowsAffected + " student(s)");
			
		} catch (SQLException e) {
			System.err.println("[EnrollmentDAO] Error enrolling student: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Add/update grade for an enrollment
	 *
	 * @param studentId the student ID
	 * @param courseId the course ID
	 * @param grade the grade (1.0 to 5.0)
	 * @throws SQLException if database operation fails
	 */
	public void addGrade(int studentId, int courseId, double grade) throws SQLException {
		String sql = "UPDATE enrollments SET grade = ?  WHERE student_id = ? AND course_id = ?";
		
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setDouble(1, grade);
			pstmt.setInt(2, studentId);
			pstmt.setInt(3, courseId);
			
			int rowsAffected = pstmt.executeUpdate();
			System.out.println("[EnrollmentDAO] Updated grade for " + rowsAffected + " enrollment(s)");
			
		} catch (SQLException e) {
			System.err.println("[EnrollmentDAO] Error updating grade: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Get enrollment by student and course ID
	 *
	 * @param studentId the student ID
	 * @param courseId the course ID
	 * @return Enrollment object or null if not found
	 * @throws SQLException if database operation fails
	 */
	public Enrollment getEnrollment(int studentId, int courseId) throws SQLException {
		String sql = "SELECT * FROM enrollments WHERE student_id = ? AND course_id = ?";
		Enrollment enrollment = null;
		
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setInt(1, studentId);
			pstmt.setInt(2, courseId);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				enrollment = new Enrollment(
					rs.getInt("enrollment_id"),
					rs.getInt("student_id"),
					rs.getInt("course_id"),
					rs.getDouble("grade"),
					rs.getDate("date_conferred"),
					rs.getString("category"),
					rs.getString("educational_attainment"),
					rs.getDate("date_admitted")
				);
			}
			
		} catch (SQLException e) {
			System.err.println("[EnrollmentDAO] Error retrieving enrollment: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		return enrollment;
	}
}
