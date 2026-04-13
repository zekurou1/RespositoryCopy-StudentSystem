package model;

import java.util.Date;

/**
 * Enrollment Model - POJO
 *
 * Responsibility: Represent an enrollment with fields matching the enrollments table
 * BE2 - DAO & CRUD Operations
 */
public class Enrollment {

	private int enrollmentId;
	private int studentId;
	private int courseId;
	private Double grade;
	private Date dateConferred;
	private String category;
	private String educationalAttainment;
	private Date dateAdmitted;

	/**
	 * Constructor without enrollment_id (for new enrollments)
	 */
	public Enrollment(int studentId, int courseId, Double grade, Date dateConferred,
	                  String category, String educationalAttainment, Date dateAdmitted) {
		this.studentId = studentId;
		this.courseId = courseId;
		this.grade = grade;
		this.dateConferred = dateConferred;
		this.category = category;
		this.educationalAttainment = educationalAttainment;
		this.dateAdmitted = dateAdmitted;
	}

	/**
	 * Constructor with enrollment_id (for existing enrollments from database)
	 */
	public Enrollment(int enrollmentId, int studentId, int courseId, Double grade,
	                  Date dateConferred, String category, String educationalAttainment, Date dateAdmitted) {
		this.enrollmentId = enrollmentId;
		this.studentId = studentId;
		this.courseId = courseId;
		this.grade = grade;
		this.dateConferred = dateConferred;
		this.category = category;
		this.educationalAttainment = educationalAttainment;
		this.dateAdmitted = dateAdmitted;
	}

	/**
	 * Default constructor
	 */
	public Enrollment() {
	}

	// Getters and Setters

	public int getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(int enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public Date getDateConferred() {
		return dateConferred;
	}

	public void setDateConferred(Date dateConferred) {
		this.dateConferred = dateConferred;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getEducationalAttainment() {
		return educationalAttainment;
	}

	public void setEducationalAttainment(String educationalAttainment) {
		this.educationalAttainment = educationalAttainment;
	}

	public Date getDateAdmitted() {
		return dateAdmitted;
	}

	public void setDateAdmitted(Date dateAdmitted) {
		this.dateAdmitted = dateAdmitted;
	}

	@Override
	public String toString() {
		return "Enrollment{" +
				"enrollmentId=" + enrollmentId +
				", studentId=" + studentId +
				", courseId=" + courseId +
				", grade=" + grade +
				", dateConferred=" + dateConferred +
				", category='" + category + '\'' +
				", educationalAttainment='" + educationalAttainment + '\'' +
				", dateAdmitted=" + dateAdmitted +
				'}';
	}
}
