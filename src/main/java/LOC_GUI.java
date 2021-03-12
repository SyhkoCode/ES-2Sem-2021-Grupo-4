import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableModel;

public class LOC_GUI extends JFrame {
  private JButton browseButton;
  
  private JLabel classcountlabel;
  
  private JScrollPane jScrollPane1;
  
  private JLabel loclabel;
  
  private JLabel packagecountlabel;
  
  private JTable resultTable;
  
  private JTextField srcPathText;
  
  private JLabel statusLabel;
  
  private JButton submitButton;
  
  public LOC_GUI() {
    initComponents();
    this.jScrollPane1.setVisible(false);
    this.statusLabel.setVisible(false);
  }
  
  private void initComponents() {
    this.srcPathText = new JTextField();
    this.browseButton = new JButton();
    this.submitButton = new JButton();
    this.jScrollPane1 = new JScrollPane();
    this.resultTable = new JTable();
    this.loclabel = new JLabel();
    this.packagecountlabel = new JLabel();
    this.classcountlabel = new JLabel();
    this.statusLabel = new JLabel();
    setDefaultCloseOperation(3);
    setTitle("CodeQualityAssessor");
    setBackground(new Color(255, 255, 255));
    setLocationByPlatform(true);
    setName("frame");
    setResizable(false);
    this.srcPathText.setEditable(false);
    this.srcPathText.setFont(new Font("Verdana", 1, 12));
    this.srcPathText.setText("Browse Your Java Project Src Directory Here");
    this.srcPathText.setName("srcPathText");
    this.browseButton.setFont(new Font("Verdana", 1, 12));
    this.browseButton.setText("Browse");
    this.browseButton.setBorder(BorderFactory.createBevelBorder(0));
    this.browseButton.setName("browseButton");
    this.browseButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            LOC_GUI.this.browseButtonActionPerformed(evt);
          }
        });
    this.submitButton.setFont(new Font("Verdana", 1, 12));
    this.submitButton.setText("Submit");
    this.submitButton.setBorder(BorderFactory.createBevelBorder(0));
    this.submitButton.setName("submitButton");
    this.submitButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            LOC_GUI.this.submitButtonActionPerformed(evt);
          }
        });
    this.jScrollPane1.setName("jScrollPane1");
    this.resultTable.setBorder(BorderFactory.createBevelBorder(0));
    this.resultTable.setFont(new Font("Verdana", 1, 12));
    this.resultTable.setModel(new DefaultTableModel(new Object[][] { { null, null }, { null, null },  }, (Object[])new String[] { "Java Class or Package Name", "LOC" }) {
          Class[] types = new Class[] { String.class, String.class };
          
          boolean[] canEdit = new boolean[] { false, false };
          
          public Class getColumnClass(int columnIndex) {
            return this.types[columnIndex];
          }
          
          public boolean isCellEditable(int rowIndex, int columnIndex) {
            return this.canEdit[columnIndex];
          }
        });
    this.resultTable.setName("resultTable");
    this.jScrollPane1.setViewportView(this.resultTable);
    this.loclabel.setFont(new Font("Verdana", 1, 12));
    this.loclabel.setHorizontalAlignment(2);
    this.loclabel.setName("loclabel");
    this.packagecountlabel.setFont(new Font("Verdana", 1, 12));
    this.packagecountlabel.setHorizontalAlignment(2);
    this.packagecountlabel.setName("packagecountlabel");
    this.classcountlabel.setFont(new Font("Verdana", 1, 12));
    this.classcountlabel.setHorizontalAlignment(2);
    this.classcountlabel.setName("classcountlabel");
    this.statusLabel.setFont(new Font("Verdana", 1, 12));
    this.statusLabel.setHorizontalAlignment(2);
    this.statusLabel.setName("statusLabel");
    GroupLayout layout = new GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane1, GroupLayout.Alignment.TRAILING, -1, 604, 32767).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.loclabel, -2, 288, -2)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.packagecountlabel, -2, 288, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, 32767).addComponent(this.statusLabel, -2, 273, -2)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.srcPathText, -2, 424, -2).addGap(34, 34, 34).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.browseButton, -1, 136, 32767).addComponent(this.submitButton, -1, 136, 32767))).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.classcountlabel, -2, 288, -2))).addContainerGap()));
    layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(this.browseButton, -1, -1, 32767).addComponent(this.srcPathText, -1, 40, 32767)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.submitButton, -2, 39, -2).addGap(15, 15, 15).addComponent(this.loclabel, -2, 23, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.packagecountlabel, -2, 23, -2).addComponent(this.statusLabel, -2, 23, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767).addComponent(this.classcountlabel, -2, 23, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.jScrollPane1, -2, 283, -2).addContainerGap()));
    pack();
  }
  
  private void browseButtonActionPerformed(ActionEvent evt) {
    JFileChooser ch = new JFileChooser();
    ch.setFileSelectionMode(1);
    int result = ch.showOpenDialog(this);
    if (result == 0)
      this.srcPathText.setText(ch.getSelectedFile().getAbsolutePath()); 
  }
  
  private void submitButtonActionPerformed(ActionEvent evt) {
    this.statusLabel.setText("Processing...");
    this.submitButton.setEnabled(false);
    this.statusLabel.setVisible(true);
    ArrayList<String> list = new ArrayList<String>();
    list.add("0");
    LOC.setList(list);
    LOC.setScrPath(this.srcPathText.getText());
    String[] col = { "Java Class \\ Package Name", "LOC" };
    try {
      LOC.getTotalLines(new File(LOC.getScrPath()), list);
      String locount = list.get(0);
      Object[][] row = new Object[list.size()][2];
      int packagecount = 0, classcount = 0;
      for (int i = 1; i < list.size(); i++) {
        String temp = list.get(i);
        StringTokenizer st = new StringTokenizer(temp, "=");
        String name = st.nextToken();
        String value = st.nextToken();
        if (name.endsWith(".java")) {
          classcount++;
        } else {
          packagecount++;
        } 
        row[i - 1][0] = name;
        row[i - 1][1] = value;
      } 
      DefaultTableModel model = new DefaultTableModel(row, (Object[])col);
      this.jScrollPane1.setVisible(true);
      this.resultTable.setModel(model);
      this.loclabel.setText("Total No Of Lines         : " + locount);
      this.packagecountlabel.setText("Total No Of Packages  : " + packagecount + "");
      this.classcountlabel.setText("Total No Of Classes     : " + classcount + "");
      this.submitButton.setEnabled(true);
      this.statusLabel.setText("Completed.");
    } catch (IOException e) {
      this.statusLabel.setText("Oops.Error Occured...");
      this.submitButton.setEnabled(true);
    } 
  }
  
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
          public void run() {
            (new LOC_GUI()).setVisible(true);
          }
        });
  }
}

