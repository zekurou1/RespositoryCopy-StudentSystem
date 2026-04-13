package service;

import config.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Transcript Service
 *
 * Responsibility: Build and return Transcript of Records (TOR) data for a student
 * - Executes JOIN query across students, enrollments, courses tables
 * - Computes summary statistics
 * - Returns complete student info + course enrollment records
 */
public class TranscriptService {

	/**
	 * Get transcript for a student (called by TranscriptPanel)
	 * 
	 * @param studentId the student ID
	 * @return TranscriptResult containing student info + List of enrollment records
	 * @throws Exception if query fails
	 */
	public TranscriptResult getTranscript(int studentId) throws Exception {
		String sql = "SELECT s.student_id, s.full_name, s.full_address, s.birthdate, s.place_of_birth, " +
					 "s.degree, s.major, " +
					 "c.course_id, c.course_name, c.descriptive_title, c.term, c.units, c.school_year, " +
					 "e.grade, e.date_admitted, e.date_conferred, e.category, e.educational_attainment " +
					 "FROM enrollments e " +
					 "JOIN students s ON e.student_id = s.student_id " +
					 "JOIN courses c ON e.course_id = c.course_id " +
					 "WHERE s.student_id = ? " +
					 "ORDER BY c.school_year DESC, c.term DESC";
		
		TranscriptResult result = new TranscriptResult();
		
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setInt(1, studentId);
			ResultSet rs = pstmt.executeQuery();
			
			int rowCount = 0;
			while (rs.next()) {
				// Map student info only on first iteration
				if (rowCount == 0) {
					result.studentId = rs.getInt("student_id");
					result.fullName = rs.getString("full_name");
					result.fullAddress = rs.getString("full_address");
					result.birthdate = rs.getDate("birthdate");
					result.placeOfBirth = rs.getString("place_of_birth");
					result.degree = rs.getString("degree");
					result.major = rs.getString("major");
				}
				
				// Map enrollment record
				EnrollmentRecord record = new EnrollmentRecord();
				record.courseId = rs.getInt("course_id");
				record.courseName = rs.getString("course_name");
				record.descriptiveTitle = rs.getString("descriptive_title");
				record.term = rs.getString("term");
				record.units = rs.getInt("units");
				record.schoolYear = rs.getString("school_year");
				record.grade = rs.getDouble("grade");
				record.dateAdmitted = rs.getDate("date_admitted");
				
				// Determine status based on grade
				if (rs.wasNull() || Double.isNaN(record.grade)) {
					record.status = "Enrolled (No Grade)";
					record.grade = null;
				} else if (record.grade <= 3.0) {
					record.status = "Passed";
				} else {
					record.status = "Failed";
				}
				
				result.enrollments.add(record);
				rowCount++;
			}
			
			// Calculate summary statistics
			if (!result.enrollments.isEmpty()) {
				calculateSummary(result);
			}
			
		} catch (SQLException e) {
			System.err.println("[TranscriptService] Error retrieving transcript: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		return result;
	}
	
	/**
	 * Calculate summary statistics for transcript
	 */
	private void calculateSummary(TranscriptResult result) {
		int totalUnits = 0;
		int earnedUnits = 0;
		double totalGrade = 0;
		int gradedCount = 0;
		
		for (EnrollmentRecord record : result.enrollments) {
			totalUnits += record.units;
			
			if (record.grade != null && !Double.isNaN(record.grade)) {
				earnedUnits += record.units;
				totalGrade += record.grade;
				gradedCount++;
			}
		}
		
		result.totalUnits = totalUnits;
		result.earnedUnits = earnedUnits;
		result.gpa = (gradedCount > 0) ? (totalGrade / gradedCount) : 0.0;
	}

	// ───────────────────────────────────────────────
	// Inner Classes for Result DTOs
	// ───────────────────────────────────────────────

	/**
	 * TranscriptResult - Holds complete transcript data for a student
	 */
	public static class TranscriptResult {
		public int studentId;
		public String fullName;
		public String fullAddress;
		public Date birthdate;
		public String placeOfBirth;
		public String degree;
		public String major;
		
		public List<EnrollmentRecord> enrollments = new ArrayList<>();
		
		// Summary statistics
		public int totalUnits;
		public int earnedUnits;
		public double gpa;
		
		public TranscriptResult() {}
	}
	
	/**
	 * EnrollmentRecord - Holds individual course enrollment record
	 */
	public static class EnrollmentRecord {
		public int courseId;
		public String courseName;
		public String descriptiveTitle;
		public String term;
		public int units;
		public String schoolYear;
		public Double grade;
		public Date dateAdmitted;
		public String status;  // "Passed", "Failed", "Enrolled (No Grade)"
		
		public EnrollmentRecord() {}
		
		@Override
		public String toString() {
			return term + " | " + courseName + " | " + 
				   (grade != null ? String.format("%.2f", grade) : "N/A") + " | " +
				   units + " units | " + status;
		}
	}
}
