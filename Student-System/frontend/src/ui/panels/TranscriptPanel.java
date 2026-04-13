package ui.panels;

import ui.AppTheme;
import ui.components.TableUtils;
import service.StudentService;
import service.TranscriptService;
import model.Student;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.JOptionPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

/**
 * Transcript Panel — FE3
 *
 * Responsibility: Display Transcript of Records (TOR) for a student
 * - Displays form fields:
 *   * Student ID (dropdown - loads all students)
 *   * View button
 * - When student is selected and View clicked:
 *   * Top section: JLabel fields displaying student personal info
 *     - Name, Address, Date of Birth, Place of Birth, Degree, Major
 *     - Date Conferred, Category, Educational Attainment, Date Admitted
 *   * Table section: JTable with student's course enrollments
 *     - Columns: Term | Course Name | Descriptive Title | Final Grade | Units
 */
public class TranscriptPanel extends JPanel {

	private JTable enrollmentTable;
	private JComboBox<String> studentDropdown;
	private JButton viewButton;
	
	// Services
	private StudentService studentService;
	private TranscriptService transcriptService;
	private List<Student> studentList;
	
	// Student info labels
	private JLabel nameLabel;
	private JLabel addressLabel;
	private JLabel birthdateLabel;
	private JLabel placeOfBirthLabel;
	private JLabel degreeLabel;
	private JLabel majorLabel;
	private JLabel dateConferredLabel;
	private JLabel categoryLabel;
	private JLabel educationalAttainmentLabel;
	private JLabel dateAdmittedLabel;
	
	private JPanel tablePanel;

