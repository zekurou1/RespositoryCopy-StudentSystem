package ui.panels;

import dao.CourseDAO;
import dao.EnrollmentDAO;
import model.Course;
import model.Student;
import service.StudentService;
import ui.AppTheme;
import ui.components.FormUtils;
import utils.Validator;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Enroll Student Panel
 *
 * Responsibility: Form UI for enrolling a student into a course
 * - Displays form fields:
 *   * Student ID (dropdown - loads all students)
 *   * Course ID (dropdown - loads all courses)
 *   * Term
 *   * School Year
 *   * Category
 *   * Date Admitted (date picker)
 * - Includes Submit button
 * - Calls EnrollmentDAO.enrollStudent() on submit
 * - Shows confirmation dialog on success
 * FE2 - Student Forms (Input)
 */
public class EnrollStudentPanel extends JPanel {

	private JComboBox<String> studentCombo;
	private JComboBox<String> courseCombo;
	private JTextField termField;
	private JTextField schoolYearField;
	private JTextField categoryField;
	private JTextField dateAdmittedField;

	private StudentService studentService;
	private CourseDAO courseDAO;
	private EnrollmentDAO enrollmentDAO;
	
	private List<Student> studentList;
	private List<Course> courseList;

	/**
	 * Constructor - Initialize the panel
	 */
	public EnrollStudentPanel() {
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
			System.err.println("[EnrollStudentPanel] Error loading data: " + e.getMessage());
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
		JLabel titleLabel = new JLabel("Enroll Student to Course");
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
		formPanel.setMaximumSize(new Dimension(500, 350));

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
		gbc.gridx = 1;
		formPanel.add(studentCombo, gbc);

		// Course dropdown
		gbc.gridy = 1;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Course:"), gbc);
		courseCombo = new JComboBox<>();
		populateCourseDropdown();
		courseCombo.setPreferredSize(new Dimension(200, 28));
		gbc.gridx = 1;
		formPanel.add(courseCombo, gbc);

		// Term
		gbc.gridy = 2;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Term:"), gbc);
		termField = FormUtils.createTextField("");
		gbc.gridx = 1;
		formPanel.add(termField, gbc);

		// School Year
		gbc.gridy = 3;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("School Year:"), gbc);
		schoolYearField = FormUtils.createTextField("");
		gbc.gridx = 1;
		formPanel.add(schoolYearField, gbc);

		// Category
		gbc.gridy = 4;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Category:"), gbc);
		categoryField = FormUtils.createTextField("Regular");
		gbc.gridx = 1;
		formPanel.add(categoryField, gbc);

		// Date Admitted (yyyy-MM-dd)
		gbc.gridy = 5;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Date Admitted (yyyy-MM-dd):"), gbc);
		dateAdmittedField = FormUtils.createTextField("");
		gbc.gridx = 1;
		formPanel.add(dateAdmittedField, gbc);

		add(formPanel);
		add(Box.createVerticalStrut(16));

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(AppTheme.WHITE);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JButton submitBtn = new JButton("Enroll");
		AppTheme.stylePrimaryButton(submitBtn);
		submitBtn.addActionListener(e -> enrollStudent());

		JButton clearBtn = FormUtils.createClearButton();
		clearBtn.addActionListener(e -> clearForm());

		buttonPanel.add(submitBtn);
		buttonPanel.add(clearBtn);

		add(buttonPanel);
		add(Box.createVerticalGlue());
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
	 * Populate course dropdown
	 */
	private void populateCourseDropdown() {
		courseCombo.removeAllItems();
		if (courseList != null && !courseList.isEmpty()) {
			for (Course c : courseList) {
				courseCombo.addItem(c.getCourseId() + " - " + c.getCourseName());
			}
		} else {
			courseCombo.addItem("No courses available");
		}
	}

	/**
	 * Refresh dropdown data when panel becomes visible
	 */
	public void refreshData() {
		try {
			studentList = studentService.getAllStudents();
			courseList = courseDAO.getAllCourses();
			populateStudentDropdown();
			populateCourseDropdown();
		} catch (Exception e) {
			System.err.println("[EnrollStudentPanel] Error refreshing data: " + e.getMessage());
		}
	}

	/**
	 * Validate and enroll a student
	 */
	private void enrollStudent() {
		if (studentList == null || studentList.isEmpty() || courseList == null || courseList.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No students or courses available", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int studentIndex = studentCombo.getSelectedIndex();
		int courseIndex = courseCombo.getSelectedIndex();
		String term = termField.getText();
		String schoolYear = schoolYearField.getText();
		String category = categoryField.getText();
		String dateAdmittedStr = dateAdmittedField.getText();

		// Validation
		if (studentIndex < 0) {
			JOptionPane.showMessageDialog(this, "Please select a student", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (courseIndex < 0) {
			JOptionPane.showMessageDialog(this, "Please select a course", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!Validator.isNotEmpty(term)) {
			JOptionPane.showMessageDialog(this, "Term is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!Validator.isNotEmpty(schoolYear)) {
			JOptionPane.showMessageDialog(this, "School Year is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!Validator.isNotEmpty(category)) {
			JOptionPane.showMessageDialog(this, "Category is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!Validator.isValidDateFormat(dateAdmittedStr)) {
			JOptionPane.showMessageDialog(this, "Date Admitted must be in format yyyy-MM-dd", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Parse date
		Date dateAdmitted = Validator.parseDate(dateAdmittedStr);
		if (dateAdmitted == null) {
			JOptionPane.showMessageDialog(this, "Invalid date format", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			Student selectedStudent = studentList.get(studentIndex);
			Course selectedCourse = courseList.get(courseIndex);
			
			// Call DAO to enroll student
			enrollmentDAO.enrollStudent(selectedStudent.getStudentId(), selectedCourse.getCourseId(), 
										term, schoolYear, category, dateAdmitted);
			
			JOptionPane.showMessageDialog(this, "Student Enrolled Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
			clearForm();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error enrolling student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Clear all form fields
	 */
	private void clearForm() {
		studentCombo.setSelectedIndex(0);
		courseCombo.setSelectedIndex(0);
		termField.setText("");
		schoolYearField.setText("");
		categoryField.setText("Regular");
		dateAdmittedField.setText("");
	}
}
