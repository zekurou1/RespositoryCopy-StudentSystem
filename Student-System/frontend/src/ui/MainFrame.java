package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import ui.panels.ClassListPanel;
import ui.panels.EnrollStudentPanel;
import ui.panels.GradePanel;
import ui.panels.RegisterStudentPanel;
import ui.panels.TranscriptPanel;

/**
 * FE1 — Main application window.
 *
 * Layout:
 *  TOPBAR  (red, 54 px)
 *  SIDEBAR | CONTENT AREA
 *  (210px) | [HEADER (breadcrumb+title)]
 *          | [CardLayout panels]
 *
 * To switch panels from outside: MainFrame.showPanel(PANEL_REGISTER)
 */
public class MainFrame extends JFrame {

	// ── Panel name constants (use these with showPanel) ──────────────────────
	public static final String PANEL_DASHBOARD = "dashboard";
	public static final String PANEL_REGISTER = "register";
	public static final String PANEL_ENROLL = "enroll";
	public static final String PANEL_GRADE = "grade";
	public static final String PANEL_TOR = "tor";
	public static final String PANEL_CLASSLIST = "classlist";

	// ── Internal state ────────────────────────────────────────────────────────
	private CardLayout cardLayout;
	private JPanel cardContainer;
	private JLabel lblBreadcrumb;
	private JLabel lblPageTitle;
	private JButton activeNavButton;

	// ── Nav item registry (panel name → display label) ────────────────────────
	private static final String[][] NAV_ITEMS = {
		{ PANEL_REGISTER, "Register Student", "STUDENTS" },
		{ PANEL_ENROLL, "Enroll Student", "STUDENTS" },
		{ PANEL_GRADE, "Encode Grades", "STUDENTS" },
		{ PANEL_TOR, "View TOR", "RECORDS" },
		{ PANEL_CLASSLIST, "Class List", "RECORDS" },
	};

	// ── Section headers for sidebar grouping ─────────────────────────────────
	// Rendered once per unique group key in NAV_ITEMS
	private String lastSection = "";

