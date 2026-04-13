package utils;

import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Validator
 *
 * Responsibility: Input validation for forms
 * - Validates empty fields
 * - Validates date formats
 * - Validates grade range (1.0 to 5.0)
 * - Provides static validation methods called by UI panels
 * FE2 - Student Forms (Input)
 */
public class Validator {

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * Validate that a string is not empty
	 *
	 * @param value the string to validate
	 * @return true if not empty, false otherwise
	 */
	public static boolean isNotEmpty(String value) {
		return value != null && !value.trim().isEmpty();
	}

	/**
	 * Validate that a date is in valid format
	 *
	 * @param dateString the date string to validate (format: yyyy-MM-dd)
	 * @return true if valid format, false otherwise
	 */
	public static boolean isValidDateFormat(String dateString) {
		if (!isNotEmpty(dateString)) {
			return false;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			sdf.setLenient(false);
			sdf.parse(dateString);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * Validate that a grade is in valid range (1.0 to 5.0)
	 *
	 * @param grade the grade to validate
	 * @return true if 1.0 <= grade <= 5.0, false otherwise
	 */
	public static boolean isValidGrade(double grade) {
		return grade >= 1.0 && grade <= 5.0;
	}

	/**
	 * Parse date string to java.util.Date
	 *
	 * @param dateString the date string (format: yyyy-MM-dd)
	 * @return java.util.Date object or null if parse fails
	 */
	public static java.util.Date parseDate(String dateString) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			sdf.setLenient(false);
			return sdf.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Validate that a string is a valid integer
	 *
	 * @param value the string to validate
	 * @return true if valid integer, false otherwise
	 */
	public static boolean isValidInteger(String value) {
		if (!isNotEmpty(value)) {
			return false;
		}
		try {
			Integer.parseInt(value.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Validate that a string is a valid double
	 *
	 * @param value the string to validate
	 * @return true if valid double, false otherwise
	 */
	public static boolean isValidDouble(String value) {
		if (!isNotEmpty(value)) {
			return false;
		}
		try {
			Double.parseDouble(value.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Validate all required student registration fields
	 *
	 * @param fullName the student's full name
	 * @param address the student's address
	 * @param birthdate birthdate string (yyyy-MM-dd format)
	 * @param placeOfBirth the place of birth
	 * @param degree the degree
	 * @param major the major
	 * @return true if all fields valid, false otherwise
	 */
	public static boolean validateStudentRegistration(String fullName, String address,
													   String birthdate, String placeOfBirth,
													   String degree, String major) {
		// TODO: Implement student validation
		// - Check all fields are not empty using isNotEmpty()
		// - Check birthdate is valid format using isValidDateFormat()
		// - Return true if all valid, false otherwise
		return false;
	}

	// TODO: Add other validation methods as needed (e.g., validateEnrollment, validateGrade, etc.)
}
