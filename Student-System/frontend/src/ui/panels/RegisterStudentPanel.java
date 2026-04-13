package ui.panels;

import service.StudentService;
import ui.AppTheme;
import ui.components.FormUtils;
import utils.Validator;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

/**
 * Register Student Panel
 *
 * Responsibility: Form UI for registering a new student
 * - Displays form fields for student data entry:
 *   * Full Name
 *   * Full Address
 *   * Birthdate
 *   * Place of Birth
 *   * Degree
 *   * Major
 * - Includes Submit button
 * - Calls StudentService.insertStudent() on submit
 * - Shows confirmation dialog on success
 * FE2 - Student Forms (Input)
 */
public class RegisterStudentPanel extends JPanel {

	private JTextField fullNameField;
	private JTextField fullAddressField;
	private JTextField birthdateField;
	private JTextField placeOfBirthField;
	private JTextField degreeField;
	private JTextField majorField;

	private StudentService studentService;

	/**
	 * Constructor - Initialize the panel
	 */
	public RegisterStudentPanel() {
		this.studentService = new StudentService();
		setBackground(AppTheme.WHITE);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPadding();
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
	 * Initialize all form components
	 */
	private void initializeComponents() {
		// Title
		JLabel titleLabel = new JLabel("Register New Student");
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
		formPanel.setMaximumSize(new Dimension(500, 400));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 0, 8, 16);
		gbc.anchor = GridBagConstraints.WEST;

		// Full Name
		gbc.gridy = 0;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Full Name:"), gbc);
		fullNameField = FormUtils.createTextField("");
		gbc.gridx = 1;
		formPanel.add(fullNameField, gbc);

		// Full Address
		gbc.gridy = 1;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Full Address:"), gbc);
		fullAddressField = FormUtils.createTextField("");
		gbc.gridx = 1;
		formPanel.add(fullAddressField, gbc);

		// Birthdate (yyyy-MM-dd format)
		gbc.gridy = 2;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Birthdate (yyyy-MM-dd):"), gbc);
		birthdateField = FormUtils.createTextField("");
		gbc.gridx = 1;
		formPanel.add(birthdateField, gbc);

		// Place of Birth
		gbc.gridy = 3;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Place of Birth:"), gbc);
		placeOfBirthField = FormUtils.createTextField("");
		gbc.gridx = 1;
		formPanel.add(placeOfBirthField, gbc);

		// Degree
		gbc.gridy = 4;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Degree:"), gbc);
		degreeField = FormUtils.createTextField("");
		gbc.gridx = 1;
		formPanel.add(degreeField, gbc);

		// Major
		gbc.gridy = 5;
		gbc.gridx = 0;
		formPanel.add(FormUtils.createLabel("Major:"), gbc);
		majorField = FormUtils.createTextField("");
		gbc.gridx = 1;
		formPanel.add(majorField, gbc);

		add(formPanel);
		add(Box.createVerticalStrut(16));

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(AppTheme.WHITE);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 0));
		buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

		JButton submitBtn = new JButton("Register");
		AppTheme.stylePrimaryButton(submitBtn);
		submitBtn.addActionListener(e -> registerStudent());

		JButton clearBtn = FormUtils.createClearButton();
		clearBtn.addActionListener(e -> clearForm());

		buttonPanel.add(submitBtn);
		buttonPanel.add(clearBtn);

		add(buttonPanel);
		add(Box.createVerticalGlue());
	}

	/**
	 * Validate and register a student
	 */
	private void registerStudent() {
		String fullName = fullNameField.getText();
		String fullAddress = fullAddressField.getText();
		String birthdateStr = birthdateField.getText();
		String placeOfBirth = placeOfBirthField.getText();
		String degree = degreeField.getText();
		String major = majorField.getText();

		// Validation
		if (!Validator.isNotEmpty(fullName)) {
			JOptionPane.showMessageDialog(this, "Full Name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!Validator.isNotEmpty(fullAddress)) {
			JOptionPane.showMessageDialog(this, "Full Address is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!Validator.isValidDateFormat(birthdateStr)) {
			JOptionPane.showMessageDialog(this, "Birthdate must be in format yyyy-MM-dd", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!Validator.isNotEmpty(placeOfBirth)) {
			JOptionPane.showMessageDialog(this, "Place of Birth is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!Validator.isNotEmpty(degree)) {
			JOptionPane.showMessageDialog(this, "Degree is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!Validator.isNotEmpty(major)) {
			JOptionPane.showMessageDialog(this, "Major is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Parse date
		Date birthdate = Validator.parseDate(birthdateStr);
		if (birthdate == null) {
			JOptionPane.showMessageDialog(this, "Invalid date format", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			// Call service to insert student
			studentService.insertStudent(fullName, fullAddress, birthdate, placeOfBirth, degree, major);
			
			JOptionPane.showMessageDialog(this, "Student Registered Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
			clearForm();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error registering student: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	/**
	 * Clear all form fields
	 */
	private void clearForm() {
		fullNameField.setText("");
		fullAddressField.setText("");
		birthdateField.setText("");
		placeOfBirthField.setText("");
		degreeField.setText("");
		majorField.setText("");
	}
}
