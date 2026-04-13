package ui.components;

import ui.AppTheme;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Dimension;

/**
 * Form Utilities
 *
 * Responsibility: Reusable JLabel + JTextField factory methods
 * - Provides static methods to create consistent form fields
 * - Handles layout and styling of form components
 * FE2 - Student Forms (Input)
 */
public class FormUtils {

	/**
	 * Create a label for form display
	 *
	 * @param labelText the label text
	 * @return JLabel configured with AppTheme styling
	 */
	public static JLabel createLabel(String labelText) {
		JLabel label = new JLabel(labelText);
		label.setFont(AppTheme.FONT_LABEL);
		label.setForeground(AppTheme.TEXT_PRIMARY);
		return label;
	}

	/**
	 * Create a text field for form input
	 *
	 * @param initialValue the initial value (empty string if null)
	 * @return JTextField configured with AppTheme styling
	 */
	public static JTextField createTextField(String initialValue) {
		JTextField field = new JTextField(initialValue != null ? initialValue : "");
		field.setFont(AppTheme.FONT_TABLE);
		field.setPreferredSize(new Dimension(200, 28));
		field.setForeground(AppTheme.TEXT_PRIMARY);
		field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
			javax.swing.BorderFactory.createLineBorder(AppTheme.BORDER, 1),
			javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));
		return field;
	}

	/**
	 * Create a label-textfield pair for form input
	 * Returns the textfield; caller must add both label and field to parent
	 *
	 * @param labelText the label text
	 * @param initialValue the initial value for the text field
	 * @return JTextField configured with label context
	 */
	public static JTextField createFormField(String labelText, String initialValue) {
		return createTextField(initialValue);
	}

	/**
	 * Create a display label (typically readonly)
	 *
	 * @param labelText the label text
	 * @param value the value to display
	 * @return JLabel configured as form display label
	 */
	public static JLabel createDisplayLabel(String labelText, String value) {
		JLabel label = new JLabel(labelText + ": " + (value != null ? value : ""));
		label.setFont(AppTheme.FONT_TABLE);
		label.setForeground(AppTheme.TEXT_PRIMARY);
		return label;
	}

	/**
	 * Create clear button styling
	 *
	 * @return JButton configured as a clear/reset button
	 */
	public static javax.swing.JButton createClearButton() {
		javax.swing.JButton button = new javax.swing.JButton("Clear");
		AppTheme.styleGhostButton(button);
		return button;
	}
}
