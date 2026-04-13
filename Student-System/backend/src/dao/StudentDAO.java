package dao;

import config.DBConnection;
import model.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Student Data Access Object (DAO)
 *
 * Responsibility: Handle all SQL queries for the students table
 * Rule: ONLY this class should write SQL for students
 * BE2 - DAO & CRUD Operations
 */
public class StudentDAO {

	/**
	 * Insert a new student into the database
	 *
	 * @param student the Student object to insert
	 * @throws SQLException if database operation fails
	 */
	public void insertStudent(Student student) throws SQLException {
		String sql = "INSERT INTO students (full_name, full_address, birthdate, place_of_birth, degree, major) " +
					 "VALUES (?, ?, ?, ?, ?, ?)";
		
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setString(1, student.getFullName());
			pstmt.setString(2, student.getFullAddress());
			pstmt.setDate(3, new java.sql.Date(student.getBirthdate().getTime()));
			pstmt.setString(4, student.getPlaceOfBirth());
			pstmt.setString(5, student.getDegree());
			pstmt.setString(6, student.getMajor());
			
			int rowsAffected = pstmt.executeUpdate();
			System.out.println("[StudentDAO] Inserted " + rowsAffected + " student(s)");
			
		} catch (SQLException e) {
			System.err.println("[StudentDAO] Error inserting student: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Retrieve a student by ID
	 *
	 * @param studentId the ID of the student to retrieve
	 * @return Student object or null if not found
	 * @throws SQLException if database operation fails
	 */
	public Student getStudentById(int studentId) throws SQLException {
		String sql = "SELECT * FROM students WHERE student_id = ?";
		Student student = null;
		
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setInt(1, studentId);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				student = new Student(
					rs.getInt("student_id"),
					rs.getString("full_name"),
					rs.getString("full_address"),
					rs.getDate("birthdate"),
					rs.getString("place_of_birth"),
					rs.getString("degree"),
					rs.getString("major")
				);
			}
			
		} catch (SQLException e) {
			System.err.println("[StudentDAO] Error retrieving student: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		return student;
	}

	/**
	 * Retrieve all students
	 *
	 * @return List of all Student objects
	 * @throws SQLException if database operation fails
	 */
	public List<Student> getAllStudents() throws SQLException {
		String sql = "SELECT * FROM students ORDER BY full_name ASC";
		List<Student> students = new ArrayList<>();
		
		try (Connection conn = DBConnection.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)) {
			
			while (rs.next()) {
				Student student = new Student(
					rs.getInt("student_id"),
					rs.getString("full_name"),
					rs.getString("full_address"),
					rs.getDate("birthdate"),
					rs.getString("place_of_birth"),
					rs.getString("degree"),
					rs.getString("major")
				);
				students.add(student);
			}
			
		} catch (SQLException e) {
			System.err.println("[StudentDAO] Error retrieving all students: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		return students;
	}
}
