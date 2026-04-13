package ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

/**
 * AppTheme.java — FE1
 * Central theme constants for the Student Information System.
 * White base + TUP Red accent.
 *
 * USAGE:
 *   panel.setBackground(AppTheme.WHITE);
 *   label.setFont(AppTheme.FONT_TITLE);
 *   label.setForeground(AppTheme.RED);
 *   AppTheme.stylePrimaryButton(button);
 *   AppTheme.styleTable(table);
 */
public class AppTheme {

	// ─────────────────────────────────────────────
	// COLORS
	// ─────────────────────────────────────────────

	/** Primary accent — TUP Red */
	public static final Color RED = new Color(0xC0392B);

	/** Darker red — for hover states on buttons */
	public static final Color RED_DARK = new Color(0x96281B);

	/** Very light red — for active nav item background */
	public static final Color RED_XLIGHT = new Color(0xFEF5F4);

	/** Light red — for borders on accented cards */
	public static final Color RED_LIGHT = new Color(0xFADBD8);

	/** Pure white — main content background */
	public static final Color WHITE = new Color(0xFFFFFF);

	/** Off-white — sidebar, table header, stat card background */
	public static final Color OFF_WHITE = new Color(0xFAFAF9);

	/** Border color — all dividers and card borders */
	public static final Color BORDER = new Color(0xE8E4E0);

	/** Primary text — headings, table data */
	public static final Color TEXT_PRIMARY = new Color(0x1A1A1A);

	/** Secondary text — labels, nav items */
	public static final Color TEXT_SECONDARY = new Color(0x6B6560);

	/** Muted text — section headers, placeholders */
	public static final Color TEXT_MUTED = new Color(0xA09A94);

	/** Medium text — nav items, secondary labels */
	public static final Color TEXT_MID = new Color(0x7A746E);

	/** Dark text — main headings */
	public static final Color TEXT_DARK = new Color(0x2D2520);

	/** Page background — light gray */
	public static final Color BG_PAGE = new Color(0xFCFBF8);

	/** Badge: Enrolled */
	public static final Color BADGE_ENROLLED_BG = new Color(0xFEF5F4);
	public static final Color BADGE_ENROLLED_FG = new Color(0x96281B);

	/** Badge: Passed */
	public static final Color BADGE_PASSED_BG = new Color(0xEAF7EE);
	public static final Color BADGE_PASSED_FG = new Color(0x1B5E20);

	/** Badge: Incomplete */
	public static final Color BADGE_INCOMPLETE_BG = new Color(0xFFF8E1);
	public static final Color BADGE_INCOMPLETE_FG = new Color(0x7B5E00);

	// ─────────────────────────────────────────────
	// DIMENSIONS
	// ─────────────────────────────────────────────

	public static final int SIDEBAR_WIDTH = 220;
	public static final int TOPBAR_HEIGHT = 58;
	public static final int NAV_ITEM_HEIGHT = 36;
	public static final int CONTENT_PAD = 24;
	public static final int CONTENT_PADDING = 24;
	public static final int CARD_PAD = 16;
	public static final int BORDER_RADIUS = 8;
	public static final int CARD_RADIUS = 10;

	// ─────────────────────────────────────────────
	// FONTS
	// ─────────────────────────────────────────────

	/** App title in topbar */
	public static final Font FONT_TITLE = new Font("Georgia", Font.BOLD, 16);

	/** Panel headings (e.g. "Student Records") */
	public static final Font FONT_HEADING = new Font("Georgia", Font.BOLD, 20);

	/** Navigation items */
	public static final Font FONT_NAV = new Font("SansSerif", Font.PLAIN, 13);

	/** Navigation items — active state */
	public static final Font FONT_NAV_ACTIVE = new Font("SansSerif", Font.BOLD, 13);

