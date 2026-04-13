package model;

import java.util.Date;

/**
 * Student Model - POJO
 *
 * Responsibility: Represent a student with fields matching the students table
 * BE2 - DAO & CRUD Operations
 */
public class Student {

	private int studentId;
	private String fullName;
	private String fullAddress;
	private Date birthdate;
	private String placeOfBirth;
	private String degree;
	private String major;

	/**
	 * Constructor without student_id (for new students)
	 */
	public Student(String fullName, String fullAddress, Date birthdate,
	               String placeOfBirth, String degree, String major) {
		this.fullName = fullName;
		this.fullAddress = fullAddress;
		this.birthdate = birthdate;
		this.placeOfBirth = placeOfBirth;
		this.degree = degree;
		this.major = major;
	}

	/**
	 * Constructor with student_id (for existing students from database)
	 */
	public Student(int studentId, String fullName, String fullAddress, Date birthdate,
	               String placeOfBirth, String degree, String major) {
		this.studentId = studentId;
		this.fullName = fullName;
		this.fullAddress = fullAddress;
		this.birthdate = birthdate;
		this.placeOfBirth = placeOfBirth;
		this.degree = degree;
		this.major = major;
	}

	/**
	 * Default constructor
	 */
	public Student() {
	}

	// Getters and Setters

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	@Override
	public String toString() {
		return "Student{" +
				"studentId=" + studentId +
				", fullName='" + fullName + '\'' +
				", fullAddress='" + fullAddress + '\'' +
				", birthdate=" + birthdate +
				", placeOfBirth='" + placeOfBirth + '\'' +
				", degree='" + degree + '\'' +
				", major='" + major + '\'' +
				'}';
	}
}
