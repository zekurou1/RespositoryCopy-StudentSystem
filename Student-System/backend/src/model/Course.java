package model;

/**
 * Course Model - POJO
 *
 * Responsibility: Represent a course with fields matching the courses table
 * BE2 - DAO & CRUD Operations
 */
public class Course {

	private int courseId;
	private String courseName;
	private String college;
	private String term;
	private String descriptiveTitle;
	private String courseYearSection;
	private int units;
	private String days;
	private String time;
	private String room;
	private String schoolYear;
	private String instructor;

	/**
	 * Constructor without course_id (for new courses)
	 */
	public Course(String courseName, String college, String term,
	              String descriptiveTitle, String courseYearSection, int units,
	              String days, String time, String room, String schoolYear, String instructor) {
		this.courseName = courseName;
		this.college = college;
		this.term = term;
		this.descriptiveTitle = descriptiveTitle;
		this.courseYearSection = courseYearSection;
		this.units = units;
		this.days = days;
		this.time = time;
		this.room = room;
		this.schoolYear = schoolYear;
		this.instructor = instructor;
	}

	/**
	 * Constructor with course_id (for existing courses from database)
	 */
	public Course(int courseId, String courseName, String college, String term,
	              String descriptiveTitle, String courseYearSection, int units,
	              String days, String time, String room, String schoolYear, String instructor) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.college = college;
		this.term = term;
		this.descriptiveTitle = descriptiveTitle;
		this.courseYearSection = courseYearSection;
		this.units = units;
		this.days = days;
		this.time = time;
		this.room = room;
		this.schoolYear = schoolYear;
		this.instructor = instructor;
	}

	/**
	 * Default constructor
	 */
	public Course() {
	}

	// Getters and Setters

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getCollege() {
		return college;
	}

	public void setCollege(String college) {
		this.college = college;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getDescriptiveTitle() {
		return descriptiveTitle;
	}

	public void setDescriptiveTitle(String descriptiveTitle) {
		this.descriptiveTitle = descriptiveTitle;
	}

	public String getCourseYearSection() {
		return courseYearSection;
	}

	public void setCourseYearSection(String courseYearSection) {
		this.courseYearSection = courseYearSection;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	@Override
	public String toString() {
		return "Course{" +
				"courseId=" + courseId +
				", courseName='" + courseName + '\'' +
				", college='" + college + '\'' +
				", term='" + term + '\'' +
				", descriptiveTitle='" + descriptiveTitle + '\'' +
				", courseYearSection='" + courseYearSection + '\'' +
				", units=" + units +
				", days='" + days + '\'' +
				", time='" + time + '\'' +
				", room='" + room + '\'' +
				", schoolYear='" + schoolYear + '\'' +
				", instructor='" + instructor + '\'' +
				'}';
	}
}