	/**
	 * Constructor - Initialize the panel
	 */
	public TranscriptPanel() {
		// Initialize services
		this.studentService = new StudentService();
		this.transcriptService = new TranscriptService();
		
		// Set panel styling
		AppTheme.stylePanel(this);
		setLayout(new BorderLayout(0, AppTheme.CONTENT_PAD));

		// Add header
		JPanel headerPanel = createHeaderPanel();
		add(headerPanel, BorderLayout.NORTH);

		// Add info section (student details)
		JPanel infoPanel = createInfoPanel();
		add(infoPanel, BorderLayout.CENTER);

		// Add table panel with scrollable table
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBackground(AppTheme.WHITE);
		add(tablePanel, BorderLayout.SOUTH);
		
		// Load students into dropdown
		loadStudents();
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
		JLabel titleLabel = new JLabel("Transcript of Records (TOR)");
		titleLabel.setFont(AppTheme.FONT_HEADING);
		titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
		headerPanel.add(titleLabel);
		headerPanel.add(Box.createVerticalStrut(12));

		// Selector row
		JPanel selectorPanel = new JPanel();
		selectorPanel.setOpaque(false);
		selectorPanel.setLayout(new BoxLayout(selectorPanel, BoxLayout.X_AXIS));

		JLabel studentLabel = new JLabel("Select Student:");
		studentLabel.setFont(AppTheme.FONT_LABEL);
		studentLabel.setForeground(AppTheme.TEXT_PRIMARY);
		selectorPanel.add(studentLabel);
		selectorPanel.add(Box.createHorizontalStrut(8));

		studentDropdown = new JComboBox<>();
		studentDropdown.setFont(AppTheme.FONT_TABLE);
		studentDropdown.setPreferredSize(new java.awt.Dimension(250, 28));
		selectorPanel.add(studentDropdown);
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
	 * Create student info section
	 */
	private JPanel createInfoPanel() {
		JPanel infoPanel = new JPanel();
		AppTheme.stylePanel(infoPanel);
		infoPanel.setLayout(new GridBagLayout());
		infoPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
			AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD, AppTheme.CONTENT_PAD
		));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 12, 6, 12);
		gbc.anchor = GridBagConstraints.WEST;

		// Row 1: Name and Address
		gbc.gridx = 0;
		gbc.gridy = 0;
		infoPanel.add(new JLabel("Name:"), gbc);
		nameLabel = createDisplayLabel("");
		gbc.gridx = 1;
		infoPanel.add(nameLabel, gbc);

		gbc.gridx = 2;
		infoPanel.add(new JLabel("Address:"), gbc);
		addressLabel = createDisplayLabel("");
		gbc.gridx = 3;
		gbc.weightx = 1.0;
		infoPanel.add(addressLabel, gbc);

		// Row 2: Birthdate and Place of Birth
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		infoPanel.add(new JLabel("Date of Birth:"), gbc);
		birthdateLabel = createDisplayLabel("");
		gbc.gridx = 1;
		infoPanel.add(birthdateLabel, gbc);

		gbc.gridx = 2;
		infoPanel.add(new JLabel("Place of Birth:"), gbc);
		placeOfBirthLabel = createDisplayLabel("");
		gbc.gridx = 3;
		gbc.weightx = 1.0;
		infoPanel.add(placeOfBirthLabel, gbc);

		// Row 3: Degree and Major
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0;
		infoPanel.add(new JLabel("Degree:"), gbc);
		degreeLabel = createDisplayLabel("");
		gbc.gridx = 1;
		infoPanel.add(degreeLabel, gbc);

		gbc.gridx = 2;
		infoPanel.add(new JLabel("Major:"), gbc);
		majorLabel = createDisplayLabel("");
		gbc.gridx = 3;
		gbc.weightx = 1.0;
		infoPanel.add(majorLabel, gbc);

		// Row 4: Date Conferred and Category
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0;
		infoPanel.add(new JLabel("Date Conferred:"), gbc);
		dateConferredLabel = createDisplayLabel("");
		gbc.gridx = 1;
		infoPanel.add(dateConferredLabel, gbc);

		gbc.gridx = 2;
		infoPanel.add(new JLabel("Category:"), gbc);
		categoryLabel = createDisplayLabel("");
		gbc.gridx = 3;
		gbc.weightx = 1.0;
		infoPanel.add(categoryLabel, gbc);

		// Row 5: Educational Attainment and Date Admitted
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 0;
		infoPanel.add(new JLabel("Educational Attainment:"), gbc);
		educationalAttainmentLabel = createDisplayLabel("");
		gbc.gridx = 1;
		infoPanel.add(educationalAttainmentLabel, gbc);

		gbc.gridx = 2;
		infoPanel.add(new JLabel("Date Admitted:"), gbc);
		dateAdmittedLabel = createDisplayLabel("");
		gbc.gridx = 3;
		gbc.weightx = 1.0;
		infoPanel.add(dateAdmittedLabel, gbc);

		return infoPanel;
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
	 * Handle View button click
	 */
	private void handleViewClicked() {
		if (studentDropdown.getSelectedIndex() < 0 || studentList == null || studentList.isEmpty()) {
			JOptionPane.showMessageDialog(
				this, "Please select a student", "Selection Required", 
				JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		Student selectedStudent = studentList.get(studentDropdown.getSelectedIndex());
		displayTranscript(selectedStudent.getStudentId());
	}

	/**
	 * Load students from StudentService into dropdown
	 */
	private void loadStudents() {
		try {
			studentList = studentService.getAllStudents();
			studentDropdown.removeAllItems();
			
			if (studentList != null && !studentList.isEmpty()) {
				for (Student student : studentList) {
					studentDropdown.addItem(student.getStudentId() + " - " + student.getFullName());
				}
			} else {
				studentDropdown.addItem("No students available");
			}
		} catch (Exception e) {
			System.err.println("[TranscriptPanel] Error loading students: " + e.getMessage());
			e.printStackTrace();
			studentDropdown.addItem("Error loading students");
		}
	}

	/**
	 * Load and display transcript for a student
	 */
	private void displayTranscript(int studentId) {
		try {
			// Call TranscriptService to get transcript data
			TranscriptService.TranscriptResult result = transcriptService.getTranscript(studentId);
			
			if (result == null || result.enrollments.isEmpty()) {
				JOptionPane.showMessageDialog(
					this, "No transcript data found for this student", 
					"No Data", JOptionPane.INFORMATION_MESSAGE
				);
				clearDisplay();
				return;
			}
			
			// Populate student info labels
			updateStudentInfo(result);
			
			// Populate table with enrollment records
			updateEnrollmentTable(result);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(
				this, "Error loading transcript: " + e.getMessage(),
				"Error", JOptionPane.ERROR_MESSAGE
			);
			e.printStackTrace();
		}
	}

	/**
	 * Update student info labels
	 */
	private void updateStudentInfo(TranscriptService.TranscriptResult result) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		nameLabel.setText(result.fullName != null ? result.fullName : "N/A");
		addressLabel.setText(result.fullAddress != null ? result.fullAddress : "N/A");
		birthdateLabel.setText(result.birthdate != null ? sdf.format(result.birthdate) : "N/A");
		placeOfBirthLabel.setText(result.placeOfBirth != null ? result.placeOfBirth : "N/A");
		degreeLabel.setText(result.degree != null ? result.degree : "N/A");
		majorLabel.setText(result.major != null ? result.major : "N/A");
		dateConferredLabel.setText("N/A");  // Not in TranscriptResult - could be added later
		categoryLabel.setText("N/A");       // Not in TranscriptResult - could be added later
		educationalAttainmentLabel.setText("N/A");  // Not in TranscriptResult
		dateAdmittedLabel.setText("N/A");  // Not in TranscriptResult
	}

	/**
	 * Clear all display fields
	 */
	private void clearDisplay() {
		nameLabel.setText("");
		addressLabel.setText("");
		birthdateLabel.setText("");
		placeOfBirthLabel.setText("");
		degreeLabel.setText("");
		majorLabel.setText("");
		dateConferredLabel.setText("");
		categoryLabel.setText("");
		educationalAttainmentLabel.setText("");
		dateAdmittedLabel.setText("");
		
		tablePanel.removeAll();
		tablePanel.revalidate();
		tablePanel.repaint();
	}

	/**
	 * Update enrollment table with student's courses
	 */
	private void updateEnrollmentTable(TranscriptService.TranscriptResult result) {
		String[] columnNames = { "Term", "Course Name", "Descriptive Title", "Grade", "Units", "Status" };
		Object[][] data = new Object[result.enrollments.size()][6];
		
		// Populate data array from enrollment records
		for (int i = 0; i < result.enrollments.size(); i++) {
			TranscriptService.EnrollmentRecord record = result.enrollments.get(i);
			data[i][0] = record.term != null ? record.term : "N/A";
			data[i][1] = record.courseName != null ? record.courseName : "N/A";
			data[i][2] = record.descriptiveTitle != null ? record.descriptiveTitle : "N/A";
			data[i][3] = record.grade != null ? String.format("%.2f", record.grade) : "N/A";
			data[i][4] = record.units;
			data[i][5] = record.status != null ? record.status : "N/A";
		}

		// Create table using TableUtils
		enrollmentTable = TableUtils.createTable(columnNames, data);

		// Replace or update tablePanel
		tablePanel.removeAll();
		JScrollPane scrollPane = new JScrollPane(enrollmentTable);
		scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(AppTheme.BORDER, 1));
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		
		// Add summary section
		JPanel summaryPanel = createSummaryPanel(result);
		tablePanel.add(summaryPanel, BorderLayout.SOUTH);
		
		tablePanel.revalidate();
		tablePanel.repaint();
	}
	
	/**
	 * Create summary statistics panel
	 */
	private JPanel createSummaryPanel(TranscriptService.TranscriptResult result) {
		JPanel summaryPanel = new JPanel();
		AppTheme.stylePanelMuted(summaryPanel);
		summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.X_AXIS));
		summaryPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 16, 12, 16));
		
		JLabel totalUnitsLabel = new JLabel("Total Units: " + result.totalUnits);
		totalUnitsLabel.setFont(AppTheme.FONT_LABEL);
		totalUnitsLabel.setForeground(AppTheme.TEXT_PRIMARY);
		
		JLabel earnedUnitsLabel = new JLabel("Earned Units: " + result.earnedUnits);
		earnedUnitsLabel.setFont(AppTheme.FONT_LABEL);
		earnedUnitsLabel.setForeground(AppTheme.TEXT_PRIMARY);
		
		JLabel gpaLabel = new JLabel("GPA: " + String.format("%.2f", result.gpa));
		gpaLabel.setFont(AppTheme.FONT_LABEL);
		gpaLabel.setForeground(AppTheme.TEXT_PRIMARY);
		
		summaryPanel.add(totalUnitsLabel);
		summaryPanel.add(Box.createHorizontalStrut(24));
		summaryPanel.add(earnedUnitsLabel);
		summaryPanel.add(Box.createHorizontalStrut(24));
		summaryPanel.add(gpaLabel);
		summaryPanel.add(Box.createHorizontalGlue());
		
		return summaryPanel;
	}
}