	/** Section titles, column headers */
	public static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 11);

	/** Table body text */
	public static final Font FONT_TABLE = new Font("SansSerif", Font.PLAIN, 12);

	/** Stat card numbers */
	public static final Font FONT_STAT = new Font("SansSerif", Font.BOLD, 22);

	/** Stat card labels */
	public static final Font FONT_STAT_LABEL = new Font("SansSerif", Font.PLAIN, 11);

	/** Small muted text — breadcrumb, footer, monospace IDs */
	public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 11);

	/** Monospaced — student IDs */
	public static final Font FONT_MONO = new Font("Monospaced", Font.PLAIN, 11);

	/** Buttons */
	public static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 12);

	// ─────────────────────────────────────────────
	// WINDOW DEFAULTS
	// ─────────────────────────────────────────────

	public static final String APP_TITLE = "Student Information System";
	public static final String APP_SUBTITLE = "Technological University of the Philippines";
	public static final int WINDOW_W = 1100;
	public static final int WINDOW_H = 680;
	public static final int WINDOW_MIN_W = 900;
	public static final int WINDOW_MIN_H = 560;

	// ─────────────────────────────────────────────
	// HELPER: enable anti-aliased text rendering
	// ─────────────────────────────────────────────

	/**
	 * Call this at the top of any paintComponent() override
	 * to get smooth, anti-aliased text and shapes.
	 *
	 * Example:
	 *   protected void paintComponent(Graphics g) {
	 *       super.paintComponent(g);
	 *       AppTheme.enableAA(g);
	 *       ...
	 *   }
	 */
	public static void enableAA(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	}

	// ─────────────────────────────────────────────
	// HELPER: apply to any JPanel for default look
	// ─────────────────────────────────────────────

	/**
	 * Applies the standard white background and no border.
	 * Call on every JPanel in the app for consistency.
	 *
	 * Example:
	 *   JPanel panel = new JPanel();
	 *   AppTheme.stylePanel(panel);
	 */
	public static void stylePanel(JPanel panel) {
		panel.setBackground(WHITE);
		panel.setOpaque(true);
	}

	/**
	 * Applies the off-white background — use for sidebar, table header.
	 */
	public static void stylePanelMuted(JPanel panel) {
		panel.setBackground(OFF_WHITE);
		panel.setOpaque(true);
	}

	// ─────────────────────────────────────────────
	// HELPER: style a primary (red) button
	// ─────────────────────────────────────────────

	/**
	 * Applies primary button styling (TUP Red).
	 *
	 * Example:
	 *   JButton btn = new JButton("+ Register Student");
	 *   AppTheme.stylePrimaryButton(btn);
	 */
	public static void stylePrimaryButton(JButton btn) {
		btn.setBackground(RED);
		btn.setForeground(Color.WHITE);
		btn.setFont(FONT_BUTTON);
		btn.setFocusPainted(false);
		btn.setBorderPainted(false);
		btn.setOpaque(true);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/**
	 * Applies ghost button styling (text + border).
	 *
	 * Example:
	 *   JButton btn = new JButton("Export");
	 *   AppTheme.styleGhostButton(btn);
	 */
	public static void styleGhostButton(JButton btn) {
		btn.setBackground(WHITE);
		btn.setForeground(TEXT_SECONDARY);
		btn.setFont(FONT_BUTTON);
		btn.setFocusPainted(false);
		btn.setOpaque(true);
		btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btn.setBorder(BorderFactory.createLineBorder(BORDER, 1));
	}

	// ─────────────────────────────────────────────
	// HELPER: style a JTable
	// ─────────────────────────────────────────────

	/**
	 * Applies consistent table styling.
	 *
	 * Example:
	 *   JTable table = new JTable(model);
	 *   AppTheme.styleTable(table);
	 */
	public static void styleTable(JTable table) {
		table.setFont(FONT_TABLE);
		table.setForeground(TEXT_PRIMARY);
		table.setBackground(WHITE);
		table.setRowHeight(38);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setSelectionBackground(RED_XLIGHT);
		table.setSelectionForeground(RED_DARK);
		table.getTableHeader().setFont(FONT_LABEL);
		table.getTableHeader().setForeground(TEXT_MUTED);
		table.getTableHeader().setBackground(OFF_WHITE);
		table.getTableHeader().setPreferredSize(new Dimension(0, 36));
	}

	// ─────────────────────────────────────────────
	// HELPER: font factory methods
	// ─────────────────────────────────────────────

	/**
	 * Get a bold/semibold font at the given size
	 */
	public static Font semibold(int size) {
		return new Font("SansSerif", Font.BOLD, size);
	}

	/**
	 * Get a regular font at the given size
	 */
	public static Font regular(int size) {
		return new Font("SansSerif", Font.PLAIN, size);
	}

	// Prevent instantiation
	private AppTheme() {
	}
}
