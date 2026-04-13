package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Manager
 * 
 * Responsibility: Return a live JDBC Connection to PostgreSQL
 * 
 * BE1 - Database Setup & Connection
 */
public class DBConnection {

	// Database connection parameters
	private static final String URL = "jdbc:postgresql://localhost:5432/school1_db";
	private static final String USER = "postgres";
	private static final String PASSWORD = "Zqxwce1234";
	private static final String DRIVER = "org.postgresql.Driver";

	static {
		// Load PostgreSQL JDBC driver
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("[DBConnection] PostgreSQL JDBC Driver not found!");
			e.printStackTrace();
		}
	}

	/**
	 * Get a connection to the database
	 *
	 * @return a live JDBC Connection to PostgreSQL
	 * @throws SQLException if connection fails
	 */
	public static Connection getConnection() throws SQLException {
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
			return conn;
		} catch (SQLException e) {
			System.err.println("[DBConnection] Failed to connect to database");
			System.err.println("URL: " + URL);
			System.err.println("User: " + USER);
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Test the database connection
	 * 
	 * @return true if connection successful, false otherwise
	 */
	public static boolean testConnection() {
		try (Connection conn = getConnection()) {
			if (conn != null && !conn.isClosed()) {
				System.out.println("[DBConnection] Database connection test PASSED");
				return true;
			}
		} catch (SQLException e) {
			System.err.println("[DBConnection] Database connection test FAILED");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Main method for testing database connection
	 */
	public static void main(String[] args) {
		System.out.println("Testing database connection...");
		boolean success = testConnection();
		if (success) {
			System.out.println("SUCCESS: Database is ready!");
		} else {
			System.out.println("FAILED: Cannot connect to database.");
		}
	}
}
