package dao;

import config.DBConnection;
import model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Course Data Access Object (DAO)
 *
 * Responsibility: Handle all SQL queries for the courses table
 * Rule: ONLY this class should write SQL for courses
 * BE2 - DAO & CRUD Operations
 */
public class CourseDAO {

	/**
	 * Insert a new course into the database
	 *
	 * @param course the Course object to insert
	 * @throws SQLException if database operation fails
	 */
	public void insertCourse(Course course) throws SQLException {
		String sql = "INSERT INTO courses (course_name, college, term, descriptive_title, course_year_section, " +
					 "units, days, time, room, school_year, instructor) " +
					 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setString(1, course.getCourseName());
			pstmt.setString(2, course.getCollege());
			pstmt.setString(3, course.getTerm());
			pstmt.setString(4, course.getDescriptiveTitle());
			pstmt.setString(5, course.getCourseYearSection());
			pstmt.setInt(6, course.getUnits());
			pstmt.setString(7, course.getDays());
			pstmt.setString(8, course.getTime());
			pstmt.setString(9, course.getRoom());
			pstmt.setString(10, course.getSchoolYear());
			pstmt.setString(11, course.getInstructor());
			
			int rowsAffected = pstmt.executeUpdate();
			System.out.println("[CourseDAO] Inserted " + rowsAffected + " course(s)");
			
		} catch (SQLException e) {
			System.err.println("[CourseDAO] Error inserting course: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Retrieve a course by ID
	 *
	 * @param courseId the ID of the course to retrieve
	 * @return Course object or null if not found
	 * @throws SQLException if database operation fails
	 */
	public Course getCourseById(int courseId) throws SQLException {
		String sql = "SELECT * FROM courses WHERE course_id = ?";
		Course course = null;
		
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			
			pstmt.setInt(1, courseId);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				course = new Course(
					rs.getInt("course_id"),
					rs.getString("course_name"),
					rs.getString("college"),
					rs.getString("term"),
					rs.getString("descriptive_title"),
					rs.getString("course_year_section"),
					rs.getInt("units"),
					rs.getString("days"),
					rs.getString("time"),
					rs.getString("room"),
					rs.getString("school_year"),
					rs.getString("instructor")
				);
			}
			
		} catch (SQLException e) {
			System.err.println("[CourseDAO] Error retrieving course: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		return course;
	}

	/**
	 * Retrieve all courses
	 *
	 * @return List of all Course objects
	 * @throws SQLException if database operation fails
	 */
	public List<Course> getAllCourses() throws SQLException {
		String sql = "SELECT course_id, course_name, college, term, descriptive_title, course_year_section, " +
					 "units, days, time, room, school_year, instructor FROM courses ORDER BY course_name ASC";
		List<Course> courses = new ArrayList<>();
		
		try (Connection conn = DBConnection.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)) {
			
			while (rs.next()) {
				Course course = new Course(
					rs.getInt("course_id"),
					rs.getString("course_name"),
					rs.getString("college"),
					rs.getString("term"),
					rs.getString("descriptive_title"),
					rs.getString("course_year_section"),
					rs.getInt("units"),
					rs.getString("days"),
					rs.getString("time"),
					rs.getString("room"),
					rs.getString("school_year"),
					rs.getString("instructor")
				);
				courses.add(course);
			}
			
		} catch (SQLException e) {
			System.err.println("[CourseDAO] Error retrieving all courses: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		
		return courses;
	}
}
