import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;

import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;
import java.awt.Cursor;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.ScrollPaneConstants;

public class GUI extends JFrame {

	private class ConditionsInfo {
		private JComboBox<String> sentence;
		private JComboBox<String> condition;
		private JSpinner number;
		private JComboBox<String> separator;
		private int openParentheses = 0;
		private int closeParentheses = 0;

		public ConditionsInfo(JComboBox<String> sentence, JComboBox<String> condition, JSpinner number) {
			this.sentence = sentence;
			this.condition = condition;
			this.number = number;
		}

		public String getSentence() {
			if (((String) sentence.getSelectedItem()).equals("Linhas de c�digo"))
				return "LOC_method";
			else
				return "CYCLO_method";

		}

		public String getCondition() {
			return (String) condition.getSelectedItem();
		}

		public int getNumber() {
			return (int) number.getValue();
		}

		public void setSeparator(JComboBox<String> separator) {
			this.separator = separator;
		}

		public String getSeparator() {
			return (String) separator.getSelectedItem();
		}

		public void addOpenParentheses() {
			openParentheses++;
		}

		public void addCloseParentheses() {
			closeParentheses++;
		}

		public void removeOpenParentheses() {
			if (openParentheses > 0)
				openParentheses--;
		}

		public void removeCloseParentheses() {
			if (closeParentheses > 0)
				closeParentheses--;
		}

		@Override
		public String toString() {
			String result = "";
			for (int i = 0; i < openParentheses; i++)
				result += " ( ";
			result += getSentence() + " " + getCondition() + " " + getNumber();
			for (int i = 0; i < closeParentheses; i++)
				result += " ) ";
			if (separator != null)
				result += " " + getSeparator();
			return result;
		}
	}

	private ExcelDealer dealer;
	private String pathToSave = "";

	private ArrayList<ConditionsInfo> conditionsLongMethod = new ArrayList<ConditionsInfo>();
	private ArrayList<ConditionsInfo> conditionsGodClass = new ArrayList<ConditionsInfo>();

