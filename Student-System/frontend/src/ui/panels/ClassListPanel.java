package ui.panels;

import ui.AppTheme;
import ui.components.TableUtils;
import service.ClassListService;
import dao.CourseDAO;
import model.Course;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.util.List;
import java.util.Vector;

/**
 * Class List Panel — FE3
 *
 * Responsibility: Display Class List and summary for a course
 * - Displays form fields:
 *   * Course ID (dropdown - loads all courses)
 *   * View button
 * - When course is selected and View clicked:
 *   * Header section: JLabel fields displaying course info
 *     - Institution, College, Subject Code, Descriptive Title, Units,
 *       Course/Year/Section, Term/School Year, Days, Schedule, Room, Instructor
 *   * Table section: JTable with students enrolled in course
 *     - Columns: No. | Student ID | Student Name | Grade | Units | Degree
 *   * Footer section: Summary counts
 *     - Total Enrolled, Passed, Failed, Incomplete, Dropped
 *   * Signature lines (Instructor, Department Head, Dean of College)
 */
public class ClassListPanel extends JPanel {

	private JTable studentTable;
	private JComboBox<String> courseDropdown;
	private JButton viewButton;
	
	// Services
	private ClassListService classListService;
	private CourseDAO courseDAO;
	private List<Course> courseList;
	
	// Course header labels
	private JLabel institutionLabel;
	private JLabel collegeLabel;
	private JLabel subjectCodeLabel;
	private JLabel descriptiveTitleLabel;
	private JLabel unitsLabel;
	private JLabel courseYearSectionLabel;
	private JLabel termSchoolYearLabel;
	private JLabel daysLabel;
	private JLabel scheduleLabel;
	private JLabel roomLabel;
	private JLabel instructorLabel;
	
	// Summary count labels
	private JLabel totalLabel;
	private JLabel passedLabel;
	private JLabel failedLabel;
	private JLabel incompleteLabel;
	private JLabel droppedLabel;
	
	private JPanel tablePanel;
	private JPanel summaryPanel;

