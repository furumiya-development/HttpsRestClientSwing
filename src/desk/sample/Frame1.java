package desk.sample;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Frame1 extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel panel = new JPanel();
	private JTable table1;
	private JScrollPane Scroll1;
	private JScrollPane Scroll2;
	private DefaultTableModel DtableModel;
	private String[] headers = {"商品ID", "商品番号", "商品名", "編集日付", "編集時刻", "備考"};
	private JComboBox<String> CmbHttpVer = new JComboBox<>();
	private JComboBox<String> CmbSslProtocol = new JComboBox<>();
	private JTextArea LabelArea = new JTextArea();
	private JButton ButtonQuery = new JButton();
	private JButton ButtonInsert = new JButton();
	private JButton ButtonUpdate = new JButton();
	private JButton ButtonDelete = new JButton();
	private Map<String, JLabel> labelMap = new HashMap<>();
	private Map<String, JTextField> textMap = new HashMap<>();
	private Map<String, JButton> buttonMap = new HashMap<>();
	
	public Frame1() {
		createWindow();
	}
	
	private JComponent controlSetting(JComponent ctl, String txt, int x, int y, int w, int h) {
		//ctl.setText(txt);
		ctl.setBounds(x,y,w,y);
		return ctl;
	}
	
	private JLabel labelSetting(String name, String txt, int x, int y, int w, int h) {
		var label = new JLabel();
		
		label.setName(name);
		labelMap.put(label.getName(), label);
		label.setText(txt);
		label.setLocation(x, y);
		label.setSize(w, h);
		panel.add(label);
		
		return label;
	}
	
	private JTextField textSetting(String name, String txt, int x, int y, int w, int h) {
		var text = new JTextField();
		
		text.setName(name);
		textMap.put(text.getName(), text);
		text.setText(txt);
		text.setLocation(x, y);
		text.setSize(w, h);
		panel.add(text);
		
		return text;
	}
	
	private JButton buttonSetting(JButton btn, String name, String txt, int x, int y, int w, int h) {
		
		btn.setName(name);
		buttonMap.put(btn.getName(), btn);
		btn.setText(txt);
		btn.setLocation(x, y);
		btn.setSize(w, h);
		panel.add(btn);
		
		return btn;
	}
	
	private void createWindow() {
		this.setTitle("RESTful API クライアント(HttpClient)");
		this.setLocation(500,200);
		this.setSize(800, 600);
		this.setUndecorated(false); // タイトルバー表示・非表示
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DtableModel = new DefaultTableModel(headers, 0);
		table1 = new JTable(DtableModel);
		Scroll1 = new JScrollPane(table1);
		Scroll1.setBounds(25,25,730,200);
		panel.setPreferredSize(new Dimension(700, 100));
		panel.add(Scroll1);
		
		labelSetting("labelUri", "URI:", 25, 235, 50, 25);
		textSetting("textUri", "", 65, 235, 300, 25);
		
		labelSetting("labelHttpVer", "HTTPバージョン：", 375, 235, 110, 25);
		CmbHttpVer.addItem(HttpSettings.httpVersion[0]);
		CmbHttpVer.addItem(HttpSettings.httpVersion[1]);
		CmbHttpVer.addItem(HttpSettings.httpVersion[2]);
		CmbHttpVer.setBounds(485,235,80,25);
		panel.add(CmbHttpVer);
		
		labelSetting("labelSslProtocol", "SSLプロトコル：", 575, 235, 100, 25);
		CmbSslProtocol.addItem(HttpSettings.sslProtocol[0]);
		CmbSslProtocol.addItem(HttpSettings.sslProtocol[1]);
		CmbSslProtocol.addItem(HttpSettings.sslProtocol[2]);
		CmbSslProtocol.addItem(HttpSettings.sslProtocol[3]);
		CmbSslProtocol.addItem(HttpSettings.sslProtocol[4]);
		CmbSslProtocol.setBounds(675,235,80,25);
		panel.add(CmbSslProtocol);
		
		labelSetting("labelReqBody", "レスポンスBody：", 25, 260, 100, 25);
		textSetting("textReqBody", "", 130, 260, 625, 25);
		LabelArea.setText("");
		Scroll2 = new JScrollPane(LabelArea);
		Scroll2.setBounds(25,290,350,200);
		LabelArea.setFocusable(false);
		panel.add(Scroll2);
		
		labelSetting("label1", "商品ID：", 400, 300, 100, 25);
		labelSetting("label2", "商品番号：", 400, 350, 100, 25);
		labelSetting("label3", "商品名：", 400, 400, 100, 25);
		labelSetting("label4", "備考：", 400, 450, 100, 25);
		
		labelSetting("labelNumId", "", 700, 300, 50,25);
		labelMap.get("labelNumId").setHorizontalAlignment(JLabel.RIGHT);
		textSetting("textShohinNum", "", 600,350, 150, 25);
		textSetting("textShohinName", "", 550,400, 200, 25);
		textSetting("textNote", "", 450,450, 300, 25);
		
		buttonSetting(ButtonQuery, "buttonQuery", "抽出(GET)", 25, 490, 150, 50);
		buttonSetting(ButtonInsert, "buttonInsert", "追加(POST)", 220, 490, 150, 50);
		buttonSetting(ButtonUpdate, "buttonUpdate", "更新(Update)", 410, 490, 150, 50);
		buttonSetting(ButtonDelete, "buttonDelete", "削除(Delete)", 600, 490, 150, 50);
		
		panel.setLayout(null);
	}
	
	public void getTableRowSetTextField() {
		labelMap.get("labelNumId").setText(((Integer) table1.getValueAt(table1.getSelectedRow(), 0)).toString());
		textMap.get("textShohinNum").setText(((Short) table1.getValueAt(table1.getSelectedRow(), 1)).toString());
		textMap.get("textShohinName").setText(table1.getValueAt(table1.getSelectedRow(), 2).toString());
		textMap.get("textNote").setText(table1.getValueAt(table1.getSelectedRow(), 5).toString());
	}
	
	public void textFieldClear() {
		labelMap.get("labelNumId").setText("");
		textMap.get("textShohinNum").setText("");
		textMap.get("textShohinName").setText("");
		textMap.get("textNote").setText("");
	}
	
	public void tableSetting() {
		table1.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		TableColumn col = table1.getColumnModel().getColumn(0);
		col.setPreferredWidth(5);
		col = table1.getColumnModel().getColumn(1);
		col.setPreferredWidth(5);
		col = table1.getColumnModel().getColumn(2);
		col.setPreferredWidth(20);
		col = table1.getColumnModel().getColumn(3);
		col.setPreferredWidth(5);
		col = table1.getColumnModel().getColumn(4);
		col.setPreferredWidth(5);
		/**col = table1.getColumnModel().getColumn(5);
		col.setPreferredWidth(35);**/
	}
	
	public void showDialog(String msg, String title, int msgType) {
		JOptionPane.showMessageDialog(this, msg , title, msgType);
	}
	
	public JPanel getFpanel() {
		return panel;
	}
	
	public JTable getTable1() {
		return table1;
	}
	public DefaultTableModel getDtableModel() {
		return DtableModel;
	}
	public JTextField getTextUri() {
		return textMap.get("textUri");
	}
	public JTextField getTextReqBody() {
		return textMap.get("textReqBody");
	}
	
	public JTextArea getLabelArea() {
		return LabelArea;
	}
	
	public JLabel getLabelNumId() {
		return labelMap.get("labelNumId");
	}
	public JTextField getTextShohinNum() {
		return textMap.get("textShohinNum");
	}
	public JTextField getTextShohinName() {
		return textMap.get("textShohinName");
	}
	public JTextField getTextNote() {
		return textMap.get("textNote");
	}
	
	public JButton getButtonQuery() {
		return ButtonQuery;
	}
	public JButton getButtonInsert() {
		return ButtonInsert;
	}
	public JButton getButtonUpdate() {
		return ButtonUpdate;
	}
	public JButton getButtonDelete() {
		return ButtonDelete;
	}
}