	private GroupLayout gl_conditions_Panel;
	private JPanel contentPane;
	private JTextField fileName_TF;
	private JTable table;
	private DefaultTableModel tableModel;
	private JLabel Label_Classes;
	private JLabel Label_Packages;
	private JLabel Label_LOC;
	private JLabel Label_Methods;
	private JLabel Projeto_Label;
	private JLabel NClasses_Label;
	private JLabel NClasses_LOC;
	private JLabel NClasses_Methods;
	private JLabel NClasses_Packages;
	private JPanel panel;
	private JPanel panel_1;
	private JPanel conditions_Panel;
	private JPanel addCondition_Panel;
	private JTextArea conditionFormat_TA;
	private JPanel conditions_Panel_1;
	private JLabel lblSe_1;
	private JComboBox<String> comboBox_1;
	private JButton button_1;
	private JSpinner spinner_1;
	private JPanel addCondition_Panel_1;
	private JButton btnNewButton_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.put("TextField.inactiveBackground", new ColorUIResource(new Color(255, 255, 255)));
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);

		addFrameContent();
	}

	private void addFrameContent() {
		JButton btnChooseFile = new JButton("Choose File");
		btnChooseFile.setFocusable(false);
		btnChooseFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnChooseFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String path = jfc.getSelectedFile().getAbsolutePath();
					String excelName = jfc.getSelectedFile().getName() + "_metrics";
					dealer = new ExcelDealer(path, false);
					fileName_TF.setText(path + "");
					if (pathToSave.isEmpty())
						pathToSave = path;

					dealer.createExcelFile(excelName, pathToSave, ReadJavaProject.readJavaProject(path));

					dealer = new ExcelDealer(pathToSave + "\\" + excelName + ".xlsx", true);
					readExcel();
				}
			}
		});
		btnChooseFile.setBounds(604, 609, 150, 30);
		contentPane.add(btnChooseFile);

		fileName_TF = new JTextField();
		fileName_TF.setEditable(false);
		fileName_TF.setForeground(Color.BLACK);
		fileName_TF.setSelectedTextColor(Color.WHITE);
		fileName_TF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		fileName_TF.setBounds(12, 610, 580, 30);
		contentPane.add(fileName_TF);
		fileName_TF.setColumns(10);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 13, 958, 583);
		contentPane.add(tabbedPane);

		panel_1 = new JPanel();
		tabbedPane.addTab("Regras", null, panel_1, null);
		panel_1.setLayout(null);

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(12, 13, 929, 464);
		panel_1.add(tabbedPane_1);

		JScrollPane conditions_SP = new JScrollPane();
		tabbedPane_1.addTab("isLongMethod", null, conditions_SP, null);

		conditions_Panel = new JPanel();
		conditions_SP.setViewportView(conditions_Panel);

		JPanel panel_3 = getConditionPanel();

		addCondition_Panel = new JPanel();
		addCondition_Panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		addCondition_Panel.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton = new JButton("Add new Condition");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				addNewConditionBox();
			}
		});
		btnNewButton.setFocusable(false);
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton.setIcon(new ImageIcon("images/plus_Image.png"));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		addCondition_Panel.add(btnNewButton, BorderLayout.CENTER);

		gl_conditions_Panel = new GroupLayout(conditions_Panel);
		gl_conditions_Panel.setHorizontalGroup(gl_conditions_Panel.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE)
				.addComponent(addCondition_Panel, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE));
		gl_conditions_Panel
				.setVerticalGroup(
						gl_conditions_Panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_conditions_Panel.createSequentialGroup()
										.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(addCondition_Panel, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE)));
		conditions_Panel.setLayout(gl_conditions_Panel);

		JScrollPane scrollPane_2 = new JScrollPane();
		tabbedPane_1.addTab("isGodClass", null, scrollPane_2, null);

		conditions_Panel_1 = new JPanel();
		scrollPane_2.setViewportView(conditions_Panel_1);

		JPanel panel_2 = getConditionPanel();

		addCondition_Panel_1 = new JPanel();
		addCondition_Panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		addCondition_Panel_1.setLayout(new BorderLayout(0, 0));
		GroupLayout gl_conditions_Panel_1 = new GroupLayout(conditions_Panel_1);
		gl_conditions_Panel_1.setHorizontalGroup(gl_conditions_Panel_1.createParallelGroup(Alignment.LEADING)
				.addGap(0, 922, Short.MAX_VALUE)
				.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE)
				.addComponent(addCondition_Panel_1, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE));
		gl_conditions_Panel_1
				.setVerticalGroup(
						gl_conditions_Panel_1.createParallelGroup(Alignment.LEADING).addGap(0, 432, Short.MAX_VALUE)
								.addGroup(gl_conditions_Panel_1.createSequentialGroup()
										.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(addCondition_Panel_1, GroupLayout.PREFERRED_SIZE, 100,
												GroupLayout.PREFERRED_SIZE)));

		btnNewButton_2 = new JButton("Add new Condition");

		btnNewButton_2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_2.setIcon(new ImageIcon("images/plus_Image.png"));
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 30));
		addCondition_Panel_1.add(btnNewButton_2, BorderLayout.CENTER);
		conditions_Panel_1.setLayout(gl_conditions_Panel_1);

		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_1.setFocusable(false);
		btnNewButton_1.setBounds(891, 490, 50, 50);
		btnNewButton_1.setIcon(new ImageIcon("images/save_Image.png"));
		panel_1.add(btnNewButton_1);

		JButton btnNewButton_1_1 = new JButton("");
		btnNewButton_1_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_1_1.setFocusable(false);
		btnNewButton_1_1.setBounds(829, 490, 50, 50);
		btnNewButton_1_1.setIcon(new ImageIcon("images/upload_Image.png"));
		panel_1.add(btnNewButton_1_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(12, 490, 500, 50);
		panel_1.add(scrollPane_1);

		conditionFormat_TA = new JTextArea();
		conditionFormat_TA.setEditable(false);
		scrollPane_1.setViewportView(conditionFormat_TA);

		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("Excel", null, scrollPane, null);

		tableModel = new DefaultTableModel();
		table = new JTable() {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(
						Math.max(rendererWidth + getIntercellSpacing().width + 10, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		table.setFont(new Font("Tahoma", Font.PLAIN, 15));
		table.setModel(tableModel);

		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);

		panel = new JPanel();
		tabbedPane.addTab("Resumo", null, panel, null);
		panel.setLayout(null);

		Projeto_Label = new JLabel("Resumo do Projeto");
		Projeto_Label.setBounds(0, 33, 953, 47);
		panel.add(Projeto_Label);
		Projeto_Label.setFont(new Font("Tahoma", Font.PLAIN, 31));
		Projeto_Label.setHorizontalAlignment(SwingConstants.CENTER);

		NClasses_Label = new JLabel("Nº de Classes");
		NClasses_Label.setBounds(359, 203, 126, 30);
		panel.add(NClasses_Label);
		NClasses_Label.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_Label.setFont(new Font("Tahoma", Font.BOLD, 15));

		NClasses_Packages = new JLabel("Nº de Packages");
		NClasses_Packages.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_Packages.setBounds(359, 163, 126, 30);
		panel.add(NClasses_Packages);
		NClasses_Packages.setFont(new Font("Tahoma", Font.BOLD, 15));

		NClasses_Methods = new JLabel("Nº de Métodos");
		NClasses_Methods.setBounds(359, 243, 126, 30);
		panel.add(NClasses_Methods);
		NClasses_Methods.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_Methods.setFont(new Font("Tahoma", Font.BOLD, 15));

		NClasses_LOC = new JLabel("Nº de Linhas");
		NClasses_LOC.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_LOC.setBounds(359, 283, 126, 30);
		panel.add(NClasses_LOC);
		NClasses_LOC.setFont(new Font("Tahoma", Font.BOLD, 15));

		Label_Classes = new JLabel("");
		Label_Classes.setBounds(480, 203, 34, 30);
		panel.add(Label_Classes);
		Label_Classes.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Classes.setFont(new Font("Tahoma", Font.PLAIN, 14));

		Label_Packages = new JLabel("");
		Label_Packages.setBounds(480, 163, 34, 30);
		panel.add(Label_Packages);
		Label_Packages.setFont(new Font("Tahoma", Font.PLAIN, 14));

		Label_Methods = new JLabel("");
		Label_Methods.setBounds(480, 243, 32, 30);
		panel.add(Label_Methods);
		Label_Methods.setFont(new Font("Tahoma", Font.PLAIN, 14));

		Label_LOC = new JLabel("");
		Label_LOC.setBounds(480, 283, 34, 30);
		panel.add(Label_LOC);
		Label_LOC.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JButton btnChooseExcelLocation = new JButton("Choose Excel Location");
		btnChooseExcelLocation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					pathToSave = jfc.getSelectedFile().getAbsolutePath();
				}
			}
		});
		btnChooseExcelLocation.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnChooseExcelLocation.setFocusable(false);
		btnChooseExcelLocation.setBounds(766, 609, 204, 30);
		contentPane.add(btnChooseExcelLocation);
	}

	private void readExcel() {
		fileName_TF.setText(dealer.getExcel_file());
		tableModel.setRowCount(0);
		tableModel.setColumnCount(0);

		Object[] header = dealer.getExcelHeader();
		tableModel.setColumnIdentifiers(header);

		for (int i = 0; i != header.length; i++)
			table.getColumnModel().getColumn(i).setResizable(false);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		for (Object[] row : dealer.getAllRows()) {
			tableModel.addRow(row);
		}
		Label_Classes.setText(String.valueOf((dealer.getClasses().size())));
		Label_Packages.setText(String.valueOf(dealer.getAllCellsOfRow(1).size()));
		Label_LOC.setText(String.valueOf(dealer.sumLinesOfCode()));
		Label_Methods.setText(String.valueOf(dealer.getAllCellsOfRow(3).size()));
	}

	private void addNewConditionBox() {
		JPanel panel = getConditionPanel();
		JPanel conditionSeparator = getCondionsSeparatorPanel();
		gl_conditions_Panel.setHorizontalGroup(gl_conditions_Panel.createParallelGroup(Alignment.LEADING)
				.addComponent(conditionSeparator, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE)
				.addComponent(panel, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE));
		gl_conditions_Panel.setVerticalGroup(gl_conditions_Panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_conditions_Panel.createSequentialGroup().addGap(150 * conditionsLongMethod.size() - 200)
						.addComponent(conditionSeparator, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE).addComponent(
								addCondition_Panel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)));
	}

	private JPanel getConditionPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setLayout(null);
		panel.setToolTipText(conditionsLongMethod.size() + "");

		JLabel lblSe = new JLabel("SE");
		lblSe.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblSe.setBounds(12, 35, 43, 30);
		panel.add(lblSe);

		JComboBox<String> comboBox = getComoboBox(
				new DefaultComboBoxModel<String>(new String[] { "Linhas de c\u00F3digo", "N\u00FAmero de ciclos" }),
				240, 35, 200, 30);
		panel.add(comboBox);

		JComboBox<String> comboBoxSignals = getComoboBox(
				new DefaultComboBoxModel<String>(new String[] { "=", ">", "<", ">=", "<=" }), 490, 35, 100, 30);
		panel.add(comboBoxSignals);

		JButton rightPlus = getButton("+ (", 67, 34, 60, 30);
		rightPlus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				conditionsLongMethod.get(Integer.parseInt(panel.getToolTipText())).addOpenParentheses();
				updateTA();
			}
		});
		panel.add(rightPlus);

		JButton rightMinus = getButton("- (", 139, 35, 60, 30);
		rightMinus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				conditionsLongMethod.get(Integer.parseInt(panel.getToolTipText())).removeOpenParentheses();
				updateTA();
			}
		});
		panel.add(rightMinus);

		JButton leftPlus = getButton("+ )", 778, 34, 60, 30);
		leftPlus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				conditionsLongMethod.get(Integer.parseInt(panel.getToolTipText())).addCloseParentheses();
				updateTA();
			}
		});
		panel.add(leftPlus);

		JButton leftMinus = getButton("- )", 850, 35, 60, 30);
		leftMinus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				conditionsLongMethod.get(Integer.parseInt(panel.getToolTipText())).removeCloseParentheses();
				updateTA();
			}
		});
		panel.add(leftMinus);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
		spinner.setFont(new Font("Tahoma", Font.PLAIN, 20));
		spinner.setBounds(640, 35, 100, 30);
		panel.add(spinner);

		conditionsLongMethod.add(new ConditionsInfo(comboBox, comboBoxSignals, spinner));

		return panel;
	}

	private JComboBox<String> getComoboBox(DefaultComboBoxModel<String> list, int x, int y, int width, int height) {
		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		comboBox.setFocusable(false);
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 20));
		comboBox.setModel(list);
		comboBox.setBounds(x, y, width, height);

		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED)
					updateTA();

			}
		});
		return comboBox;
	}

	private JButton getButton(String text, int x, int y, int width, int height) {
		JButton button = new JButton(text);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setFocusable(false);
		button.setFont(new Font("Tahoma", Font.PLAIN, 15));
		button.setBounds(x, y, width, height);
		return button;
	}

	private JPanel getCondionsSeparatorPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setLayout(null);

		JComboBox<String> comboBox_2 = getComoboBox(new DefaultComboBoxModel<String>(new String[] { "E", "OU" }), 411,
				12, 100, 26);
		panel.add(comboBox_2);

		conditionsLongMethod.get(conditionsLongMethod.size() - 2).setSeparator(comboBox_2);
		return panel;
	}

	private void updateTA() {
		conditionFormat_TA.setText("SE (");
		for (ConditionsInfo condition : conditionsLongMethod)
			conditionFormat_TA.append(" " + condition.toString());

		conditionFormat_TA.append(" )");
	}
}
