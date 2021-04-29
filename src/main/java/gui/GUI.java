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
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
import metrics.MethodRuleAnalysis;
import metrics.Quality;
import metrics.ReadJavaProject;
import metrics.Rule;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
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

	private boolean isLongMethod = false;

	private GroupLayout conditions_IsLongMethod_GL;
	private GroupLayout conditions_IsGodClass_GL;
	private JPanel contentPane;
	private JTextField fileName_TF;
	private JTable table;
	private JTable table1;
	private JTable table2;
	private DefaultTableModel tableModel;
	private DefaultTableModel tableModel1;
	private DefaultTableModel tableModel2;
	private JLabel Label_Classes;
	private JLabel Label_Packages;
	private JLabel Label_LOC;
	private JLabel Label_Methods;
	private JLabel Projeto_Label;
	private JLabel NClasses_Label;
	private JLabel NClasses_LOC;
	private JLabel NClasses_Methods;
	private JLabel NClasses_Packages;
	private JPanel resumePanel;
	private JPanel panel_1;
	private JPanel conditions_isLongMethod_Panel;
	private JPanel addCondition_isLongMethod_Panel;
	private JTextArea conditionFormat_TA;
	private JPanel conditions_isGodClass_Panel;
	private JPanel addCondition_isGodClass_Panel;
	private JTextField metricasGeradas;
	private JTextField regras;
	private JButton bRules;
	private JTextField localizacaoResultados;
	private JButton bLocation;
	private JCheckBox testRuleEffiency;
	private JButton buttonLocEfficency;
	private JTextField localTeoricos;
	private JButton bTeoricos;
	private JButton runRules;
	private JButton btnSaveFile;
	private JPanel matrizConfusao_1;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textFieldTeoricos;
	private JTextField textFieldCreated;
	private JTextField textFieldMetricas;
	private JTextField textFieldRules;
	private JPanel avaliacaoRegras;
	private JTextField nVP, nVN, nFP, nFN;
	private String csDefault, csCreated, metricsFile, rulesFile;
	private HashMap<String, Indicator> classesMap, methodsMap;
	private JTextField mVP;
	private JTextField mFP;
	private JTextField mFN;
	private JTextField mVN;
	private ChartPanel chartFrame, chartFrame2;
	private DefaultPieDataset pieDataSet, pieDataSet2;

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

					fileName_TF.setText(path + "");
					if (pathToSave.isEmpty())
						pathToSave = path;
					List<String[]> javaProjectList = ReadJavaProject.readJavaProject(path);
					javaProjectList.add(0, new String[] { "MethodID", "package", "class", "method", "NOM_class",
							"LOC_class", "WMC_class", "LOC_method", "CYCLO_method" });
					try {
						ExcelDealer.createExcelFile(pathToSave + "\\" + excelName, javaProjectList, "Metrics");
					} catch (Exception e) {
						// TODO Lançar erro
						e.printStackTrace();
					}

					readExcel(pathToSave + "\\" + excelName + ".xlsx");
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
		tabbedPane_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				isLongMethod = isLongMethod ? false : true;
				try {
					updateTA();
				} catch (Exception e) {

				}

			}
		});
		tabbedPane_1.setBounds(12, 13, 929, 464);
		panel_1.add(tabbedPane_1);

		JScrollPane conditions_SP = new JScrollPane();
		tabbedPane_1.addTab("isLongMethod", null, conditions_SP, null);

		conditions_isLongMethod_Panel = new JPanel();
		conditions_SP.setViewportView(conditions_isLongMethod_Panel);

		addCondition_isLongMethod_Panel = new JPanel();
		addCondition_isLongMethod_Panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		addCondition_isLongMethod_Panel.setLayout(new BorderLayout(0, 0));

		JButton btnNewButton = new JButton("Add new Condition");
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

		JButton btnNewButton_2 = new JButton("Add new Condition");

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

		btnSaveFile = new JButton("");
		btnSaveFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "text");
				jfc.setFileFilter(filter);
				int returnValue = jfc.showSaveDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					String path = jfc.getSelectedFile().getAbsolutePath();
					if (!path.endsWith(".txt")) {
						path = path.replaceAll("\\.[^.]*$", "") + ".txt";
					}
					if (pathToSave.isEmpty())
						pathToSave = path;
					FileDealer.createFile(path, getRulesString());
				}
			}
		});
		btnSaveFile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSaveFile.setFocusable(false);
		btnSaveFile.setBounds(891, 490, 50, 50);
		btnSaveFile.setIcon(new ImageIcon("images/save_Image.png"));
		panel_1.add(btnSaveFile);

		JButton btnNewButton_1_1 = new JButton("");
		btnNewButton_1_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_1_1.setFocusable(false);
		btnNewButton_1_1.setBounds(829, 490, 50, 50);
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
		panel_1.add(btnNewButton_1_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(12, 490, 500, 50);
		panel_1.add(scrollPane_1);

		conditionFormat_TA = new JTextArea();
		conditionFormat_TA.setEditable(false);
		scrollPane_1.setViewportView(conditionFormat_TA);

		JButton btnNewButton_1_1_1 = new JButton("");
		btnNewButton_1_1_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				clearConditions();
			}
		});
		btnNewButton_1_1_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_1_1_1.setFocusable(false);
		btnNewButton_1_1_1.setBounds(767, 490, 50, 50);
		btnNewButton_1_1_1.setIcon(new ImageIcon("images/clear_Image.png"));
		panel_1.add(btnNewButton_1_1_1);

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

		resumePanel = new JPanel();
		tabbedPane.addTab("Resumo", null, resumePanel, null);
		resumePanel.setLayout(null);

		Projeto_Label = new JLabel("Resumo do Projeto");
		Projeto_Label.setBounds(0, 33, 953, 47);
		resumePanel.add(Projeto_Label);
		Projeto_Label.setFont(new Font("Tahoma", Font.PLAIN, 31));
		Projeto_Label.setHorizontalAlignment(SwingConstants.CENTER);

		NClasses_Label = new JLabel("Nº de Classes");
		NClasses_Label.setBounds(359, 203, 126, 30);
		resumePanel.add(NClasses_Label);
		NClasses_Label.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_Label.setFont(new Font("Tahoma", Font.BOLD, 15));

		NClasses_Packages = new JLabel("Nº de Packages");
		NClasses_Packages.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_Packages.setBounds(359, 163, 126, 30);
		resumePanel.add(NClasses_Packages);
		NClasses_Packages.setFont(new Font("Tahoma", Font.BOLD, 15));

		NClasses_Methods = new JLabel("Nº de Métodos");
		NClasses_Methods.setBounds(359, 243, 126, 30);
		resumePanel.add(NClasses_Methods);
		NClasses_Methods.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_Methods.setFont(new Font("Tahoma", Font.BOLD, 15));

		NClasses_LOC = new JLabel("Nº de Linhas");
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
		initiateConditionLongMethod();
		isLongMethod = false;
		initiateConditionGodClass();
		isLongMethod = true;

		JPanel detecaoPanel = new JPanel();
		tabbedPane.addTab("Correr Regras", null, detecaoPanel, null);
		detecaoPanel.setLayout(null);

		metricasGeradas = new JTextField();
		metricasGeradas.setEditable(false);
		metricasGeradas.setBounds(125, 11, 491, 26);
		detecaoPanel.add(metricasGeradas);
		metricasGeradas.setColumns(10);

		JButton bMetrics = new JButton("Choose metrics");
		bMetrics.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xlsx");
				jfc.setFileFilter(filter);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					pathToSave = jfc.getSelectedFile().getAbsolutePath();
					metricasGeradas.setText(pathToSave);

				}
			}
		});

		regras = new JTextField();
		regras.setEditable(false);
		regras.setColumns(10);
		regras.setBounds(125, 48, 491, 26);
		detecaoPanel.add(regras);

		localizacaoResultados = new JTextField();
		localizacaoResultados.setEnabled(false);
		localizacaoResultados.setEditable(false);
		localizacaoResultados.setColumns(10);
		localizacaoResultados.setBounds(125, 85, 491, 26);
		detecaoPanel.add(localizacaoResultados);
		bMetrics.setBounds(626, 11, 174, 26);
		detecaoPanel.add(bMetrics);

		bRules = new JButton("Choose rules");
		bRules.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt files", "txt");
				jfc.setFileFilter(filter);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					pathToSave = jfc.getSelectedFile().getAbsolutePath();
					regras.setText(pathToSave);
				}
			}
		});

		bRules.setBounds(626, 48, 174, 26);
		detecaoPanel.add(bRules);

		JCheckBox keepResults = new JCheckBox("Keep Results");
		keepResults.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (keepResults.isSelected()) {
					localizacaoResultados.setEnabled(true);
					bLocation.setEnabled(true);
				} else {
					localizacaoResultados.setEnabled(false);
					bLocation.setEnabled(false);
				}
			}
		});
		keepResults.setBounds(6, 80, 113, 37);
		detecaoPanel.add(keepResults);

		bLocation = new JButton("Choose location");
		bLocation.setEnabled(false);
		bLocation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				FileNameExtensionFilter filter = new FileNameExtensionFilter(".xlsx", "xlsx");
				jfc.setFileFilter(filter);
				int returnValue = jfc.showSaveDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {

					String path = jfc.getSelectedFile().getAbsolutePath();
					if (!path.endsWith(".txt")) {
						path = path.replaceAll("\\.[^.]*$", "") + ".xlsx";
						localizacaoResultados.setText(path);
					}
					if (pathToSave.isEmpty())
						pathToSave = path;

				}
			}
		});
		bLocation.setBounds(626, 85, 174, 26);
		detecaoPanel.add(bLocation);

		testRuleEffiency = new JCheckBox("Test Rule Effiency");
		testRuleEffiency.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (testRuleEffiency.isSelected()) {
					buttonLocEfficency.setEnabled(true);
					bTeoricos.setEnabled(true);
					localTeoricos.setEnabled(true);
				} else {
					buttonLocEfficency.setEnabled(false);
					bTeoricos.setEnabled(false);
					localTeoricos.setEnabled(false);
				}
			}
		});
		testRuleEffiency.setBounds(263, 120, 139, 37);
		detecaoPanel.add(testRuleEffiency);

		buttonLocEfficency = new JButton("Choose location efficiency");
		buttonLocEfficency.setEnabled(false);
		buttonLocEfficency.setBounds(408, 125, 208, 26);
		detecaoPanel.add(buttonLocEfficency);

		localTeoricos = new JTextField();
		localTeoricos.setEnabled(false);
		localTeoricos.setEditable(false);
		localTeoricos.setColumns(10);
		localTeoricos.setBounds(125, 164, 491, 26);
		detecaoPanel.add(localTeoricos);

		bTeoricos = new JButton("Choose teoricos");
		bTeoricos.setEnabled(false);
		bTeoricos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xlsx");
				jfc.setFileFilter(filter);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					pathToSave = jfc.getSelectedFile().getAbsolutePath();
					localTeoricos.setText(pathToSave);
				}
			}
		});

		bTeoricos.setBounds(626, 164, 174, 26);
		detecaoPanel.add(bTeoricos);
		JTabbedPane panelResultados = new JTabbedPane(JTabbedPane.TOP);

		runRules = new JButton("Run");
		runRules.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					MethodRuleAnalysis mra = new MethodRuleAnalysis(
							MethodData.excelToMetricsMap(metricasGeradas.getText()),
							Rule.allRules(new File(regras.getText())));

					if (keepResults.isSelected()) {
						mra.getResults().add(0, new String[] { "MethodID", "package", "class", "method", "NOM_class",
								"LOC_class", "WMC_class", "LOC_method", "CYCLO_method" });
						ExcelDealer.createExcelFile(localizacaoResultados.getText(), mra.getResults(), "Rules");
						panelResultados.setEnabled(true);
					}
					readCodeSmells(mra.getCodeSmellResults());
					panelResultados.setEnabled(true);

				} catch (Exception e1) {
					// TODO Gerar erros
					e1.printStackTrace();
				}

			}

		});
		runRules.setBounds(372, 201, 174, 26);
		detecaoPanel.add(runRules);

		panelResultados.setBounds(10, 238, 931, 304);
		detecaoPanel.add(panelResultados);
		panelResultados.setEnabled(false);
		JScrollPane sClasses = new JScrollPane();

		panelResultados.addTab("Classes", null, sClasses, null);

		tableModel1 = new DefaultTableModel();
		table1 = new JTable() {
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

		table1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		table1.setModel(tableModel1);

		table1.getTableHeader().setReorderingAllowed(false);
		sClasses.setViewportView(table1);
		JScrollPane sMetodos = new JScrollPane();

		panelResultados.addTab("Methods", null, sMetodos, null);

		tableModel2 = new DefaultTableModel();
		table2 = new JTable() {
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

		table2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		table2.setModel(tableModel2);

		table2.getTableHeader().setReorderingAllowed(false);
		sMetodos.setViewportView(table2);

		avaliacaoRegras = new JPanel();
		tabbedPane.addTab("Avaliação Regras", null, avaliacaoRegras, null);
		avaliacaoRegras.setLayout(null);

		// Avaliação Regras
		JPanel matrizConfusao = new JPanel();
		matrizConfusao.setBounds(26, 233, 354, 66);
		avaliacaoRegras.add(matrizConfusao);
		matrizConfusao.setLayout(new GridLayout(2, 2));

		nVP = new JTextField("- Verdadeiros Positivos");
		nVP.setHorizontalAlignment(JTextField.CENTER);
		nVP.setEditable(false);
		nVP.setBackground(new Color(0, 255, 51));
		matrizConfusao.add(nVP);

		nFP = new JTextField("- Falsos Positivos");
		nFP.setEditable(false);
		nFP.setHorizontalAlignment(JTextField.CENTER);
		nFP.setBackground(new Color(255, 51, 51));
		matrizConfusao.add(nFP);

		nFN = new JTextField("- Falsos Negativos");
		nFN.setEditable(false);
		nFN.setHorizontalAlignment(JTextField.CENTER);
		nFN.setBackground(new Color(204, 0, 0));
		matrizConfusao.add(nFN);

		nVN = new JTextField("- Verdadeiros Negativos");
		nVN.setEditable(false);
		nVN.setHorizontalAlignment(JTextField.CENTER);
		nVN.setBackground(new Color(0, 153, 0));
		matrizConfusao.add(nVN);

		JPanel matrizConfusao_2 = new JPanel();
		matrizConfusao_2.setBounds(564, 233, 354, 66);
		avaliacaoRegras.add(matrizConfusao_2);
		matrizConfusao_2.setLayout(new GridLayout(2, 2));

		mVP = new JTextField("- Verdadeiros Positivos");
		mVP.setHorizontalAlignment(SwingConstants.CENTER);
		mVP.setEditable(false);
		mVP.setBackground(new Color(0, 255, 51));
		matrizConfusao_2.add(mVP);

		mFP = new JTextField("- Falsos Positivos");
		mFP.setHorizontalAlignment(SwingConstants.CENTER);
		mFP.setEditable(false);
		mFP.setBackground(new Color(255, 51, 51));
		matrizConfusao_2.add(mFP);

		mFN = new JTextField("- Falsos Negativos");
		mFN.setHorizontalAlignment(SwingConstants.CENTER);
		mFN.setEditable(false);
		mFN.setBackground(new Color(204, 0, 0));
		matrizConfusao_2.add(mFN);

		mVN = new JTextField("- Verdadeiros Negativos");
		mVN.setHorizontalAlignment(SwingConstants.CENTER);
		mVN.setEditable(false);
		mVN.setBackground(new Color(0, 153, 0));
		matrizConfusao_2.add(mVN);

		JLabel lblNewLabel = new JLabel("Classes");
		lblNewLabel.setBounds(25, 210, 42, 23);
		avaliacaoRegras.add(lblNewLabel);

		textFieldTeoricos = new JTextField();
		textFieldTeoricos.setEditable(false);
		textFieldTeoricos.setBounds(91, 44, 594, 20);
		avaliacaoRegras.add(textFieldTeoricos);
		textFieldTeoricos.setColumns(10);

		JButton buttonDefault = new JButton("Teoricos");
		buttonDefault.setEnabled(false);
		buttonDefault.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xlsx");
				jfc.setFileFilter(filter);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					csDefault = jfc.getSelectedFile().getAbsolutePath();
					textFieldTeoricos.setText(csDefault);
				}
			}
		});

		JLabel lblMtodos = new JLabel("Métodos");
		lblMtodos.setBounds(563, 210, 51, 23);
		avaliacaoRegras.add(lblMtodos);
		buttonDefault.setBounds(709, 43, 109, 23);
		avaliacaoRegras.add(buttonDefault);

		textFieldCreated = new JTextField();
		textFieldCreated.setEditable(false);
		textFieldCreated.setColumns(10);
		textFieldCreated.setBounds(91, 75, 594, 20);
		avaliacaoRegras.add(textFieldCreated);

		JButton buttonCreated = new JButton("Sem Bool");
		buttonCreated.setEnabled(false);
		buttonCreated.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xlsx");
				jfc.setFileFilter(filter);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					csCreated = jfc.getSelectedFile().getAbsolutePath();
					textFieldCreated.setText(csCreated);
				}
			}
		});
		buttonCreated.setBounds(709, 74, 109, 23);
		avaliacaoRegras.add(buttonCreated);

		textFieldMetricas = new JTextField();
		textFieldMetricas.setEditable(false);
		textFieldMetricas.setColumns(10);
		textFieldMetricas.setBounds(91, 107, 594, 20);
		avaliacaoRegras.add(textFieldMetricas);

		JButton metricsButton = new JButton("Metrics");
		metricsButton.setEnabled(false);
		metricsButton.setBounds(709, 106, 109, 23);
		metricsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xlsx");
				jfc.setFileFilter(filter);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					metricsFile = jfc.getSelectedFile().getAbsolutePath();
					textFieldMetricas.setText(metricsFile);

				}
			}
		});
		avaliacaoRegras.add(metricsButton);

		textFieldRules = new JTextField();
		textFieldRules.setEditable(false);
		textFieldRules.setColumns(10);
		textFieldRules.setBounds(91, 139, 594, 20);
		avaliacaoRegras.add(textFieldRules);

		JButton rulesButton = new JButton("Rules");
		rulesButton.setEnabled(false);
		rulesButton.setBounds(709, 138, 109, 23);
		rulesButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc = new JFileChooser(".");
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt files", "txt");
				jfc.setFileFilter(filter);
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					rulesFile = jfc.getSelectedFile().getAbsolutePath();
					textFieldRules.setText(rulesFile);
				}
			}
		});
		avaliacaoRegras.add(rulesButton);

		JButton buttonAvaliar = new JButton("Avaliar");

		ButtonGroup buttonGroup = new ButtonGroup();

		JRadioButton compare2 = new JRadioButton("Comparar 2");
		compare2.setBounds(186, 14, 109, 23);
		compare2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				buttonDefault.setEnabled(true);
				buttonCreated.setEnabled(true);
				metricsButton.setEnabled(false);
				textFieldMetricas.setText("");
				rulesButton.setEnabled(false);
				textFieldRules.setText("");
				buttonAvaliar.setEnabled(true);
			}
		});
		avaliacaoRegras.add(compare2);
		buttonGroup.add(compare2);

		JRadioButton compare3 = new JRadioButton("Comparar 3");
		compare3.setBounds(576, 14, 109, 23);
		compare3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				buttonDefault.setEnabled(true);
				buttonCreated.setEnabled(false);
				textFieldCreated.setText("");
				metricsButton.setEnabled(true);
				rulesButton.setEnabled(true);
				buttonAvaliar.setEnabled(true);
			}
		});
		avaliacaoRegras.add(compare3);
		buttonGroup.add(compare3);

		buttonAvaliar.setEnabled(false);
		buttonAvaliar.setBounds(417, 170, 89, 23);
		buttonAvaliar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					if (compare2.isSelected()) {
						CompareFiles comparef = new CompareFiles(csDefault, csCreated);
						Quality quality = comparef.testQuality(new String[] { "is_God_Class", "is_Long_Method" });
						classesMap = quality.getIndicatorsPerClass();
						methodsMap = quality.getIndicatorsPerMethod();
						updateEvaluationInfo();

					}
					if (compare3.isSelected()) {
						CompareFiles comparef = new CompareFiles(csDefault, metricsFile, rulesFile);
						Quality quality = comparef.testQuality(new String[] { "is_God_Class", "is_Long_Method" });
						classesMap = quality.getIndicatorsPerClass();
						methodsMap = quality.getIndicatorsPerMethod();
						updateEvaluationInfo();
					}
				} catch (Exception e) {
					// TODO Gerar erro e simplificar
					e.printStackTrace();
				}
			}
		});
		avaliacaoRegras.add(buttonAvaliar);

	}

	private void readExcel(String path ) {
		try {
			fileName_TF.setText(path);
			tableModel.setRowCount(0);
			tableModel.setColumnCount(0);

			Object[] header = ExcelDealer.getRow(path, 0, 0);
			tableModel.setColumnIdentifiers(header);

			for (int i = 0; i != header.length; i++)
				table.getColumnModel().getColumn(i).setResizable(false);

			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			for (Object[] row : ExcelDealer.getAllRows(path, 0)) {
				tableModel.addRow(row);
			}

			// TODO caso o projeto tenha classes com o mesmo nome dá pedrinho aka coco
			Label_Classes.setText(String.valueOf(ExcelDealer.getAllCellsOfColumn(path, 0, 2, false).size() - 1));
			Label_Packages.setText(String.valueOf(ExcelDealer.getAllCellsOfColumn(path, 0, 1, false).size() - 1));
			Label_LOC.setText(String.valueOf(ExcelDealer.sumAllColumn(path, 0, 7)));
			Label_Methods.setText(String.valueOf(ExcelDealer.getAllCellsOfColumn(path, 0, 3, true).size() - 1));
		} catch (Exception e) {
			// TODO Gerar erro
			e.printStackTrace();
		}

	}

	private void readCodeSmells(ArrayList<ArrayList<String[]>> list) {

		tableModel1.setRowCount(0);
		tableModel1.setColumnCount(0);
		tableModel2.setRowCount(0);
		tableModel2.setColumnCount(0);
		for (int i = 0; i < list.get(0).get(0).length; i++) {
			tableModel1.setColumnIdentifiers(list.get(0).get(0));
			for (int j = 0; j != list.get(0).get(0).length; j++) {
				table1.getColumnModel().getColumn(j).setResizable(false);
			}
		}
		for (int i = 0; i < list.get(1).get(0).length; i++) {
			tableModel2.setColumnIdentifiers(list.get(1).get(0));
			for (int j = 0; j != list.get(1).get(0).length; j++) {
				table2.getColumnModel().getColumn(j).setResizable(false);
			}

		}

		table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table2.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		for (int i = 1; i < list.get(0).size(); i++) {
			tableModel1.addRow(list.get(0).get(i));
		}

		for (int i = 1; i < list.get(1).size(); i++) {
			tableModel2.addRow(list.get(1).get(i));
		}

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
		write.add("is_Long_Method");
		write.add(getLongMethodFormat());
		write.add("is_God_Class");
		write.add(getGodClassFormat());
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
		System.out.println(conditionsRule);
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

	private void updateEvaluationInfo() {
		if (chartFrame != null && chartFrame2 != null) {
			avaliacaoRegras.remove(chartFrame);
			avaliacaoRegras.remove(chartFrame2);
		}

		int cVP = 0;
		int cVN = 0;
		int cFP = 0;
		int cFN = 0;
		int mVPs = 0;
		int mVNs = 0;
		int mFPs = 0;
		int mFNs = 0;

		for (String key : classesMap.keySet()) {
			Indicator indy = classesMap.get(key);
			System.out.println("Na GUI " + indy);
			switch (indy) {
			case VP:
				cVP = cVP + 1;
				break;
			case VN:
				cVN = cVN + 1;
				break;
			case FP:
				cFP = cFP + 1;
				System.out.println("Encontrei " + cFP + " FP");
				break;
			case FN:
				cFN = cFN + 1;
				System.out.println("Encontrei " + cFN + " FN");
				break;
			}
		}

		for (String key : methodsMap.keySet()) {
			Indicator indy = methodsMap.get(key);
			System.out.println("Na GUI " + indy);
			switch (indy) {
			case VP:
				mVPs = mVPs + 1;
				break;
			case VN:
				mVNs = mVNs + 1;
				break;
			case FP:
				mFPs = mFPs + 1;
				break;
			case FN:
				mFNs = mFNs + 1;
				break;
			}
		}

		nVP.setText(cVP + " Verdadeiros Positivos");
		nVN.setText(cVN + " Verdadeiros Negativos");
		nFP.setText(cFP + " Falsos Positivos");
		nFN.setText(cFN + " Falsos Negativos");

		mVP.setText(mVPs + " Verdadeiros Positivos");
		mVN.setText(mVNs + " Verdadeiros Negativos");
		mFP.setText(mFPs + " Falsos Positivos");
		mFN.setText(mFNs + " Falsos Negativos");

		pieDataSet = new DefaultPieDataset();
		JFreeChart chart = ChartFactory.createPieChart("", pieDataSet, true, true, true);
		PiePlot plot = (PiePlot) chart.getPlot();

		if (cVP > 0) {
			pieDataSet.setValue("VP", cVP);
		}

		if (cVN > 0) {
			pieDataSet.setValue("VN", cVN);
		}

		if (cFP > 0) {
			pieDataSet.setValue("FP", cFP);
		}
		if (cFN > 0) {
			pieDataSet.setValue("FN", cFN);
		}

		if (mVPs > 0) {
			pieDataSet.setValue("VP", mVPs);
		}

		if (mVNs > 0) {
			pieDataSet.setValue("VN", mVNs);
		}

		if (mFPs > 0) {
			pieDataSet.setValue("FP", mFPs);
		}
		if (mFNs > 0) {
			pieDataSet.setValue("FN", mFNs);
		}

		plot.setSectionPaint("VP", new Color(0, 255, 51));
		plot.setSectionPaint("VN", new Color(0, 153, 0));
		plot.setSectionPaint("FP", new Color(255, 51, 51));
		plot.setSectionPaint("FN", new Color(204, 0, 0));

		chartFrame = new ChartPanel(chart);
		chartFrame.setBounds(26, 310, 354, 234);
		chartFrame.setVisible(true);

		avaliacaoRegras.add(chartFrame);

		pieDataSet2 = new DefaultPieDataset();
		pieDataSet2.setValue("VP", mVPs);
		pieDataSet2.setValue("VN", mVNs);
		pieDataSet2.setValue("FP", mFPs);
		pieDataSet2.setValue("FN", mFNs);

		JFreeChart chart2 = ChartFactory.createPieChart("", pieDataSet2, true, true, true);
		PiePlot plot2 = (PiePlot) chart2.getPlot();
		plot2.setSectionPaint("VP", new Color(0, 255, 51));
		plot2.setSectionPaint("VN", new Color(0, 153, 0));
		plot2.setSectionPaint("FP", new Color(255, 51, 51));
		plot2.setSectionPaint("FN", new Color(204, 0, 0));

		chartFrame2 = new ChartPanel(chart2);
		chartFrame2.setBounds(563, 310, 354, 234);
		chartFrame2.setVisible(true);

		avaliacaoRegras.add(chartFrame2);

	}
}
