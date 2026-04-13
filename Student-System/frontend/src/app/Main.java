package app;

import ui.MainFrame;

/**
 * Main entry point for the Student Information System application.
 *
 * Responsibility: Launch the application and create the MainFrame
 */
public class Main {

	public static void main(String[] args) {
		// Run on Event Dispatch Thread for thread safety
		javax.swing.SwingUtilities.invokeLater(() -> {
			// Create and display the main application window
			MainFrame frame = new MainFrame();
			frame.setVisible(true);
		});
	}
}
