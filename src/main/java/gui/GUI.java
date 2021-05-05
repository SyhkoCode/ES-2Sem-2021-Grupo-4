package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
import java.util.HashMap;
import java.util.List;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import metrics.CompareFiles;
import metrics.ExcelDealer;
import metrics.FileDealer;
import metrics.Indicator;
import metrics.MethodData;
import metrics.MetricsRuleAnalysis;
import metrics.Quality;
import metrics.ReadJavaProject;
import metrics.Rule;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import java.awt.Cursor;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.event.ChangeEvent;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class GUI extends JFrame {

	private class ConditionsInfo {
		private JComboBox<String> sentence;
		private JComboBox<String> condition;
		private JSpinner number;
		private JComboBox<String> separator;
		private boolean isClassCondition;
		private int openParentheses = 0;
		private int closeParentheses = 0;

		public ConditionsInfo(JComboBox<String> sentence, JComboBox<String> condition, JSpinner number,
				boolean isClassCondition) {
			this.sentence = sentence;
			this.condition = condition;
			this.number = number;
			this.isClassCondition = isClassCondition;
		}

		public String getSentence() {
			if (((String) sentence.getSelectedItem()).equals("Linhas de código"))
				return "LOC_" + (isClassCondition ? "class" : "method");
			else if (((String) sentence.getSelectedItem()).equals("Número de ciclos"))
				return (isClassCondition ? "WMC_class" : "CYCLO_method");
			else
				return "NOM_class";

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

		public void setNewValues(String sentenceStr, String conditionStr, int numberInt) {
			sentence.setSelectedItem(getSentenceValue(sentenceStr));
			condition.setSelectedItem(conditionStr);
			number.setValue(numberInt);
		}

		public String getSentenceValue(String sentenceStr) {
			if (sentenceStr.equals("LOC_method") || sentenceStr.equals("LOC_class"))
				return "Linhas de código";
			else if (sentenceStr.equals("WMC_class") || sentenceStr.equals("CYCLO_method"))
				return "Número de ciclos";
			else
				return "Número de métodos";
		}

		public void setSeparatorValue(String sepratorStr) {
			separator.setSelectedItem(sepratorStr);
		}

		@Override
		public String toString() {
			String result = "";
			for (int i = 0; i < openParentheses; i++)
				result += "( ";
			result += getSentence() + " " + getCondition() + " " + getNumber();
			for (int i = 0; i < closeParentheses; i++)
				result += " )";
			if (separator != null)
				result += " " + getSeparator();
			return result;
		}
	}

	private static final String[] EXCEL_HEADER = new String[] { "MethodID", "package", "class", "method", "NOM_class",
			"LOC_class", "WMC_class", "LOC_method", "CYCLO_method" };

	private static MyProgressBar bar;

	private ArrayList<ConditionsInfo> conditionsLongMethod = new ArrayList<ConditionsInfo>();
	private ArrayList<ConditionsInfo> conditionsGodClass = new ArrayList<ConditionsInfo>();

	private boolean isLongMethod = false;

	private GroupLayout conditions_IsLongMethod_GL, conditions_IsGodClass_GL;
	private JPanel contentPane;
	private JTextField filePath_TF;
	private JTable table, tableCodeSmellsClasses, tableCodeSmellsMethods;
	private DefaultTableModel tableModel, tableClassesModel, tableMethodsModel;
	private JLabel Label_Classes, Label_Packages, Label_LOC, Label_Methods;
	private JPanel conditions_isLongMethod_Panel;
	private JPanel addCondition_isLongMethod_Panel;
	private JTextArea conditionFormat_TA;
	private JPanel conditions_isGodClass_Panel, addCondition_isGodClass_Panel;

	private JTextField resultMetricsPath_TF;

	private JTextField codeSmellsPathAv_TF;
	private JTextField metricsPathAv_TF;
	private JTextField rulePathAv_TF;
	private JPanel avaliacaoRegras;
	private JTextField nVP, nVN, nFP, nFN;
	private String csDefault, csCreated, metricsFile, rulesFile;
	private HashMap<String, Indicator> classesMap, methodsMap;
	private JTextField mVP;
	private JTextField mFP;
	private JTextField mFN;
	private JTextField mVN;
	private ChartPanel chartFrame, chartFrame2;
	private JTextField pathToSaveExcel_TF;
	private JTextField metricsPath_TF;
	private JTextField rulePath_TF;
	private JTabbedPane resultsPanel;

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
		setIconImage(Toolkit.getDefaultToolkit().getImage("images\\skunk2.png"));
		setTitle("Code Quality Assessor");
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

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 13, 958, 632);
		contentPane.add(tabbedPane);

		JPanel excelPanel = new JPanel();
		tabbedPane.addTab("Excel / Resumo", null, excelPanel, null);
		excelPanel.setLayout(null);

		JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_2.setBounds(12, 13, 929, 450);
		excelPanel.add(tabbedPane_2);

		JScrollPane scrollPane = new JScrollPane();
		tabbedPane_2.addTab("Excel", null, scrollPane, null);
		tableModel = new DefaultTableModel();
		table = new JTable() {
			private static final long serialVersionUID = 4865677530706787901L;

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
		table.setRowHeight(20);

		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);

		JPanel resumePanel = new JPanel();
		tabbedPane_2.addTab("Resumo", null, resumePanel, null);
		resumePanel.setLayout(null);

		JLabel Projeto_Label = new JLabel("Resumo do Projeto");
		Projeto_Label.setBounds(0, 33, 953, 47);
		resumePanel.add(Projeto_Label);
		Projeto_Label.setFont(new Font("Tahoma", Font.PLAIN, 31));
		Projeto_Label.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel NClasses_Label = new JLabel("Nº de Classes");
		NClasses_Label.setBounds(359, 203, 126, 30);
		resumePanel.add(NClasses_Label);
		NClasses_Label.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_Label.setFont(new Font("Tahoma", Font.BOLD, 15));

		JLabel NClasses_Packages = new JLabel("Nº de Packages");
		NClasses_Packages.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_Packages.setBounds(359, 163, 126, 30);
		resumePanel.add(NClasses_Packages);
		NClasses_Packages.setFont(new Font("Tahoma", Font.BOLD, 15));

		JLabel NClasses_Methods = new JLabel("Nº de Métodos");
		NClasses_Methods.setBounds(359, 243, 126, 30);
		resumePanel.add(NClasses_Methods);
		NClasses_Methods.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_Methods.setFont(new Font("Tahoma", Font.BOLD, 15));

		JLabel NClasses_LOC = new JLabel("Nº de Linhas");
		NClasses_LOC.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_LOC.setBounds(359, 283, 126, 30);
		resumePanel.add(NClasses_LOC);
		NClasses_LOC.setFont(new Font("Tahoma", Font.BOLD, 15));

		Label_Classes = new JLabel("");
		Label_Classes.setBounds(480, 203, 34, 30);
		resumePanel.add(Label_Classes);
		Label_Classes.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Classes.setFont(new Font("Tahoma", Font.PLAIN, 14));

		Label_Packages = new JLabel("");
		Label_Packages.setBounds(480, 163, 34, 30);
		resumePanel.add(Label_Packages);
		Label_Packages.setFont(new Font("Tahoma", Font.PLAIN, 14));

		Label_Methods = new JLabel("");
		Label_Methods.setBounds(480, 243, 32, 30);
		resumePanel.add(Label_Methods);
		Label_Methods.setFont(new Font("Tahoma", Font.PLAIN, 14));

		Label_LOC = new JLabel("");
		Label_LOC.setBounds(480, 283, 34, 30);
		resumePanel.add(Label_LOC);
		Label_LOC.setFont(new Font("Tahoma", Font.PLAIN, 14));

		filePath_TF = new JTextField();
		filePath_TF.setBounds(12, 519, 671, 30);
		excelPanel.add(filePath_TF);
		filePath_TF.setEditable(false);
		filePath_TF.setForeground(Color.BLACK);
		filePath_TF.setSelectedTextColor(Color.WHITE);
		filePath_TF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		filePath_TF.setColumns(10);

		JButton btnChooseExcelLocation = new JButton("Escolha onde Guardar Métricas");
		btnChooseExcelLocation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnChooseExcelLocation.setBounds(695, 475, 246, 30);
		excelPanel.add(btnChooseExcelLocation);
		btnChooseExcelLocation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
					pathToSaveExcel_TF.setText(jfc.getSelectedFile().getAbsolutePath());

			}
		});
		btnChooseExcelLocation.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnChooseExcelLocation.setFocusable(false);

		JButton analyzeProject_Btn = new JButton("Analisar Projeto");
		analyzeProject_Btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		analyzeProject_Btn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!isBarActive()) {
					if (!pathToSaveExcel_TF.getText().isEmpty()) {
						if (!filePath_TF.getText().isEmpty()) {
							readProject(pathToSaveExcel_TF.getText() + "\\" + filePath_TF.getToolTipText(),
									filePath_TF.getText());
						} else
							errorMessage("Escolha primeiro um Projeto java para ser analisado!");
					}

					else
						errorMessage("Tem de escolher onde quer guardar o ficheiro!");
				}
			}
		});
		analyzeProject_Btn.setFocusable(false);
		analyzeProject_Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		analyzeProject_Btn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		analyzeProject_Btn.setBounds(212, 563, 529, 30);
		excelPanel.add(analyzeProject_Btn);

		pathToSaveExcel_TF = new JTextField();
		pathToSaveExcel_TF.setSelectedTextColor(Color.WHITE);
		pathToSaveExcel_TF.setForeground(Color.BLACK);
		pathToSaveExcel_TF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		pathToSaveExcel_TF.setEditable(false);
		pathToSaveExcel_TF.setColumns(10);
		pathToSaveExcel_TF.setBounds(12, 475, 671, 30);
		excelPanel.add(pathToSaveExcel_TF);

		JButton btnChooseFile = new JButton("Escolha um Projeto");
		btnChooseFile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnChooseFile.setBounds(695, 519, 246, 30);
		excelPanel.add(btnChooseFile);
		btnChooseFile.setFocusable(false);
		btnChooseFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnChooseFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					filePath_TF.setText(jfc.getSelectedFile().getAbsolutePath());
					filePath_TF.setToolTipText(jfc.getSelectedFile().getName() + "_metrics");
				}

			}
		});

		JPanel rulesPanel = new JPanel();
		tabbedPane.addTab("Regras", null, rulesPanel, null);
		rulesPanel.setLayout(null);

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				isLongMethod = isLongMethod ? false : true;
				try {
					updateTA();
				} catch (Exception e) {

				}

			}
		});
		tabbedPane_1.setBounds(12, 13, 929, 513);
		rulesPanel.add(tabbedPane_1);

		JScrollPane conditions_SP = new JScrollPane();
		tabbedPane_1.addTab("isLongMethod", null, conditions_SP, null);

		conditions_isLongMethod_Panel = new JPanel();
		conditions_SP.setViewportView(conditions_isLongMethod_Panel);

		addCondition_isLongMethod_Panel = new JPanel();
		addCondition_isLongMethod_Panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		addCondition_isLongMethod_Panel.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton = new JButton("Adicionar Condição");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				addNewConditionBox(1, true);
				updateTA();
			}
		});
		btnNewButton.setFocusable(false);
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton.setIcon(new ImageIcon("images/plus_Image.png"));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 30));
		addCondition_isLongMethod_Panel.add(btnNewButton, BorderLayout.CENTER);

		JScrollPane scrollPane_2 = new JScrollPane();
		tabbedPane_1.addTab("isGodClass", null, scrollPane_2, null);

		conditions_isGodClass_Panel = new JPanel();
		scrollPane_2.setViewportView(conditions_isGodClass_Panel);

		addCondition_isGodClass_Panel = new JPanel();
		addCondition_isGodClass_Panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		addCondition_isGodClass_Panel.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton_2 = new JButton("Adicionar Condição");

		btnNewButton_2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_2.setIcon(new ImageIcon("images/plus_Image.png"));
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnNewButton_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				addNewConditionBox(1, true);
				updateTA();
			}
		});
		addCondition_isGodClass_Panel.add(btnNewButton_2, BorderLayout.CENTER);

		JButton btnSaveFile = new JButton("");
		btnSaveFile.setToolTipText("Guardar Regra");

		btnSaveFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileFilter(new FileNameExtensionFilter(".txt", "text"));

				if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					String path = jfc.getSelectedFile().getAbsolutePath();
					if (!path.endsWith(".txt")) {
						path = path.replaceAll("\\.[^.]*$", "") + ".txt";
					}
					List<String> ruleToSave = getRulesString();
					if (!ruleToSave.isEmpty())
						FileDealer.createFile(path, ruleToSave);
					else
						errorMessage("Não é possível guardar regras vazias!");
				}
			}
		});
		btnSaveFile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSaveFile.setFocusable(false);
		btnSaveFile.setBounds(891, 539, 50, 50);
		btnSaveFile.setIcon(new ImageIcon("images/save_Image.png"));
		rulesPanel.add(btnSaveFile);

		JButton btnNewButton_1_1 = new JButton("");
		btnNewButton_1_1.setToolTipText("Carregar Regra");
		btnNewButton_1_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_1_1.setFocusable(false);
		btnNewButton_1_1.setBounds(829, 539, 50, 50);
		btnNewButton_1_1.setIcon(new ImageIcon("images/upload_Image.png"));
		btnNewButton_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt files", "txt");
				jfc.setFileFilter(filter);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					loadRule(FileDealer.readFile(jfc.getSelectedFile().getAbsolutePath()));
				}
			}
		});
		rulesPanel.add(btnNewButton_1_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(12, 539, 500, 50);
		rulesPanel.add(scrollPane_1);

		conditionFormat_TA = new JTextArea();
		conditionFormat_TA.setEditable(false);
		scrollPane_1.setViewportView(conditionFormat_TA);

		JButton btnNewButton_1_1_1 = new JButton("");
		btnNewButton_1_1_1.setToolTipText("Limpar Condições");
		btnNewButton_1_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (warningMessage("Tem a certeza que deseja limpar estas condições?") == JOptionPane.YES_OPTION)
					clearConditions();
			}
		});
		btnNewButton_1_1_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_1_1_1.setFocusable(false);
		btnNewButton_1_1_1.setBounds(766, 539, 50, 50);
		btnNewButton_1_1_1.setIcon(new ImageIcon("images/clear_Image.png"));
		rulesPanel.add(btnNewButton_1_1_1);

		initiateConditionLongMethod();
		isLongMethod = false;
		initiateConditionGodClass();
		isLongMethod = true;

		JPanel detecaoPanel = new JPanel();
		tabbedPane.addTab("Correr Regras", null, detecaoPanel, null);
		detecaoPanel.setLayout(null);

		metricsPath_TF = new JTextField();
		metricsPath_TF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		metricsPath_TF.setEditable(false);
		metricsPath_TF.setBounds(199, 11, 536, 30);
		detecaoPanel.add(metricsPath_TF);
		metricsPath_TF.setColumns(10);

		JButton bMetrics = new JButton("Escolha as Métricas");
		bMetrics.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		bMetrics.setFocusable(false);
		bMetrics.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bMetrics.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");

				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setFileFilter(new FileNameExtensionFilter("XLS files", "xlsx"));

				if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
					metricsPath_TF.setText(jfc.getSelectedFile().getAbsolutePath());
			}
		});

		rulePath_TF = new JTextField();
		rulePath_TF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rulePath_TF.setEditable(false);
		rulePath_TF.setColumns(10);
		rulePath_TF.setBounds(199, 54, 536, 30);
		detecaoPanel.add(rulePath_TF);

		resultMetricsPath_TF = new JTextField();
		resultMetricsPath_TF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		resultMetricsPath_TF.setEnabled(false);
		resultMetricsPath_TF.setEditable(false);
		resultMetricsPath_TF.setColumns(10);
		resultMetricsPath_TF.setBounds(199, 97, 536, 30);
		detecaoPanel.add(resultMetricsPath_TF);
		bMetrics.setBounds(747, 11, 194, 30);
		detecaoPanel.add(bMetrics);

		JButton bRules = new JButton("Escolha as Regras");
		bRules.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		bRules.setFocusable(false);
		bRules.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bRules.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setFileFilter(new FileNameExtensionFilter("txt files", "txt"));

				if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
					rulePath_TF.setText(jfc.getSelectedFile().getAbsolutePath());
			}
		});

		bRules.setBounds(747, 54, 194, 30);
		detecaoPanel.add(bRules);

		JButton bLocation = new JButton("Escolha a Localização");
		bLocation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		bLocation.setFocusable(false);
		bLocation.setFont(new Font("Tahoma", Font.PLAIN, 15));
		bLocation.setEnabled(false);
		bLocation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (bLocation.isEnabled()) {
					JFileChooser jfc = new JFileChooser(".");
					jfc.setFileFilter(new FileNameExtensionFilter(".xlsx", "xlsx"));

					if (jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
						resultMetricsPath_TF.setText(jfc.getSelectedFile().getAbsolutePath());//.replaceAll(".xlsx", ""));
				}
			}
		});
		bLocation.setBounds(747, 97, 194, 30);
		detecaoPanel.add(bLocation);

		JCheckBox keepResults = new JCheckBox("Guardar Code Smells\r\n");
		keepResults.setHorizontalAlignment(SwingConstants.CENTER);
		keepResults.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		keepResults.setFocusable(false);
		keepResults.setFont(new Font("Tahoma", Font.PLAIN, 15));
		keepResults.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				resultMetricsPath_TF.setEnabled(keepResults.isSelected());
				bLocation.setEnabled(keepResults.isSelected());
			}
		});
		keepResults.setBounds(10, 96, 178, 30);
		detecaoPanel.add(keepResults);

		resultsPanel = new JTabbedPane(JTabbedPane.TOP);

		JButton runRules = new JButton("Correr");
		runRules.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		runRules.setFocusable(false);
		runRules.setFont(new Font("Tahoma", Font.PLAIN, 15));
		runRules.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (!isBarActive()) {
					if (!metricsPath_TF.getText().isEmpty()) {
						if (!rulePath_TF.getText().isEmpty()) {
							try {
								createCodeSmells(keepResults.isSelected(), metricsPath_TF.getText(),
										rulePath_TF.getText(), resultMetricsPath_TF.getText());
							} catch (Exception e) {
								errorMessage("Um dos ficheiros foi mal especificado!");
								bar.closeProgressBar();
							}
						} else
							errorMessage("Tem que escolher as regras!");
					} else
						errorMessage("Tem que escolher as métricas!");
				}
			}
		});
		runRules.setBounds(327, 166, 300, 30);
		detecaoPanel.add(runRules);

		resultsPanel.setBounds(10, 209, 931, 380);
		detecaoPanel.add(resultsPanel);
		resultsPanel.setEnabled(false);
		JScrollPane sClasses = new JScrollPane();

		resultsPanel.addTab("Classes", null, sClasses, null);

		tableClassesModel = new DefaultTableModel();
		tableCodeSmellsClasses = new JTable() {
			private static final long serialVersionUID = 6900757194149352960L;

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

		tableCodeSmellsClasses.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tableCodeSmellsClasses.setModel(tableClassesModel);
		tableCodeSmellsClasses.setRowHeight(20);

		tableCodeSmellsClasses.getTableHeader().setReorderingAllowed(false);
		sClasses.setViewportView(tableCodeSmellsClasses);
		JScrollPane sMetodos = new JScrollPane();

		resultsPanel.addTab("Methods", null, sMetodos, null);

		tableMethodsModel = new DefaultTableModel();
		tableCodeSmellsMethods = new JTable() {
			private static final long serialVersionUID = 3382685092130092007L;

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

		tableCodeSmellsMethods.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tableCodeSmellsMethods.setModel(tableMethodsModel);
		tableCodeSmellsMethods.setRowHeight(20);

		tableCodeSmellsMethods.getTableHeader().setReorderingAllowed(false);
		sMetodos.setViewportView(tableCodeSmellsMethods);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setIcon(new ImageIcon("images\\skunk2.png"));
		lblNewLabel_1.setBounds(35, 11, 120, 89);
		detecaoPanel.add(lblNewLabel_1);

		avaliacaoRegras = new JPanel();
		tabbedPane.addTab("Avaliar Regras", null, avaliacaoRegras, null);
		avaliacaoRegras.setLayout(null);

		// Avaliação Regras
		JPanel matrizConfusao = new JPanel();
		matrizConfusao.setBounds(26, 290, 400, 66);
		avaliacaoRegras.add(matrizConfusao);
		matrizConfusao.setLayout(new GridLayout(2, 2));

		nVP = new JTextField("- Verdadeiros Positivos");
		nVP.setForeground(Color.BLACK);
		nVP.setFont(new Font("Tahoma", Font.PLAIN, 15));
		nVP.setHorizontalAlignment(JTextField.CENTER);
		nVP.setEditable(false);
		nVP.setBackground(new Color(0, 255, 51));
		matrizConfusao.add(nVP);

		nFP = new JTextField("- Falsos Positivos");
		nFP.setForeground(Color.BLACK);
		nFP.setFont(new Font("Tahoma", Font.PLAIN, 15));
		nFP.setEditable(false);
		nFP.setHorizontalAlignment(JTextField.CENTER);
		nFP.setBackground(new Color(255, 51, 51));
		matrizConfusao.add(nFP);

		nFN = new JTextField("- Falsos Negativos");
		nFN.setForeground(Color.BLACK);
		nFN.setFont(new Font("Tahoma", Font.PLAIN, 15));
		nFN.setEditable(false);
		nFN.setHorizontalAlignment(JTextField.CENTER);
		nFN.setBackground(new Color(204, 0, 0));
		matrizConfusao.add(nFN);

		nVN = new JTextField("- Verdadeiros Negativos");
		nVN.setForeground(Color.BLACK);
		nVN.setFont(new Font("Tahoma", Font.PLAIN, 15));
		nVN.setEditable(false);
		nVN.setHorizontalAlignment(JTextField.CENTER);
		nVN.setBackground(new Color(0, 153, 0));
		matrizConfusao.add(nVN);

		JPanel matrizConfusao_2 = new JPanel();
		matrizConfusao_2.setBounds(520, 290, 400, 66);
		avaliacaoRegras.add(matrizConfusao_2);
		matrizConfusao_2.setLayout(new GridLayout(2, 2));

		mVP = new JTextField("- Verdadeiros Positivos");
		mVP.setForeground(new Color(0, 0, 0));
		mVP.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mVP.setHorizontalAlignment(SwingConstants.CENTER);
		mVP.setEditable(false);
		mVP.setBackground(new Color(0, 255, 51));
		matrizConfusao_2.add(mVP);

		mFP = new JTextField("- Falsos Positivos");
		mFP.setForeground(new Color(0, 0, 0));
		mFP.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mFP.setHorizontalAlignment(SwingConstants.CENTER);
		mFP.setEditable(false);
		mFP.setBackground(new Color(255, 51, 51));
		matrizConfusao_2.add(mFP);

		mFN = new JTextField("- Falsos Negativos");
		mFN.setForeground(new Color(0, 0, 0));
		mFN.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mFN.setHorizontalAlignment(SwingConstants.CENTER);
		mFN.setEditable(false);
		mFN.setBackground(new Color(204, 0, 0));
		matrizConfusao_2.add(mFN);

		mVN = new JTextField("- Verdadeiros Negativos");
		mVN.setForeground(new Color(0, 0, 0));
		mVN.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mVN.setHorizontalAlignment(SwingConstants.CENTER);
		mVN.setEditable(false);
		mVN.setBackground(new Color(0, 153, 0));
		matrizConfusao_2.add(mVN);

		JLabel lblNewLabel = new JLabel("Classes");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(26, 260, 400, 30);
		avaliacaoRegras.add(lblNewLabel);

		JTextField theoreticPathAv_TF = new JTextField();
		theoreticPathAv_TF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		theoreticPathAv_TF.setEditable(false);
		theoreticPathAv_TF.setBounds(26, 59, 700, 30);
		avaliacaoRegras.add(theoreticPathAv_TF);
		theoreticPathAv_TF.setColumns(10);

		JButton buttonDefault = new JButton("Teórico");
		buttonDefault.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		buttonDefault.setFocusable(false);
		buttonDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonDefault.setFont(new Font("Tahoma", Font.PLAIN, 15));
		buttonDefault.setEnabled(false);
		buttonDefault.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (buttonDefault.isEnabled()) {
					JFileChooser jfc = new JFileChooser(".");
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.setFileFilter(new FileNameExtensionFilter("XLS files", "xlsx"));

					if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
						theoreticPathAv_TF.setText(jfc.getSelectedFile().getAbsolutePath());
				}
			}
		});

		JLabel lblMtodos = new JLabel("Métodos");
		lblMtodos.setHorizontalAlignment(SwingConstants.CENTER);
		lblMtodos.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMtodos.setBounds(520, 260, 400, 30);
		avaliacaoRegras.add(lblMtodos);
		buttonDefault.setBounds(738, 58, 182, 30);
		avaliacaoRegras.add(buttonDefault);

		codeSmellsPathAv_TF = new JTextField();
		codeSmellsPathAv_TF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		codeSmellsPathAv_TF.setEditable(false);
		codeSmellsPathAv_TF.setColumns(10);
		codeSmellsPathAv_TF.setBounds(26, 102, 700, 30);
		avaliacaoRegras.add(codeSmellsPathAv_TF);

		JButton buttonCreated = new JButton("Code Smells");
		buttonCreated.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		buttonCreated.setFocusable(false);
		buttonCreated.setFont(new Font("Tahoma", Font.PLAIN, 15));
		buttonCreated.setEnabled(false);
		buttonCreated.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (buttonCreated.isEnabled()) {
					JFileChooser jfc = new JFileChooser(".");
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.setFileFilter(new FileNameExtensionFilter("XLS files", "xlsx"));

					if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
						codeSmellsPathAv_TF.setText(jfc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		buttonCreated.setBounds(738, 101, 182, 30);
		avaliacaoRegras.add(buttonCreated);

		metricsPathAv_TF = new JTextField();
		metricsPathAv_TF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		metricsPathAv_TF.setEditable(false);
		metricsPathAv_TF.setColumns(10);
		metricsPathAv_TF.setBounds(26, 145, 700, 30);
		avaliacaoRegras.add(metricsPathAv_TF);

		JButton metricsButton = new JButton("Métricas");
		metricsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		metricsButton.setFocusable(false);
		metricsButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		metricsButton.setEnabled(false);
		metricsButton.setBounds(738, 144, 182, 30);
		metricsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (metricsButton.isEnabled()) {
					JFileChooser jfc = new JFileChooser(".");
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.setFileFilter(new FileNameExtensionFilter("XLS files", "xlsx"));

					if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
						metricsPathAv_TF.setText(jfc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		avaliacaoRegras.add(metricsButton);

		rulePathAv_TF = new JTextField();
		rulePathAv_TF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rulePathAv_TF.setEditable(false);
		rulePathAv_TF.setColumns(10);
		rulePathAv_TF.setBounds(26, 188, 700, 30);
		avaliacaoRegras.add(rulePathAv_TF);

		JButton rulesButton = new JButton("Regras");
		rulesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		rulesButton.setFocusable(false);
		rulesButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		rulesButton.setEnabled(false);
		rulesButton.setBounds(738, 187, 182, 30);
		rulesButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (rulesButton.isEnabled()) {
					JFileChooser jfc = new JFileChooser(".");
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.setFileFilter(new FileNameExtensionFilter("txt files", "txt"));

					if (jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
						rulePathAv_TF.setText(jfc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		avaliacaoRegras.add(rulesButton);

		JButton buttonAvaliar = new JButton("Avaliar");
		buttonAvaliar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		buttonAvaliar.setFocusable(false);
		buttonAvaliar.setFont(new Font("Tahoma", Font.PLAIN, 15));

		ButtonGroup buttonGroup = new ButtonGroup();

		JRadioButton compare2 = new JRadioButton("Comparar com 2 ficheiros");
		compare2.setHorizontalAlignment(SwingConstants.CENTER);
		compare2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		compare2.setBounds(91, 14, 380, 30);
		compare2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				buttonDefault.setEnabled(true);
				buttonCreated.setEnabled(true);
				metricsButton.setEnabled(false);
				metricsPathAv_TF.setText("");
				rulesButton.setEnabled(false);
				rulePathAv_TF.setText("");
				buttonAvaliar.setEnabled(true);
			}
		});
		avaliacaoRegras.add(compare2);
		buttonGroup.add(compare2);

		JRadioButton compare3 = new JRadioButton("Comparar com 3 ficheiros");
		compare3.setHorizontalAlignment(SwingConstants.CENTER);
		compare3.setFont(new Font("Tahoma", Font.PLAIN, 15));
		compare3.setBounds(475, 14, 380, 30);
		compare3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				buttonDefault.setEnabled(true);
				buttonCreated.setEnabled(false);
				codeSmellsPathAv_TF.setText("");
				metricsButton.setEnabled(true);
				rulesButton.setEnabled(true);
				buttonAvaliar.setEnabled(true);
			}
		});
		avaliacaoRegras.add(compare3);
		buttonGroup.add(compare3);

		buttonAvaliar.setEnabled(false);
		buttonAvaliar.setBounds(373, 232, 200, 30);
		buttonAvaliar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (buttonAvaliar.isEnabled()) {
					if (!isBarActive()) {
						if (!theoreticPathAv_TF.getText().isEmpty()) {
							if (compare2.isSelected() && codeSmellsPathAv_TF.getText().isEmpty())
								errorMessage(
										"Tem que selecionar um ficheiro code Smells para fazer avaliação das regras!");
							else {
								if (compare3.isSelected() && metricsPathAv_TF.getText().isEmpty())
									errorMessage(
											"Tem que selecionar as métricas para poder fazer avaliação das regras!");
								else {
									if (compare3.isSelected() && rulePathAv_TF.getText().isEmpty())
										errorMessage(
												"Tem que selecionar as regras para poder fazer avaliação das regras!");
									else {
										if (compare2.isSelected() || compare3.isSelected()) {
											evaluateRule(compare2.isSelected(), theoreticPathAv_TF.getText(),
													codeSmellsPathAv_TF.getText(), metricsPathAv_TF.getText(),
													rulePathAv_TF.getText());
										}
									}
								}
							}

						} else
							errorMessage("Tem que selecionar um ficheiro teórico para fazer avaliação das regras!");
					}
				}

			}
		});
		avaliacaoRegras.add(buttonAvaliar);

	}

	private boolean isBarActive() {
		return bar != null ? bar.isActive() : false;
	}

	private void readProject(String pathExcel, String pathProject) {
		bar = new MyProgressBar(3);
		SwingWorker<Boolean, Object> worker = new SwingWorker<Boolean, Object>() {
			@Override
			protected Boolean doInBackground() throws Exception {
				try {
					List<String[]> javaProjectList = ReadJavaProject.readJavaProject(pathProject);
					javaProjectList.add(0, EXCEL_HEADER);
					bar.updateProgressBar();
					try {
						ExcelDealer.createExcelFile(pathExcel, javaProjectList, "Metrics");
						bar.updateProgressBar();
						readExcel(pathExcel + ".xlsx");
						bar.updateProgressBar();
						bar.closeProgressBar();

					} catch (Exception e) {
						bar.closeProgressBar();
						errorMessage("Ocorreu um erro ao tentar criar o Excel!");
					}
				} catch (Exception e1) {
					bar.closeProgressBar();
					errorMessage("Ocorreu um erro ao tentar analisar o Projeto java!");
				}
				return true;
			}
		};
		worker.execute();
	}

	private void readExcel(String path) {
		try {
			tableModel.setRowCount(0);
			tableModel.setColumnCount(0);

			Object[] header = ExcelDealer.getRow(path, 0, 0);
			tableModel.setColumnIdentifiers(header);

			for (int i = 0; i != header.length; i++)
				table.getColumnModel().getColumn(i).setResizable(false);

			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			int counter = 1;
			for (Object[] row : ExcelDealer.getAllRows(path, 0)) {
				row[0] = counter++;
				tableModel.addRow(row);
			}

			// TODO caso o projeto tenha classes com o mesmo nome dá pedrinho aka coco
			Label_Classes.setText(String.valueOf(ExcelDealer.getAllCellsOfColumn(path, 0, 2, false).size() - 1));
			Label_Packages.setText(String.valueOf(ExcelDealer.getAllCellsOfColumn(path, 0, 1, false).size() - 1));
			Label_LOC.setText(String.valueOf(ExcelDealer.sumAllColumn(path, 0, 7)));
			Label_Methods.setText(String.valueOf(ExcelDealer.getAllCellsOfColumn(path, 0, 3, true).size() - 1));
		} catch (Exception e) {
			errorMessage("Ocorreu um erro ao tentar ler o Excel!");
		}

	}

	private void createCodeSmells(Boolean keepResult, String metricsPath, String rulePath, String savePath)
			throws Exception {
		bar = new MyProgressBar(keepResult ? 5 : 4);
		SwingWorker<Boolean, Object> worker = new SwingWorker<Boolean, Object>() {
			@Override
			protected Boolean doInBackground() throws Exception {
				List<Rule> rules;
				try {
					rules = Rule.allRules(rulePath);
					bar.updateProgressBar();
				} catch(Exception e) {
					errorMessage("Ocorreu um erro ao ler o ficheiro com as regras!");
					bar.closeProgressBar();
					return true;
				}

				List<MethodData> methodsData;
				try {
					methodsData = MethodData.excelToMetricsMap(metricsPath);
					bar.updateProgressBar();
				} catch(Exception e) {
					errorMessage("Ocorreu um erro ao ler o ficheiro com as metricas!");
					bar.closeProgressBar();
					return true;
				}
				
				MetricsRuleAnalysis mra;
				try {
					mra = new MetricsRuleAnalysis(methodsData, rules);

					List<List<String[]>> codeSmells = mra.getCodeSmellResults();
					bar.updateProgressBar();

					readCodeSmells(codeSmells);
					bar.updateProgressBar();
				} catch(Exception e) {
					errorMessage("Ocorreu um erro ao detetar code smells!");
					bar.closeProgressBar();
					return true;
				}
				

				resultsPanel.setEnabled(true);
				if (keepResult) {
					if (!savePath.isEmpty()) {
						try {
							mra.getResults().add(0, EXCEL_HEADER);
							ExcelDealer.createExcelFile(savePath, mra.getResults(), "Rules");
							resultsPanel.setEnabled(true);
							bar.updateProgressBar();
						} catch (Exception e) {
							errorMessage("Ocorreu um erro ao tentar guardar os resultados!");
						}
					} else
						errorMessage(
								"Não foi possível guardar o ficheiro porque não especificou a localização onde o queria guardar!");
				}
				bar.closeProgressBar();
				return true;
			}
		};
		worker.execute();
	}

	private void readCodeSmells(List<List<String[]>> list) {
		tableClassesModel.setRowCount(0);
		tableClassesModel.setColumnCount(0);

		tableClassesModel.setColumnIdentifiers(list.get(0).get(0));
		for (int j = 0; j != list.get(0).get(0).length; j++)
			tableCodeSmellsClasses.getColumnModel().getColumn(j).setResizable(false);

		tableMethodsModel.setRowCount(0);
		tableMethodsModel.setColumnCount(0);

		tableMethodsModel.setColumnIdentifiers(list.get(1).get(0));
		for (int j = 0; j != list.get(1).get(0).length; j++)
			tableCodeSmellsMethods.getColumnModel().getColumn(j).setResizable(false);

		for (int i = 1; i < list.get(0).size(); i++)
			tableClassesModel.addRow(list.get(0).get(i));

		for (int i = 1; i < list.get(1).size(); i++)
			tableMethodsModel.addRow(list.get(1).get(i));
	}

	private void initiateConditionLongMethod() {
		conditions_isLongMethod_Panel.removeAll();
		conditions_IsLongMethod_GL = new GroupLayout(conditions_isLongMethod_Panel);
		conditions_isLongMethod_Panel.setLayout(conditions_IsLongMethod_GL);
		conditions_IsLongMethod_GL
				.setHorizontalGroup(conditions_IsLongMethod_GL.createParallelGroup(Alignment.LEADING).addComponent(
						addCondition_isLongMethod_Panel, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE));
		conditions_IsLongMethod_GL
				.setVerticalGroup(conditions_IsLongMethod_GL.createParallelGroup(Alignment.LEADING)
						.addGroup(conditions_IsLongMethod_GL.createSequentialGroup().addComponent(
								addCondition_isLongMethod_Panel, GroupLayout.PREFERRED_SIZE, 100,
								GroupLayout.PREFERRED_SIZE)));

	}

	private void initiateConditionGodClass() {
		conditions_isGodClass_Panel.removeAll();
		conditions_IsGodClass_GL = new GroupLayout(conditions_isGodClass_Panel);
		conditions_isGodClass_Panel.setLayout(conditions_IsGodClass_GL);
		conditions_IsGodClass_GL
				.setHorizontalGroup(conditions_IsGodClass_GL.createParallelGroup(Alignment.LEADING).addComponent(
						addCondition_isGodClass_Panel, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE));
		conditions_IsGodClass_GL.setVerticalGroup(conditions_IsGodClass_GL.createParallelGroup(Alignment.LEADING)
				.addGroup(conditions_IsGodClass_GL.createSequentialGroup().addComponent(addCondition_isGodClass_Panel,
						GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)));
	}

	private void addNewConditionBox(int number, boolean append) {
		GroupLayout layout = isLongMethod ? conditions_IsLongMethod_GL : conditions_IsGodClass_GL;
		ArrayList<ConditionsInfo> components = isLongMethod ? conditionsLongMethod : conditionsGodClass;

		if (!append) {
			JPanel panel = isLongMethod ? conditions_isLongMethod_Panel : conditions_isGodClass_Panel;
			panel.removeAll();
			layout = new GroupLayout(panel);
			panel.setLayout(layout);
			components.clear();
		}

		ParallelGroup hGroup = layout.createParallelGroup(Alignment.LEADING);
		SequentialGroup vGroup = layout.createSequentialGroup();
		JPanel addConditionPanel = isLongMethod ? addCondition_isLongMethod_Panel : addCondition_isGodClass_Panel;
		for (int i = 0; i != number; i++) {
			JPanel conditionPanel = getConditionPanel();

			if (components.size() > 1) {
				JPanel separator = getCondionsSeparatorPanel();
				hGroup.addComponent(separator, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE);
				if (i == 0)
					vGroup.addGap(150 * components.size() - 200);
				vGroup.addComponent(separator, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE);
			}

			hGroup.addComponent(conditionPanel, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE);
			vGroup.addComponent(conditionPanel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE);
		}
		hGroup.addComponent(addConditionPanel, GroupLayout.PREFERRED_SIZE, 922, GroupLayout.PREFERRED_SIZE);
		vGroup.addComponent(addConditionPanel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE);
		addComponentsToGroupLayouts(layout, hGroup, vGroup);
	}

	private void addComponentsToGroupLayouts(GroupLayout layout, ParallelGroup hGroup, SequentialGroup vGroup) {
		layout.setHorizontalGroup(hGroup);
		layout.setVerticalGroup(vGroup);
	}

	private JPanel getConditionPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setLayout(null);
		if (isLongMethod)
			panel.setToolTipText(conditionsLongMethod.size() + "");
		else
			panel.setToolTipText(conditionsGodClass.size() + "");

		JLabel lblSe = new JLabel("SE");
		lblSe.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblSe.setBounds(12, 35, 43, 30);
		panel.add(lblSe);

		JComboBox<String> comboBox = getComoboBox(
				isLongMethod ? new DefaultComboBoxModel<String>(new String[] { "Linhas de código", "Número de ciclos" })
						: new DefaultComboBoxModel<String>(
								new String[] { "Número de métodos", "Linhas de código", "Número de ciclos" }),
				240, 35, 200, 30);
		panel.add(comboBox);

		JComboBox<String> comboBoxSignals = getComoboBox(
				new DefaultComboBoxModel<String>(new String[] { "==", ">", "<", ">=", "<=" }), 490, 35, 100, 30);
		panel.add(comboBoxSignals);

		JButton rightPlus = getButton("+ (", 67, 34, 60, 30);
		rightPlus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (isLongMethod)
					conditionsLongMethod.get(Integer.parseInt(panel.getToolTipText())).addOpenParentheses();
				else
					conditionsGodClass.get(Integer.parseInt(panel.getToolTipText())).addOpenParentheses();
				updateTA();
			}
		});
		panel.add(rightPlus);

		JButton rightMinus = getButton("- (", 139, 35, 60, 30);
		rightMinus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (isLongMethod)
					conditionsLongMethod.get(Integer.parseInt(panel.getToolTipText())).removeOpenParentheses();
				else
					conditionsGodClass.get(Integer.parseInt(panel.getToolTipText())).removeOpenParentheses();
				updateTA();
			}
		});
		panel.add(rightMinus);

		JButton leftPlus = getButton("+ )", 778, 34, 60, 30);
		leftPlus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (isLongMethod)
					conditionsLongMethod.get(Integer.parseInt(panel.getToolTipText())).addCloseParentheses();
				else
					conditionsGodClass.get(Integer.parseInt(panel.getToolTipText())).addCloseParentheses();
				updateTA();
			}
		});
		panel.add(leftPlus);

		JButton leftMinus = getButton("- )", 850, 35, 60, 30);
		leftMinus.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (isLongMethod)
					conditionsLongMethod.get(Integer.parseInt(panel.getToolTipText())).removeCloseParentheses();
				else
					conditionsGodClass.get(Integer.parseInt(panel.getToolTipText())).removeCloseParentheses();
				updateTA();
			}
		});
		panel.add(leftMinus);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
		spinner.setFont(new Font("Tahoma", Font.PLAIN, 20));
		spinner.setBounds(640, 35, 100, 30);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				updateTA();
			}
		});
		panel.add(spinner);

		if (isLongMethod)
			conditionsLongMethod.add(new ConditionsInfo(comboBox, comboBoxSignals, spinner, false));
		else
			conditionsGodClass.add(new ConditionsInfo(comboBox, comboBoxSignals, spinner, true));

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

		if (isLongMethod)
			conditionsLongMethod.get(conditionsLongMethod.size() - 2).setSeparator(comboBox_2);
		else
			conditionsGodClass.get(conditionsGodClass.size() - 2).setSeparator(comboBox_2);

		return panel;
	}

	private void clearConditions() {
		if (isLongMethod) {
			conditionsLongMethod.clear();
			initiateConditionLongMethod();
		} else {
			conditionsGodClass.clear();
			initiateConditionGodClass();
		}
		updateTA();
	}

	private void updateTA() {
		if (isLongMethod)
			conditionFormat_TA.setText(getLongMethodFormat());
		else
			conditionFormat_TA.setText(getGodClassFormat());
	}

	public String getLongMethodFormat() {
		return getFormatString(conditionsLongMethod);
	}

	public String getGodClassFormat() {
		return getFormatString(conditionsGodClass);
	}

	public ArrayList<String> getRulesString() {
		ArrayList<String> write = new ArrayList<>();

		String methodRule = getLongMethodFormat();
		String classRule = getGodClassFormat();

		if (!methodRule.equals("SE ( )") && !methodRule.isEmpty()) {
			write.add("is_Long_Method");
			write.add(getLongMethodFormat());
		}
		if (!classRule.equals("SE ( )") && !classRule.isEmpty()) {
			write.add("is_God_Class");
			write.add(getGodClassFormat());
		}
		return write;
	}

	private String getFormatString(ArrayList<ConditionsInfo> conditions) {
		String result = "SE (";
		for (ConditionsInfo condition : conditions)
			result += " " + condition.toString();
		result += " )";
		return result;
	}

	private void loadRule(String[] rule) {
		boolean help = isLongMethod;
		isLongMethod = true;
		addConditionsBox(rule[0]);
		isLongMethod = false;
		addConditionsBox(rule[1]);
		isLongMethod = help;
		updateTA();
	}

	private void addConditionsBox(String conditionsRule) {
		String[] conditionsStr = conditionsRule.split(" ");
		addNewConditionBox(countConditions(conditionsStr), false);
		int i = 2;
		int count = 0;
		while (i < conditionsStr.length - 1) {
			ConditionsInfo info = isLongMethod ? conditionsLongMethod.get(count) : conditionsGodClass.get(count);
			while (conditionsStr[i].equals("(")) {
				info.addOpenParentheses();
				i++;
			}

			info.setNewValues(conditionsStr[i++], conditionsStr[i++], Integer.parseInt(conditionsStr[i++]));

			while (i < conditionsStr.length - 1 && conditionsStr[i].equals(")")) {
				info.addCloseParentheses();
				i++;
			}
			if (i < conditionsStr.length - 1 && (conditionsStr[i].equals("OU") || conditionsStr[i].equals("E")))
				info.setSeparatorValue(conditionsStr[i++]);
			count++;
		}
	}

	private int countConditions(String[] conditions) {
		int conditionBoxs = 0;
		for (int i = 0; i != conditions.length; i++) {
			if (conditions[i].equals("E") || conditions[i].equals("OU"))
				conditionBoxs++;
		}

		return conditionBoxs + 1;
	}

	private void evaluateRule(boolean evaluateCodeSmell, String theoreticPath, String codeSmellsPath,
			String metricsPath, String rulePath) {
		bar = new MyProgressBar(6);
		SwingWorker<Boolean, Object> worker = new SwingWorker<Boolean, Object>() {
			@Override
			protected Boolean doInBackground() {
				try {
					bar.updateProgressBar();
					Quality quality = evaluateCodeSmell ? new CompareFiles(theoreticPath, codeSmellsPath).testQuality()
							: new CompareFiles(theoreticPath, metricsPath, rulePath).testQuality();
					bar.updateProgressBar();
					classesMap = quality.getIndicatorsPerClass();
					bar.updateProgressBar();
					methodsMap = quality.getIndicatorsPerMethod();
					bar.updateProgressBar();
					updateEvaluationInfo(chartFrame, classesMap, nVP, nVN, nFP, nFN, 26, 360, 400, 230);
					bar.updateProgressBar();
					updateEvaluationInfo(chartFrame2, methodsMap, mVP, mVN, mFP, mFN, 520, 360, 400, 230);
					bar.updateProgressBar();
					bar.closeProgressBar();
				} catch (Exception e) {
					bar.closeProgressBar();
					errorMessage("Ocorreu um erro ao tentar comparar os ficheiros!");
				}
				return true;
			}
		};
		worker.execute();
	}

	private void updateEvaluationInfo(ChartPanel chartPanel, HashMap<String, Indicator> map, JTextField vP,
			JTextField vN, JTextField fP, JTextField fN, int x, int y, int width, int height) {
		if (chartPanel != null)
			avaliacaoRegras.remove(chartPanel);

		int cVP = 0, cVN = 0, cFP = 0, cFN = 0;

		for (String key : map.keySet()) {
			switch (map.get(key)) {
			case VP:
				cVP++;
				break;
			case VN:
				cVN++;
				break;
			case FP:
				cFP++;
				break;
			case FN:
				cFN++;
				break;
			}
		}

		vP.setText(cVP + " Verdadeiros Positivos");
		vN.setText(cVN + " Verdadeiros Negativos");
		fP.setText(cFP + " Falsos Positivos");
		fN.setText(cFN + " Falsos Negativos");

		DefaultPieDataset pieDataSet = new DefaultPieDataset();

		if (cVP > 0)
			pieDataSet.setValue("VP", cVP);

		if (cVN > 0)
			pieDataSet.setValue("VN", cVN);

		if (cFP > 0)
			pieDataSet.setValue("FP", cFP);

		if (cFN > 0)
			pieDataSet.setValue("FN", cFN);

		JFreeChart chart = ChartFactory.createPieChart("", pieDataSet, true, true, true);
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSectionPaint("VP", new Color(0, 255, 51));
		plot.setSectionPaint("VN", new Color(0, 153, 0));
		plot.setSectionPaint("FP", new Color(255, 51, 51));
		plot.setSectionPaint("FN", new Color(204, 0, 0));

		chartPanel = new ChartPanel(chart);
		chartPanel.setBounds(x, y, width, height);
		chartPanel.setVisible(true);

		avaliacaoRegras.add(chartPanel);
		avaliacaoRegras.repaint();
		avaliacaoRegras.revalidate();
	}

	private void errorMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Error Message", JOptionPane.ERROR_MESSAGE);
	}

	private int warningMessage(String message) {
		return JOptionPane.showConfirmDialog(null, message, "Warning", JOptionPane.YES_NO_OPTION);
	}
}
