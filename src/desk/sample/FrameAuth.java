package desk.sample;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;

import java.util.Map;
import java.util.HashMap;

public class FrameAuth extends JDialog {
	private static final long serialVersionUID = 1L;
	private Map<String, JLabel> labelMap = new HashMap<>();
	private JPanel panel = new JPanel();
    private JTextField textUserName = new JTextField();
    private JPasswordField textPassword = new JPasswordField();
    private JButton buttonAuth = new JButton();
    private JButton buttonCancel = new JButton();
    
    public FrameAuth() {
    	FrameDesignSetting();
    }
    
    private JLabel labelsSetting(String name, String txt, int x, int y, int w, int h)
    {
        JLabel label = new JLabel();
        label.setName(name);
        labelMap.put(label.getName(), label);
        label.setText(txt);
        label.setBounds(x, y, w, h);
        panel.add(label);

        return label;
    }
    
    private JComponent controlsSetting(JComponent ctl, String name, int x, int y, int w, int h) {
    	ctl.setName(name);
		ctl.setBounds(x,y,w,h);
		panel.add(ctl);
		
		return ctl;
	}
    
    private void FrameDesignSetting()
    {
    	this.setName("FrameAuth");
        this.setTitle("認証");
        this.setBounds(600, 250, 450, 250);
        this.setUndecorated(false); // タイトルバー表示・非表示
        this.setModal(true);
		//this.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
        
        labelsSetting("labelMessage", "認証が必要です。ユーザー名とパスワードを入力して下さい。", 25, 25, 350, 25);
        labelsSetting("labelUserName", "ユーザー:", 25, 50, 80, 25);
        labelsSetting("labelPassword", "パスワード:", 25, 100, 80, 25);

        textUserName = (JTextField)controlsSetting(textUserName, "textBoxUserName", 120, 50, 250, 25);
        
        textPassword = (JPasswordField)controlsSetting(textPassword, "textBoxPassword", 120, 100, 250, 25);

        buttonAuth = (JButton)controlsSetting(buttonAuth, "buttonAuth", 25, 150, 150, 50);
        this.buttonAuth.setText("認証");
       
        buttonCancel = (JButton)controlsSetting(buttonCancel, "buttonCancel", 250, 150, 150, 50);
        this.buttonCancel.setText("キャンセル");
        
        panel.setLayout(null);
    }
    
    public JPanel getPanel() {
    	return this.panel;
    }
    
    public JTextField getTextUserName() {
    	return this.textUserName;
    }
    
    public JPasswordField getTextPassword() {
    	return this.textPassword;
    }
    
    public JButton getButtonAuth() {
		return this.buttonAuth;
	}
    public JButton getButtonCancel() {
		return this.buttonCancel;
	}
}