	// ─────────────────────────────────────────────────────────────────────────
	// Constructor
	// ─────────────────────────────────────────────────────────────────────────
	public MainFrame() {
		setTitle("Student Information System — TUP");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1050, 660));
		setSize(1150, 700);
		setLocationRelativeTo(null);

		// Root layout: topbar on NORTH, split body in CENTER
		setLayout(new BorderLayout());
		getContentPane().setBackground(AppTheme.WHITE);

		add(buildTopbar(), BorderLayout.NORTH);
		add(buildBody(), BorderLayout.CENTER);

		// Show dashboard on startup
		showPanel(PANEL_DASHBOARD);
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Topbar
	// ─────────────────────────────────────────────────────────────────────────
	private JPanel buildTopbar() {
		JPanel bar = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				AppTheme.enableAA(g);
				g.setColor(AppTheme.RED);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		bar.setOpaque(false);
		bar.setPreferredSize(new Dimension(0, AppTheme.TOPBAR_HEIGHT));
		bar.setBorder(new EmptyBorder(0, 20, 0, 20));

		// Left: logo box + institution name
		JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
		left.setOpaque(false);

		JPanel logoBox = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				AppTheme.enableAA(g);
				g.setColor(new Color(255, 255, 255, 50));
				g.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
				g.setColor(Color.WHITE);
				g.setFont(AppTheme.semibold(13));
				FontMetrics fm = g.getFontMetrics();
				String t = "TUP";
				g.drawString(t, (getWidth() - fm.stringWidth(t)) / 2,
						(getHeight() + fm.getAscent() - fm.getDescent()) / 2);
			}
		};
		logoBox.setOpaque(false);
		logoBox.setPreferredSize(new Dimension(40, 36));

		JPanel titleBlock = new JPanel(new GridLayout(2, 1, 0, 0));
		titleBlock.setOpaque(false);
		JLabel mainTitle = new JLabel("Student Information System");
		mainTitle.setFont(AppTheme.semibold(14));
		mainTitle.setForeground(Color.WHITE);
		JLabel subTitle = new JLabel("Technological University of the Philippines");
		subTitle.setFont(AppTheme.regular(11));
		subTitle.setForeground(new Color(255, 255, 255, 180));
		titleBlock.add(mainTitle);
		titleBlock.add(subTitle);

		left.add(logoBox);
		left.add(titleBlock);

		// Right: user avatar circle
		JPanel avatarCircle = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				AppTheme.enableAA(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(255, 255, 255, 60));
				g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
				g2.setColor(Color.WHITE);
				g2.setFont(AppTheme.semibold(11));
				FontMetrics fm = g2.getFontMetrics();
				String t = "AD";
				g2.drawString(t, (getWidth() - fm.stringWidth(t)) / 2,
						(getHeight() + fm.getAscent() - fm.getDescent()) / 2);
			}
		};
		avatarCircle.setOpaque(false);
		avatarCircle.setPreferredSize(new Dimension(32, 32));
		avatarCircle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		right.setOpaque(false);
		right.add(avatarCircle);

		bar.add(left, BorderLayout.WEST);
		bar.add(right, BorderLayout.EAST);
		return bar;
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Body: sidebar + content area
	// ─────────────────────────────────────────────────────────────────────────
	private JSplitPane buildBody() {
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildSidebar(), buildContentArea());
		split.setDividerSize(0);
		split.setEnabled(false); // user cannot drag the divider
		split.setBorder(null);
		return split;
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Sidebar
	// ─────────────────────────────────────────────────────────────────────────
	private JPanel buildSidebar() {
		JPanel sidebar = new JPanel();
		sidebar.setBackground(AppTheme.OFF_WHITE);
		sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.BORDER));
		sidebar.setPreferredSize(new Dimension(AppTheme.SIDEBAR_WIDTH, 0));
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.BORDER),
				new EmptyBorder(14, 0, 14, 0)));

		// Dashboard nav item (special — no section header)
		sidebar.add(makeNavButton(PANEL_DASHBOARD, "Dashboard", true));

		// Remaining items with section headers
		lastSection = "";
		for (String[] item : NAV_ITEMS) {
			String panel = item[0];
			String label = item[1];
			String section = item[2];

			if (!section.equals(lastSection)) {
				sidebar.add(makeSectionLabel(section));
				lastSection = section;
			}
			sidebar.add(makeNavButton(panel, label, false));
		}

		sidebar.add(Box.createVerticalGlue());
		return sidebar;
	}

	private JLabel makeSectionLabel(String text) {
		JLabel lbl = new JLabel(text);
		lbl.setFont(AppTheme.semibold(10));
		lbl.setForeground(AppTheme.TEXT_MUTED);
		lbl.setBorder(new EmptyBorder(12, 18, 6, 18));
		lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
		return lbl;
	}

	private JButton makeNavButton(String panelName, String label, boolean isDashboard) {
		JButton btn = new JButton(label) {
			@Override
			protected void paintComponent(Graphics g) {
				AppTheme.enableAA(g);
				boolean active = (this == activeNavButton);
				// Left accent bar
				if (active) {
					g.setColor(AppTheme.RED);
					g.fillRect(0, 0, 3, getHeight());
					g.setColor(AppTheme.RED_XLIGHT);
					g.fillRect(3, 0, getWidth() - 3, getHeight());
				} else if (getModel().isRollover()) {
					g.setColor(new Color(0xF0ECEB));
					g.fillRect(0, 0, getWidth(), getHeight());
				}
				super.paintComponent(g);
			}
		};

		btn.setFont(AppTheme.regular(13));
		btn.setForeground(AppTheme.TEXT_MID);
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.setBorder(new EmptyBorder(9, 18, 9, 18));
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setMaximumSize(new Dimension(AppTheme.SIDEBAR_WIDTH, 38));
		btn.setAlignmentX(Component.LEFT_ALIGNMENT);

		btn.addActionListener(e -> showPanel(panelName));
		btn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				btn.repaint();
			}

			public void mouseExited(MouseEvent e) {
				btn.repaint();
			}
		});

		// Register in a map so showPanel can look it up
		btn.setName(panelName);
		return btn;
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Content area: header + CardLayout
	// ─────────────────────────────────────────────────────────────────────────
	private JPanel buildContentArea() {
		JPanel area = new JPanel(new BorderLayout());
		area.setBackground(AppTheme.BG_PAGE);

		// ── Content header ────────────────────────────────────────────────────
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(AppTheme.WHITE);
		header.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
				new EmptyBorder(14, AppTheme.CONTENT_PADDING, 12, AppTheme.CONTENT_PADDING)));

		lblBreadcrumb = new JLabel("Dashboard");
		lblBreadcrumb.setFont(AppTheme.regular(11));
		lblBreadcrumb.setForeground(AppTheme.TEXT_MUTED);

		lblPageTitle = new JLabel("Dashboard");
		lblPageTitle.setFont(AppTheme.semibold(20));
		lblPageTitle.setForeground(AppTheme.TEXT_DARK);

		JPanel titleStack = new JPanel(new GridLayout(2, 1, 0, 2));
		titleStack.setOpaque(false);
		titleStack.add(lblBreadcrumb);
		titleStack.add(lblPageTitle);
		header.add(titleStack, BorderLayout.CENTER);

		// ── Card container ────────────────────────────────────────────────────
		cardLayout = new CardLayout();
		cardContainer = new JPanel(cardLayout);
		cardContainer.setBackground(AppTheme.BG_PAGE);
		cardContainer.setBorder(new EmptyBorder(AppTheme.CONTENT_PADDING, AppTheme.CONTENT_PADDING,
				AppTheme.CONTENT_PADDING, AppTheme.CONTENT_PADDING));

		// Register all panels
		cardContainer.add(buildDashboardPanel(), PANEL_DASHBOARD);
		cardContainer.add(new RegisterStudentPanel(), PANEL_REGISTER);
		cardContainer.add(new EnrollStudentPanel(), PANEL_ENROLL);
		cardContainer.add(new GradePanel(), PANEL_GRADE);
		cardContainer.add(new TranscriptPanel(), PANEL_TOR);
		cardContainer.add(new ClassListPanel(), PANEL_CLASSLIST);

		area.add(header, BorderLayout.NORTH);
		area.add(new JScrollPane(cardContainer) {
			{
				setBorder(null);
				setOpaque(false);
				getViewport().setOpaque(false);
			}
		}, BorderLayout.CENTER);

		return area;
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Dashboard panel (built inline — no separate file needed for FE1)
	// ─────────────────────────────────────────────────────────────────────────
	private JPanel buildDashboardPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(false);

		// ── Stat cards row ────────────────────────────────────────────────────
		JPanel statsRow = new JPanel(new GridLayout(1, 4, 12, 0));
		statsRow.setOpaque(false);
		statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
		statsRow.add(makeStatCard("Total Students", "247", true));
		statsRow.add(makeStatCard("Enrollments", "618", false));
		statsRow.add(makeStatCard("Courses", "34", false));
		statsRow.add(makeStatCard("Pass Rate", "86%", true));

		// ── Quick-action buttons row ──────────────────────────────────────────
		JPanel qaRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
		qaRow.setOpaque(false);
		qaRow.add(makeQuickAction("Register Student", PANEL_REGISTER));
		qaRow.add(makeQuickAction("Enroll Student", PANEL_ENROLL));
		qaRow.add(makeQuickAction("View TOR", PANEL_TOR));
		qaRow.add(makeQuickAction("Class List", PANEL_CLASSLIST));

		// ── Welcome text ──────────────────────────────────────────────────────
		JLabel welcome = new JLabel("Welcome to the TUP Student Information System", SwingConstants.CENTER);
		welcome.setFont(AppTheme.semibold(18));
		welcome.setForeground(AppTheme.TEXT_DARK);
		welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel sub = new JLabel("Select a module from the sidebar or use the shortcuts below", SwingConstants.CENTER);
		sub.setFont(AppTheme.regular(13));
		sub.setForeground(AppTheme.TEXT_MUTED);
		sub.setAlignmentX(Component.CENTER_ALIGNMENT);

		panel.add(statsRow);
		panel.add(Box.createVerticalStrut(32));
		panel.add(welcome);
		panel.add(Box.createVerticalStrut(8));
		panel.add(sub);
		panel.add(Box.createVerticalStrut(20));
		panel.add(qaRow);

		return panel;
	}

	private JPanel makeStatCard(String label, String value, boolean accent) {
		JPanel card = new JPanel(new GridLayout(2, 1, 0, 4));
		card.setBackground(AppTheme.WHITE);
		card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.BORDER, 1, true),
				new EmptyBorder(14, 16, 14, 16)));

		JLabel lbl = new JLabel(label);
		lbl.setFont(AppTheme.regular(11));
		lbl.setForeground(AppTheme.TEXT_MUTED);

		JLabel val = new JLabel(value);
		val.setFont(AppTheme.semibold(22));
		val.setForeground(accent ? AppTheme.RED : AppTheme.TEXT_DARK);

		card.add(lbl);
		card.add(val);
		return card;
	}

	private JButton makeQuickAction(String label, String panelName) {
		JButton btn = new JButton(label);
		btn.setFont(AppTheme.regular(13));
		btn.setForeground(AppTheme.TEXT_MID);
		btn.setBackground(AppTheme.WHITE);
		btn.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(AppTheme.BORDER, 1, true),
				new EmptyBorder(12, 18, 12, 18)));
		btn.setFocusPainted(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btn.setOpaque(true);
		btn.addActionListener(e -> showPanel(panelName));
		btn.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				btn.setBackground(AppTheme.RED_XLIGHT);
				btn.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(AppTheme.RED, 1, true),
						new EmptyBorder(12, 18, 12, 18)));
				btn.setForeground(AppTheme.RED);
			}

			public void mouseExited(MouseEvent e) {
				btn.setBackground(AppTheme.WHITE);
				btn.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(AppTheme.BORDER, 1, true),
						new EmptyBorder(12, 18, 12, 18)));
				btn.setForeground(AppTheme.TEXT_MID);
			}
		});
		return btn;
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Public API — call from any panel to switch views
	// ─────────────────────────────────────────────────────────────────────────
	/**
	 * Switch the visible card and update the header + sidebar highlight.
	 *
	 * @param panelName one of the PANEL_* constants defined above
	 */
	public void showPanel(String panelName) {
		cardLayout.show(cardContainer, panelName);
		updateHeader(panelName);
		updateActiveNavButton(panelName);
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Header & nav sync helpers
	// ─────────────────────────────────────────────────────────────────────────
	private void updateHeader(String panelName) {
		String label = labelFor(panelName);
		lblBreadcrumb.setText(label);
		lblPageTitle.setText(pageTitleFor(panelName));
	}

	private void updateActiveNavButton(String panelName) {
		// Walk the sidebar looking for the JButton with matching name
		JPanel sidebar = (JPanel) ((JSplitPane) getContentPane().getComponent(1)).getLeftComponent();
		for (Component c : sidebar.getComponents()) {
			if (c instanceof JButton btn && panelName.equals(btn.getName())) {
				if (activeNavButton != null) {
					activeNavButton.setForeground(AppTheme.TEXT_MID);
					activeNavButton.repaint();
				}
				activeNavButton = btn;
				btn.setForeground(AppTheme.RED);
				btn.repaint();
				return;
			}
		}
	}

	private static String labelFor(String panelName) {
		return switch (panelName) {
		case PANEL_DASHBOARD -> "Dashboard";
		case PANEL_REGISTER -> "Register Student";
		case PANEL_ENROLL -> "Enroll Student";
		case PANEL_GRADE -> "Encode Grades";
		case PANEL_TOR -> "View TOR";
		case PANEL_CLASSLIST -> "Class List";
		default -> panelName;
		};
	}

	private static String pageTitleFor(String panelName) {
		return switch (panelName) {
		case PANEL_DASHBOARD -> "Dashboard";
		case PANEL_REGISTER -> "Register Student";
		case PANEL_ENROLL -> "Enroll Student to Course";
		case PANEL_GRADE -> "Encode Grades";
		case PANEL_TOR -> "Transcript of Records";
		case PANEL_CLASSLIST -> "Class List";
		default -> panelName;
		};
	}
}
