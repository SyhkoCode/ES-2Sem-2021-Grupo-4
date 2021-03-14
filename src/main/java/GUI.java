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
import java.io.File;
import java.util.List;
import java.awt.Font;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.ScrollPaneConstants;
import javax.swing.JToolBar;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class GUI extends JFrame {

	private ExcelDealer dealer;
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
	 * Create the frame.	 */
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
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xlsx");
				jfc.setFileFilter(filter);
				int returnValue = jfc.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					dealer = new ExcelDealer(jfc.getSelectedFile().getAbsolutePath(),true);
					fileName_TF.setText(dealer.getExcel_file());
					tableModel.setRowCount(0);
					tableModel.setColumnCount(0);
					
					Object[] header = dealer.getHeader();
					tableModel.setColumnIdentifiers(header);
					
					for(int i =0;i!=header.length;i++) 
						table.getColumnModel().getColumn(i).setResizable(false);
					
					for(Object[] row : dealer.getAllRows()) {
						tableModel.addRow(row);
					}
					Label_Classes.setText(String.valueOf((dealer.getClasses().size())));
					Label_Packages.setText(String.valueOf(dealer.getTotal(1).size()));
					Label_LOC.setText(String.valueOf(dealer.getLinesOfCode()));
					Label_Methods.setText(String.valueOf(dealer.getTotal(3).size()));
					Projeto_Label.setVisible(true);
					NClasses_Label.setVisible(true);
					NClasses_Packages.setVisible(true);
					NClasses_LOC.setVisible(true);
					NClasses_Methods.setVisible(true);
					Label_Classes.setVisible(true);
					Label_Packages.setVisible(true);
					Label_Methods.setVisible(true);
					Label_LOC.setVisible(true);
					
				}
			}
		});
		btnChooseFile.setBounds(818, 509, 150, 30);
		contentPane.add(btnChooseFile);

		fileName_TF = new JTextField();
		fileName_TF.setEditable(false);
		fileName_TF.setForeground(Color.BLACK);
		fileName_TF.setSelectedTextColor(Color.WHITE);
		fileName_TF.setFont(new Font("Tahoma", Font.PLAIN, 15));
		fileName_TF.setBounds(12, 510, 796, 30);
		contentPane.add(fileName_TF);
		fileName_TF.setColumns(10);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 13, 958, 480);
		contentPane.add(tabbedPane);

		JScrollPane scrollPane = new JScrollPane();
		tabbedPane.addTab("Excel", null, scrollPane, null);
		
		tableModel = new DefaultTableModel();
		table = new JTable(){
		    @Override
		       public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		           Component component = super.prepareRenderer(renderer, row, column);
		           int rendererWidth = component.getPreferredSize().width;
		           TableColumn tableColumn = getColumnModel().getColumn(column);
		           tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width + 10, tableColumn.getPreferredWidth()));
		           return component;
		        }
		    };
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setFont(new Font("Tahoma", Font.PLAIN, 15));
		table.setModel(tableModel);

		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);
		

		
		Projeto_Label = new JLabel("Resumo do Projeto");
		Projeto_Label.setFont(new Font("Tahoma", Font.PLAIN, 31));
		Projeto_Label.setHorizontalAlignment(SwingConstants.CENTER);
		Projeto_Label.setBounds(12, 546, 958, 47);
		contentPane.add(Projeto_Label);
		Projeto_Label.setVisible(false);
		
		NClasses_Label = new JLabel("Nº de Classes");
		NClasses_Label.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_Label.setFont(new Font("Tahoma", Font.BOLD, 15));
		NClasses_Label.setBounds(363, 583, 106, 30);
		contentPane.add(NClasses_Label);
		NClasses_Label.setVisible(false);
		
		NClasses_Packages = new JLabel("Nº de Packages");
		NClasses_Packages.setFont(new Font("Tahoma", Font.BOLD, 15));
		NClasses_Packages.setBounds(351, 612, 118, 30);
		contentPane.add(NClasses_Packages);
		NClasses_Packages.setVisible(false);
		
		NClasses_Methods = new JLabel("Nº de Métodos");
		NClasses_Methods.setHorizontalAlignment(SwingConstants.LEFT);
		NClasses_Methods.setFont(new Font("Tahoma", Font.BOLD, 15));
		NClasses_Methods.setBounds(511, 612, 118, 30);
		contentPane.add(NClasses_Methods);
		NClasses_Methods.setVisible(false);
		
		NClasses_LOC = new JLabel("Nº de Linhas");
		NClasses_LOC.setFont(new Font("Tahoma", Font.BOLD, 15));
		NClasses_LOC.setBounds(523, 583, 106, 30);
		contentPane.add(NClasses_LOC);
		NClasses_LOC.setVisible(false);
		
		Label_Classes = new JLabel("");
		Label_Classes.setHorizontalAlignment(SwingConstants.LEFT);
		Label_Classes.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Label_Classes.setBounds(471, 583, 30, 30);
		contentPane.add(Label_Classes);
		Label_Classes.setVisible(false);
		
		
		Label_Packages = new JLabel("");
		Label_Packages.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Label_Packages.setBounds(471, 612, 30, 30);
		contentPane.add(Label_Packages);
		Label_Packages.setVisible(false);
		
		Label_Methods = new JLabel("");
		Label_Methods.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Label_Methods.setBounds(624, 612, 30, 30);
		contentPane.add(Label_Methods);
		Label_Methods.setVisible(false);
		
		Label_LOC = new JLabel("");
		Label_LOC.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Label_LOC.setBounds(624, 583, 44, 30);
		contentPane.add(Label_LOC);
		Label_LOC.setVisible(false);
	}

}
