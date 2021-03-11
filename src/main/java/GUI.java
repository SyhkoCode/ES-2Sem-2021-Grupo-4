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
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ScrollPaneConstants;

public class GUI extends JFrame {

	private ExcelDealer dealer;
	private JPanel contentPane;
	private JTextField fileName_TF;
	private JTable table;
	private DefaultTableModel tableModel;

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
		setBounds(100, 100, 1000, 600);
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
				}
			}
		});
		btnChooseFile.setBounds(820, 506, 150, 30);
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
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tabbedPane.addTab("Excel", null, scrollPane, null);
		
		tableModel = new DefaultTableModel();
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 15));
		table.setModel(tableModel);

		table.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(table);
	}
}
