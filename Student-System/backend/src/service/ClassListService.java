package service;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class List Service
 *
 * Responsibility: Build and return Class List data for a course
 * - Executes JOIN query across enrollments, students, courses tables
 * - Computes summary counts (passed, failed, incomplete, etc.)
 * - Returns course info + student enrollment records + summary
 */
public class ClassListService {

	/**
	 * Get class list for a course (called by ClassListPanel)
	 * 
	 * @param courseId the course ID
	 * @return ClassListResult containing course info + List of student records + summary counts
	 * @throws Exception if query fails
	 */
	public ClassListResult getClassList(int courseId) throws Exception {
		// First, get course info
		String courseSQL = "SELECT * FROM courses WHERE course_id = ?";
		
		// Then, get all enrollments for this course
		String enrollSQL = "SELECT s.student_id, s.full_name, s.degree, e.grade, e.date_admitted, c.units " +
						  "FROM enrollments e " +
						  "JOIN students s ON e.student_id = s.student_id " +
						  "JOIN courses c ON e.course_id = c.course_id " +
						  "WHERE e.course_id = ? " +
						  "ORDER BY s.full_name ASC";
		
		ClassListResult result = new ClassListResult();
		
		try (Connection conn = DBConnection.getConnection()) {
			
			// Get course info
			try (PreparedStatement pstmt = conn.prepareStatement(courseSQL)) {
				pstmt.setInt(1, courseId);
				ResultSet rs = pstmt.executeQuery();
				
				if (rs.next()) {
					result.courseId = rs.getInt("course_id");
					result.courseName = rs.getString("course_name");
					result.college = rs.getString("college");
					result.descriptiveTitle = rs.getString("descriptive_title");
					result.units = rs.getInt("units");
					result.term = rs.getString("term");
					result.schoolYear = rs.getString("school_year");
					result.instructor = rs.getString("instructor");
					result.days = rs.getString("days");
					result.time = rs.getString("time");
				}
			}
			
			// Get student enrollments
			try (PreparedStatement pstmt = conn.prepareStatement(enrollSQL)) {
				pstmt.setInt(1, courseId);
				ResultSet rs = pstmt.executeQuery();
				
				while (rs.next()) {
					StudentEnrollment student = new StudentEnrollment();
					student.studentId = rs.getInt("student_id");
					student.studentName = rs.getString("full_name");
					student.degree = rs.getString("degree");
					student.grade = rs.getDouble("grade");
					student.units = rs.getInt("units");
					
					// Determine status
					if (rs.wasNull() || Double.isNaN(student.grade)) {
						student.status = "Pending Grade";
						student.grade = null;
					} else if (student.grade <= 3.0) {
						student.status = "Passed";
					} else {
						student.status = "Failed";
					}
					
					result.students.add(student);
				}
			}
			
			// Calculate summary
			if (!result.students.isEmpty()) {
				calculateSummary(result);
			}
			
		} catch (SQLException e) {
			System.err.println("[ClassListService] Error retrieving class list: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		return result;
	}
	
	/**
	 * Calculate summary statistics for class list
	 */
	private void calculateSummary(ClassListResult result) {
		int graded = 0;
		int pending = 0;
		int passed = 0;
		int failed = 0;
		double totalGrade = 0;
		
		for (StudentEnrollment student : result.students) {
			if (student.grade == null || Double.isNaN(student.grade)) {
				pending++;
			} else {
				graded++;
				totalGrade += student.grade;
				if (student.grade <= 3.0) {
					passed++;
				} else {
					failed++;
				}
			}
		}
		
		result.totalStudents = result.students.size();
		result.gradedCount = graded;
		result.pendingGradeCount = pending;
		result.passedCount = passed;
		result.failedCount = failed;
		result.classAverage = (graded > 0) ? (totalGrade / graded) : 0.0;
	}

	// ───────────────────────────────────────────────
	// Inner Classes for Result DTOs
	// ───────────────────────────────────────────────

	/**
	 * ClassListResult - Holds complete class list data for a course
	 */
	public static class ClassListResult {
		public int courseId;
		public String courseName;
		public String college;
		public String descriptiveTitle;
		public int units;
		public String term;
		public String schoolYear;
		public String instructor;
		public String days;
		public String time;
		
		public List<StudentEnrollment> students = new ArrayList<>();
		
		// Summary statistics
		public int totalStudents;
		public int gradedCount;
		public int pendingGradeCount;
		public int passedCount;
		public int failedCount;
		public double classAverage;
		
		public ClassListResult() {}
	}
	
	/**
	 * StudentEnrollment - Holds individual student enrollment record
	 */
	public static class StudentEnrollment {
		public int studentId;
		public String studentName;
		public String degree;
		public Double grade;
		public int units;
		public String status;  // "Passed", "Failed", "Pending Grade"
		
		public StudentEnrollment() {}
		
		@Override
		public String toString() {
			return studentId + " | " + studentName + " | " + 
				   (grade != null ? String.format("%.2f", grade) : "N/A") + " | " +
				   status;
		}
	}
}
