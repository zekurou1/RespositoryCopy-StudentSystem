package ui.panels;

import dao.CourseDAO;
import dao.EnrollmentDAO;
import model.Course;
import model.Enrollment;
import model.Student;
import service.StudentService;
import ui.AppTheme;
import ui.components.FormUtils;
import utils.Validator;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * Grade Panel
 *
 * Responsibility: Form UI for encoding and updating grades
 * - Displays form fields:
 *   * Student ID (dropdown - loads all students)
 *   * Course ID (dropdown - loads courses for selected student)
 *   * Grade (text field - accepts 1.0 to 5.0)
 * - Includes Submit button
 * - Validates grade range (1.0 to 5.0)
 * - Calls EnrollmentDAO.addGrade() on submit
 * - Shows confirmation dialog on success
 */
public class GradePanel extends JPanel {

	private JComboBox<String> studentCombo;
	private JComboBox<String> courseCombo;
	private JTextField gradeField;

	private StudentService studentService;
	private CourseDAO courseDAO;
	private EnrollmentDAO enrollmentDAO;
	
	private List<Student> studentList;
	private List<Course> courseList;
	private List<Course> studentEnrollments;

	/**
	 * Constructor - Initialize the panel
	 */
	public GradePanel() {
		this.studentService = new StudentService();
		this.courseDAO = new CourseDAO();
		this.enrollmentDAO = new EnrollmentDAO();
		setBackground(AppTheme.WHITE);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPadding();
		loadData();
		initializeComponents();
	}

	/**
	 * Set panel padding
	 */
	private void setPadding() {
		setBorder(BorderFactory.createEmptyBorder(
			AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD, 
			AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD
		));
	}

	/**
	 * Load students and courses from backend
	 */
	private void loadData() {
		try {
			studentList = studentService.getAllStudents();
			courseList = courseDAO.getAllCourses();
		} catch (Exception e) {
			System.err.println("[GradePanel] Error loading data: " + e.getMessage());
			e.printStackTrace();
			studentList = new Vector<>();
			courseList = new Vector<>();
		}
	}

	/**
	 * Initialize all form components
	 */
	private void initializeComponents() {
		// Title
		JLabel titleLabel = new JLabel("Encode Student Grade");
		titleLabel.setFont(AppTheme.FONT_HEADING);
		titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
		titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(titleLabel);
		add(Box.createVerticalStrut(16));

		// Form Fields Container
		JPanel formPanel = new JPanel();
		formPanel.setBackground(AppTheme.WHITE);
		formPanel.setLayout(new GridBagLayout());
		formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		formPanel.setMaximumSize(new Dimension(500, 200));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 0, 8, 16);
		gbc.anchor = GridBagConstraints.WEST;

		// Student dropdown
		gbc.gridy = 0;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Student:"), gbc);
		studentCombo = new JComboBox<>();
		populateStudentDropdown();
		studentCombo.setPreferredSize(new Dimension(200, 28));
		studentCombo.addActionListener(e -> onStudentSelected());
		gbc.gridx = 1;
		formPanel.add(studentCombo, gbc);

		// Course dropdown (dependent on student)
		gbc.gridy = 1;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Course:"), gbc);
		courseCombo = new JComboBox<>();
		courseCombo.setPreferredSize(new Dimension(200, 28));
		gbc.gridx = 1;
		formPanel.add(courseCombo, gbc);

		// Grade
		gbc.gridy = 2;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Grade (1.0-5.0):"), gbc);
		gradeField = FormUtils.createTextField("");
		gbc.gridx = 1;
		formPanel.add(gradeField, gbc);

		add(formPanel);
		add(Box.createVerticalStrut(16));

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(AppTheme.WHITE);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JButton submitBtn = new JButton("Submit Grade");
		AppTheme.stylePrimaryButton(submitBtn);
		submitBtn.addActionListener(e -> saveGrade());

		JButton clearBtn = FormUtils.createClearButton();
		clearBtn.addActionListener(e -> clearForm());

		buttonPanel.add(submitBtn);
		buttonPanel.add(clearBtn);

		add(buttonPanel);
		add(Box.createVerticalGlue());
		
		// Load initial courses
		onStudentSelected();
	}

	/**
	 * Populate student dropdown
	 */
	private void populateStudentDropdown() {
		studentCombo.removeAllItems();
		if (studentList != null && !studentList.isEmpty()) {
			for (Student s : studentList) {
				studentCombo.addItem(s.getStudentId() + " - " + s.getFullName());
			}
		} else {
			studentCombo.addItem("No students available");
		}
	}

	/**
	 * Load courses for selected student
	 */
	private void onStudentSelected() {
		int studentIndex = studentCombo.getSelectedIndex();
		if (studentIndex < 0 || studentList == null || studentList.isEmpty()) {
			courseCombo.removeAllItems();
			courseCombo.addItem("No courses available");
			return;
		}

		Student selectedStudent = studentList.get(studentIndex);
		studentEnrollments = new Vector<>();

		// Load enrolled courses for selected student from EnrollmentDAO
		try {
			if (courseList != null) {
				for (Course course : courseList) {
					Enrollment enrollment = enrollmentDAO.getEnrollment(selectedStudent.getStudentId(), course.getCourseId());
					if (enrollment != null) {
						studentEnrollments.add(course);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("[GradePanel] Error loading enrollments: " + e.getMessage());
			e.printStackTrace();
		}

		// Populate course dropdown with enrolled courses only
		populateCourseDropdown();
	}

	/**
	 * Populate course dropdown with courses enrolled by selected student
	 */
	private void populateCourseDropdown() {
		courseCombo.removeAllItems();
		if (studentEnrollments != null && !studentEnrollments.isEmpty()) {
			for (Course c : studentEnrollments) {
				courseCombo.addItem(c.getCourseId() + " - " + c.getCourseName());
			}
		} else {
			courseCombo.addItem("No enrolled courses");
		}
	}

	/**
	 * Validate and save grade
	 */
	private void saveGrade() {
		if (studentList == null || studentList.isEmpty() || studentEnrollments == null || studentEnrollments.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No students or courses available", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int studentIndex = studentCombo.getSelectedIndex();
		int courseIndex = courseCombo.getSelectedIndex();
		String gradeStr = gradeField.getText();

		// Validation
		if (studentIndex < 0) {
			JOptionPane.showMessageDialog(this, "Please select a student", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (courseIndex < 0) {
			JOptionPane.showMessageDialog(this, "Please select a course", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!Validator.isNotEmpty(gradeStr)) {
			JOptionPane.showMessageDialog(this, "Grade is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!Validator.isValidDouble(gradeStr)) {
			JOptionPane.showMessageDialog(this, "Grade must be a valid number", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		double grade = Double.parseDouble(gradeStr);
		if (!Validator.isValidGrade(grade)) {
			JOptionPane.showMessageDialog(this, "Grade must be between 1.0 and 5.0", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			Student selectedStudent = studentList.get(studentIndex);
			Course selectedCourse = studentEnrollments.get(courseIndex);
			
			// Call DAO to add grade
			enrollmentDAO.addGrade(selectedStudent.getStudentId(), selectedCourse.getCourseId(), grade);
			
			JOptionPane.showMessageDialog(this, "Grade Submitted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
			clearForm();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error submitting grade: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Clear all form fields
	 */
	private void clearForm() {
		studentCombo.setSelectedIndex(0);
		onStudentSelected();
		gradeField.setText("");
	}
}
