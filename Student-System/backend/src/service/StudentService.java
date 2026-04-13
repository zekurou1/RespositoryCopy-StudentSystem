package service;

import dao.StudentDAO;
import model.Student;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Student Service
 *
 * Responsibility: Wrap StudentDAO, handle business logic, called by frontend forms
 * Rule: ONLY Service calls DAO, never directly from UI
 * BE2 - DAO & CRUD Operations
 */
public class StudentService {

	private StudentDAO studentDAO = new StudentDAO();

	/**
	 * Insert a new student (called by RegisterStudentPanel)
	 *
	 * @param fullName the student's full name
	 * @param address the student's address
	 * @param birthdate the student's birthdate
	 * @param placeOfBirth the student's place of birth
	 * @param degree the student's degree
	 * @param major the student's major
	 * @throws SQLException if insertion fails
	 */
	public void insertStudent(String fullName, String address, Date birthdate,
							  String placeOfBirth, String degree,
							  String major) throws SQLException {
		try {
			// Create Student object
			Student student = new Student(fullName, address, birthdate, placeOfBirth, degree, major);
			
			// Call DAO to insert
			studentDAO.insertStudent(student);
			
			System.out.println("[StudentService] Successfully inserted student: " + fullName);
			
		} catch (SQLException e) {
			System.err.println("[StudentService] Error inserting student: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Get all students (called by dropdowns in EnrollStudentPanel, GradePanel, TranscriptPanel)
	 *
	 * @return List of all students
	 * @throws SQLException if query fails
	 */
	public List<Student> getAllStudents() throws SQLException {
		try {
			return studentDAO.getAllStudents();
		} catch (SQLException e) {
			System.err.println("[StudentService] Error retrieving all students: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Get a student by ID
	 *
	 * @param studentId the student ID
	 * @return Student object or null if not found
	 * @throws SQLException if query fails
	 */
	public Student getStudentById(int studentId) throws SQLException {
		try {
			return studentDAO.getStudentById(studentId);
		} catch (SQLException e) {
			System.err.println("[StudentService] Error retrieving student by ID: " + e.getMessage());
			throw e;
		}
	}
}