	/**
	 * Constructor - Initialize the panel
	 */
	public ClassListPanel() {
		// Initialize services
		this.classListService = new ClassListService();
		this.courseDAO = new CourseDAO();
		
		// Set panel styling
		AppTheme.stylePanel(this);
		setLayout(new BorderLayout(0, AppTheme.CONTENT_PAD));

		// Add header with title and selector
		JPanel headerPanel = createHeaderPanel();
		add(headerPanel, BorderLayout.NORTH);

		// Add course info section
		JPanel courseInfoPanel = createCourseInfoPanel();
		add(courseInfoPanel, BorderLayout.WEST);

		// Create center panel for table and summary (stacked vertically)
		JPanel centerPanel = new JPanel(new BorderLayout(0, AppTheme.CONTENT_PAD));
		centerPanel.setBackground(AppTheme.WHITE);
		centerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
			AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD
		));

		// Add table panel (scrollable)
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(AppTheme.WHITE);
		centerPanel.add(tablePanel, BorderLayout.CENTER);

		// Add summary and signature panel at bottom
		JPanel footerPanel = createFooterPanel();
		centerPanel.add(footerPanel, BorderLayout.SOUTH);

		add(centerPanel, BorderLayout.CENTER);
		
		// Load courses into dropdown
		loadCourses();
	}

	/**
	 * Create header section with title and selector
	 */
	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel();
		AppTheme.stylePanelMuted(headerPanel);
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		headerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
			AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD
		));

		// Title
		JLabel titleLabel = new JLabel("Class List");
		titleLabel.setFont(AppTheme.FONT_HEADING);
		titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
		headerPanel.add(titleLabel);
		headerPanel.add(Box.createVerticalStrut(12));

		// Selector row
		JPanel selectorPanel = new JPanel();
		selectorPanel.setOpaque(false);
		selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.X_AXIS));

		JLabel courseLabel = new JLabel("Select Course:");
		courseLabel.setFont(AppTheme.FONT_LABEL);
		courseLabel.setForeground(AppTheme.TEXT_PRIMARY);
		selectorPanel.add(courseLabel);
		selectorPanel.add(Box.createHorizontalStrut(8));

		courseDropdown = new JComboBox<>();
		courseDropdown.setFont(AppTheme.FONT_TABLE);
		courseDropdown.setPreferredSize(new java.awt.Dimension(300, 28));
		selectorPanel.add(courseDropdown);
		selectorPanel.add(Box.createHorizontalStrut(12));

		viewButton = new JButton("View");
		AppTheme.stylePrimaryButton(viewButton);
		viewButton.addActionListener(e -> handleViewClicked());
		selectorPanel.add(viewButton);
		selectorPanel.add(Box.createHorizontalGlue());

		headerPanel.add(selectorPanel);

		return headerPanel;
	}

	/**
	 * Create course info section
	 */
	private JPanel createCourseInfoPanel() {
		JPanel infoPanel = new JPanel();
		AppTheme.stylePanel(infoPanel);
		infoPanel.setLayout(new GridBagLayout());
		infoPanel.setPreferredSize(new java.awt.Dimension(300, 0));
		infoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
			AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD
		));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 8, 4, 8);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		int gridy = 0;

		// Institution
		JLabel instLabel = new JLabel("Institution:");
		instLabel.setFont(AppTheme.FONT_LABEL);
		gbc.gridy = gridy++;
		infoPanel.add(instLabel, gbc);
		institutionLabel = createDisplayLabel("Technological University of the Philippines");
		gbc.gridy = gridy++;
		infoPanel.add(institutionLabel, gbc);

		// College
		JLabel collegeHeaderLabel = new JLabel("College:");
		collegeHeaderLabel.setFont(AppTheme.FONT_LABEL);
		gbc.gridy = gridy++;
		infoPanel.add(collegeHeaderLabel, gbc);
		collegeLabel = createDisplayLabel("");
		gbc.gridy = gridy++;
		infoPanel.add(collegeLabel, gbc);

		// Subject Code
		JLabel codeLabel = new JLabel("Subject Code:");
		codeLabel.setFont(AppTheme.FONT_LABEL);
		gbc.gridy = gridy++;
		infoPanel.add(codeLabel, gbc);
		subjectCodeLabel = createDisplayLabel("");
		gbc.gridy = gridy++;
		infoPanel.add(subjectCodeLabel, gbc);

		// Descriptive Title
		JLabel titleLabel = new JLabel("Descriptive Title:");
		titleLabel.setFont(AppTheme.FONT_LABEL);
		gbc.gridy = gridy++;
		infoPanel.add(titleLabel, gbc);
		descriptiveTitleLabel = createDisplayLabel("");
		gbc.gridy = gridy++;
		infoPanel.add(descriptiveTitleLabel, gbc);

		// Units
		JLabel unitsHeaderLabel = new JLabel("Units:");
		unitsHeaderLabel.setFont(AppTheme.FONT_LABEL);
		gbc.gridy = gridy++;
		infoPanel.add(unitsHeaderLabel, gbc);
		unitsLabel = createDisplayLabel("");
		gbc.gridy = gridy++;
		infoPanel.add(unitsLabel, gbc);

		// Course/Year/Section
		JLabel courseYearLabel = new JLabel("Course/Year/Section:");
		courseYearLabel.setFont(AppTheme.FONT_LABEL);
		gbc.gridy = gridy++;
		infoPanel.add(courseYearLabel, gbc);
		courseYearSectionLabel = createDisplayLabel("");
		gbc.gridy = gridy++;
		infoPanel.add(courseYearSectionLabel, gbc);

		// Term/School Year
		JLabel termLabel = new JLabel("Term/School Year:");
		termLabel.setFont(AppTheme.FONT_LABEL);
		gbc.gridy = gridy++;
		infoPanel.add(termLabel, gbc);
		termSchoolYearLabel = createDisplayLabel("");
		gbc.gridy = gridy++;
		infoPanel.add(termSchoolYearLabel, gbc);

		// Days
		JLabel daysLabel2 = new JLabel("Days:");
		daysLabel2.setFont(AppTheme.FONT_LABEL);
		gbc.gridy = gridy++;
		infoPanel.add(daysLabel2, gbc);
		daysLabel = createDisplayLabel("");
		gbc.gridy = gridy++;
		infoPanel.add(daysLabel, gbc);

		// Schedule/Time
		JLabel scheduleLabel2 = new JLabel("Schedule:");
		scheduleLabel2.setFont(AppTheme.FONT_LABEL);
		gbc.gridy = gridy++;
		infoPanel.add(scheduleLabel2, gbc);
		scheduleLabel = createDisplayLabel("");
		gbc.gridy = gridy++;
		infoPanel.add(scheduleLabel, gbc);

		// Room
		JLabel roomLabel2 = new JLabel("Room:");
		roomLabel2.setFont(AppTheme.FONT_LABEL);
		gbc.gridy = gridy++;
		infoPanel.add(roomLabel2, gbc);
		roomLabel = createDisplayLabel("");
		gbc.gridy = gridy++;
		infoPanel.add(roomLabel, gbc);

		// Instructor
		JLabel instructorLabel2 = new JLabel("Instructor:");
		instructorLabel2.setFont(AppTheme.FONT_LABEL);
		gbc.gridy = gridy++;
		infoPanel.add(instructorLabel2, gbc);
		instructorLabel = createDisplayLabel("");
		gbc.gridy = gridy++;
		gbc.weighty = 1.0;
		infoPanel.add(instructorLabel, gbc);

		return infoPanel;
	}

	/**
	 * Create footer section with summary and signature lines
	 */
	private JPanel createFooterPanel() {
		JPanel footerPanel = new JPanel();
		AppTheme.stylePanelMuted(footerPanel);
		footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));
		footerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
			AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD
		));

		// Divider
		footerPanel.add(new javax.swing.JSeparator());
		footerPanel.add(Box.createVerticalStrut(8));

		// Nothing Follows line
		JLabel nothingLabel = new JLabel("xxxxxxxxxx Nothing Follows xxxxxxxxxx");
		nothingLabel.setFont(AppTheme.FONT_SMALL);
		nothingLabel.setForeground(AppTheme.TEXT_MUTED);
		nothingLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		footerPanel.add(nothingLabel);
		footerPanel.add(Box.createVerticalStrut(12));

		// Summary section
		JPanel summaryGrid = new JPanel();
		summaryGrid.setOpaque(false);
		summaryGrid.setLayout(new BoxLayout(summaryGrid, BoxLayout.Y_AXIS));

		totalLabel = new JLabel("No. of Students Enrolled: 0");
		totalLabel.setFont(AppTheme.FONT_TABLE);
		totalLabel.setForeground(AppTheme.TEXT_PRIMARY);
		summaryGrid.add(totalLabel);

		passedLabel = new JLabel("No. of Students Passed: 0");
		passedLabel.setFont(AppTheme.FONT_TABLE);
		passedLabel.setForeground(AppTheme.TEXT_PRIMARY);
		summaryGrid.add(passedLabel);

		failedLabel = new JLabel("No. of Students Failed: 0");
		failedLabel.setFont(AppTheme.FONT_TABLE);
		failedLabel.setForeground(AppTheme.TEXT_PRIMARY);
		summaryGrid.add(failedLabel);

		incompleteLabel = new JLabel("No. of Students Incomplete: 0");
		incompleteLabel.setFont(AppTheme.FONT_TABLE);
		incompleteLabel.setForeground(AppTheme.TEXT_PRIMARY);
		summaryGrid.add(incompleteLabel);

		droppedLabel = new JLabel("No. of Students Dropped: 0");
		droppedLabel.setFont(AppTheme.FONT_TABLE);
		droppedLabel.setForeground(AppTheme.TEXT_PRIMARY);
		summaryGrid.add(droppedLabel);

		footerPanel.add(summaryGrid);
		footerPanel.add(Box.createVerticalStrut(16));

		// Signature lines
		JPanel signaturesPanel = new JPanel();
		signaturesPanel.setOpaque(false);
		signaturesPanel.setLayout(new BoxLayout(signaturesPanel, BoxLayout.Y_AXIS));

		addSignatureLine(signaturesPanel, "Instructor/Professor");
		signaturesPanel.add(Box.createVerticalStrut(8));
		addSignatureLine(signaturesPanel, "Department Head");
		signaturesPanel.add(Box.createVerticalStrut(8));
		addSignatureLine(signaturesPanel, "Dean of College");

		footerPanel.add(signaturesPanel);

		return footerPanel;
	}

	/**
	 * Add a signature line to the signature panel
	 */
	private void addSignatureLine(JPanel parent, String title) {
		JPanel linePanel = new JPanel();
		linePanel.setOpaque(false);
		linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.X_AXIS));

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(AppTheme.FONT_TABLE);
		titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
		titleLabel.setPreferredSize(new java.awt.Dimension(150, 20));
		linePanel.add(titleLabel);

		JLabel lineLabel = new JLabel(
			"_________________________________     Date: _______________"
		);
		lineLabel.setFont(AppTheme.FONT_SMALL);
		lineLabel.setForeground(AppTheme.TEXT_MUTED);
		linePanel.add(lineLabel);

		parent.add(linePanel);
	}

	/**
	 * Create a styled display label
	 */
	private JLabel createDisplayLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(AppTheme.FONT_TABLE);
		label.setForeground(AppTheme.TEXT_PRIMARY);
		return label;
	}

	/**
	 * Load courses from CourseDAO into dropdown
	 */
	private void loadCourses() {
		try {
			courseList = courseDAO.getAllCourses();
			courseDropdown.removeAllItems();
			
			if (courseList != null && !courseList.isEmpty()) {
				for (Course course : courseList) {
					courseDropdown.addItem(course.getCourseId() + " - " + course.getCourseName());
				}
			} else {
				courseDropdown.addItem("No courses available");
			}
		} catch (Exception e) {
			System.err.println("[ClassListPanel] Error loading courses: " + e.getMessage());
			e.printStackTrace();
			courseDropdown.addItem("Error loading courses");
		}
	}

	/**
	 * Handle View button click
	 */
	private void handleViewClicked() {
		if (courseDropdown.getSelectedIndex() < 0 || courseList == null || courseList.isEmpty()) {
			JOptionPane.showMessageDialog(
				this, "Please select a course", "Selection Required",
				JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		Course selectedCourse = courseList.get(courseDropdown.getSelectedIndex());
		displayClassList(selectedCourse.getCourseId());
	}

	/**
	 * Load and display class list for a course
	 */
	/**
	 * Refresh dropdown data when panel becomes visible
	 */
	public void refreshData() {
		loadCourses();
	}

	private void displayClassList(int courseId) {
		try {
			// Call ClassListService to get class list data
			ClassListService.ClassListResult result = classListService.getClassList(courseId);
			
			if (result == null || result.students.isEmpty()) {
				JOptionPane.showMessageDialog(
					this, "No student data found for this course", 
					"No Data", JOptionPane.INFORMATION_MESSAGE
				);
				refreshDisplay();
				return;
			}
			
			// Populate course info labels
			updateCourseInfo(result);
			
			// Populate table with student records
			updateStudentTable(result);
			
			// Update summary counts
			updateSummary(result);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
				this, "Error loading class list: " + e.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE
			);
			e.printStackTrace();
		}
	}

	/**
	 * Update course info labels
	 */
	private void updateCourseInfo(ClassListService.ClassListResult result) {
		collegeLabel.setText(result.college != null ? result.college : "N/A");
		subjectCodeLabel.setText(result.courseName != null ? result.courseName : "N/A");
		descriptiveTitleLabel.setText(result.descriptiveTitle != null ? result.descriptiveTitle : "N/A");
		unitsLabel.setText(String.valueOf(result.units));
		courseYearSectionLabel.setText("N/A");
		termSchoolYearLabel.setText(result.term != null ? (result.term + ", " + result.schoolYear) : "N/A");
		daysLabel.setText(result.days != null ? result.days : "N/A");
		scheduleLabel.setText(result.time != null ? result.time : "N/A");
		roomLabel.setText("N/A");
		instructorLabel.setText(result.instructor != null ? result.instructor : "N/A");
	}

	/**
	 * Update student table with enrolled students
	 */
	private void updateStudentTable(ClassListService.ClassListResult result) {
		String[] columnNames = { "No.", "Student ID", "Student Name", "Grade", "Units", "Degree" };
		Object[][] data = new Object[result.students.size()][6];
		
		// Populate data array from student records
		for (int i = 0; i < result.students.size(); i++) {
			ClassListService.StudentEnrollment student = result.students.get(i);
			data[i][0] = i + 1;
			data[i][1] = student.studentId;
			data[i][2] = student.studentName;
			data[i][3] = student.grade != null ? String.format("%.2f", student.grade) : "N/A";
			data[i][4] = student.units;
			data[i][5] = student.degree != null ? student.degree : "N/A";
		}

		// Create table using TableUtils
		studentTable = TableUtils.createTable(columnNames, data);

		// Replace or update tablePanel
		tablePanel.removeAll();
		JScrollPane scrollPane = new JScrollPane(studentTable);
		scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(AppTheme.BORDER, 1));
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		tablePanel.revalidate();
		tablePanel.repaint();
	}

	/**
	 * Update summary counts
	 */
	private void updateSummary(ClassListService.ClassListResult result) {
		totalLabel.setText("No. of Students Enrolled: " + result.totalStudents);
		passedLabel.setText("No. of Students Passed: " + result.passedCount);
		failedLabel.setText("No. of Students Failed: " + result.failedCount);
		incompleteLabel.setText("No. of Students Incomplete: " + result.pendingGradeCount);
		droppedLabel.setText("No. of Students Dropped: 0");
	}
	
	/**
	 * Clear all display fields
	 */
	private void refreshDisplay() {
		collegeLabel.setText("");
		subjectCodeLabel.setText("");
		descriptiveTitleLabel.setText("");
		unitsLabel.setText("");
		courseYearSectionLabel.setText("");
		termSchoolYearLabel.setText("");
		daysLabel.setText("");
		scheduleLabel.setText("");
		roomLabel.setText("");
		instructorLabel.setText("");
		
		totalLabel.setText("No. of Students Enrolled: 0");
		passedLabel.setText("No. of Students Passed: 0");
		failedLabel.setText("No. of Students Failed: 0");
		incompleteLabel.setText("No. of Students Incomplete: 0");
		droppedLabel.setText("No. of Students Dropped: 0");
		
		tablePanel.removeAll();
		tablePanel.revalidate();
		tablePanel.repaint();
	}
}
