import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.ColorUIResource;

import javax.swing.JFileChooser;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class GUI extends JFrame {

	private ExcelDealer dealer;
	private String pathToSave = "";

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
	private JScrollPane scrollPane_1;

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

		scrollPane_1 = new JScrollPane();
		tabbedPane.addTab("Regras", null, scrollPane_1, null);

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
	
}
