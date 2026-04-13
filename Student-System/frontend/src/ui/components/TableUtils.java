package ui.components;

import ui.AppTheme;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;

/**
 * Table Utilities
 *
 * Responsibility: Reusable JTable setup, column sizing, formatting
 * - Provides static methods to create and configure JTable instances
 * - Handles column sizing and formatting
 * - Provides helper methods for common table operations
 */
public class TableUtils {

	private static final int MIN_COLUMN_WIDTH = 60;
	private static final int MAX_COLUMN_WIDTH = 300;

	/**
	 * Create a formatted JTable with specified columns
	 *
	 * @param columnNames array of column names
	 * @param data 2D array of data
	 * @return configured JTable
	 */
	public static JTable createTable(String[] columnNames, Object[][] data) {
		// Create a read-only table model
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Make all cells read-only
			}
		};

		// Create JTable from model
		JTable table = new JTable(model);

		// Apply AppTheme styling
		AppTheme.styleTable(table);

		// Configure row height for better readability
		table.setRowHeight(28);

		// Set column widths with intelligent distribution
		autoSizeColumns(table);

		// Center-align numeric columns (Grade, Units, No.)
		centerAlignNumericColumns(table, columnNames);

		return table;
	}

	/**
	 * Auto-size all columns in a table based on content
	 *
	 * @param table the JTable to size
	 */
	public static void autoSizeColumns(JTable table) {
		if (table.getColumnCount() == 0) {
			return;
		}

		// Get content width by measuring renderer for each column
		for (int col = 0; col < table.getColumnCount(); col++) {
			int maxWidth = MIN_COLUMN_WIDTH;

			// Measure header width
			TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
			Component headerComp = headerRenderer.getTableCellRendererComponent(
				table, table.getColumnModel().getColumn(col).getHeaderValue(),
				false, false, 0, col
			);
			maxWidth = Math.max(maxWidth, headerComp.getPreferredSize().width);

			// Measure data cell widths (check first 100 rows for performance)
			int rowsToCheck = Math.min(100, table.getRowCount());
			for (int row = 0; row < rowsToCheck; row++) {
				TableCellRenderer cellRenderer = table.getCellRenderer(row, col);
				Object value = table.getValueAt(row, col);
				Component cellComp = cellRenderer.getTableCellRendererComponent(
					table, value, false, false, row, col
				);
				maxWidth = Math.max(maxWidth, cellComp.getPreferredSize().width);
			}

			// Apply width with padding and max constraint
			int finalWidth = Math.min(maxWidth + 16, MAX_COLUMN_WIDTH);
			table.getColumnModel().getColumn(col).setPreferredWidth(finalWidth);
		}
	}

	/**
	 * Center-align columns that contain numeric data or row numbers
	 *
	 * @param table the JTable to format
	 * @param columnNames array of column names
	 */
	private static void centerAlignNumericColumns(JTable table, String[] columnNames) {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		centerRenderer.setFont(AppTheme.FONT_TABLE);
		centerRenderer.setForeground(AppTheme.TEXT_PRIMARY);

		// List of column names to center-align
		String[] numericColumns = { "No.", "Grade", "Units", "Student ID", "Final Grade" };

		for (int col = 0; col < columnNames.length; col++) {
			String colName = columnNames[col].trim();
			for (String numCol : numericColumns) {
				if (colName.equalsIgnoreCase(numCol)) {
					table.getColumnModel().getColumn(col).setCellRenderer(centerRenderer);
					break;
				}
			}
		}
	}

	/**
	 * Set all columns to equal width
	 *
	 * @param table the JTable to format
	 */
	public static void setEqualColumnWidths(JTable table) {
		if (table.getColumnCount() == 0) {
			return;
		}
		int totalWidth = table.getWidth();
		int colWidth = totalWidth / table.getColumnCount();
		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col).setPreferredWidth(colWidth);
		}
	}

	/**
	 * Set specific column widths
	 *
	 * @param table the JTable to format
	 * @param widths array of widths for each column
	 */
	public static void setColumnWidths(JTable table, int[] widths) {
		for (int col = 0; col < Math.min(widths.length, table.getColumnCount()); col++) {
			table.getColumnModel().getColumn(col).setPreferredWidth(widths[col]);
		}
	}
}
