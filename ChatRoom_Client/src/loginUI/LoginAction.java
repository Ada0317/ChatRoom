package loginUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import client.ChatClient;
import dataBase.Figures;
import friendListUI.FriendListUI;


/**
 * LoginAction
 * ��½�ڼ䰴��������
 * ��½��ע�ᶼ���ô˼�����
 * ���Լ���������ť
 * ʵ�ֵ�½��ע�ᡢ�ύע����Ϣ�Ĺ���
 * @author He11o_Liu
 *
 */
public class LoginAction implements ActionListener {

	private JTextField userid_field;// Login�����ID��
	private JPasswordField password_field;// Login���������
	private JTextField NikeName;// Register������ǳ�
	private JTextField Rpassword;// Register���������
	private ChatClient cc; //LoginUI���������������ӷ�������ChatClient
	//private RegisterUI ru;
	//private boolean is_Registering = false;// ������������ж��Ƿ��Ѿ�����ע�����
	public static JFrame LoginJF;//��½�����Լ�ע������JF ���ڹرմ���

	public ChatClient getCc() {
		return cc;
	}

	public void setCc(ChatClient cc) {
		this.cc = cc;
	}

	public JTextField getUsername() {
		return userid_field;
	}

	public void setUsername(JTextField username) {
		this.userid_field = username;
	}

	public JPasswordField getPassword() {
		return password_field;
	}

	public void setPassword(JPasswordField password) {
		this.password_field = password;
	}

	public JTextField getNikeName() {
		return NikeName;
	}

	public void setNikeName(JTextField nikeName) {
		NikeName = nikeName;
	}

	public JTextField getRpassword() {
		return Rpassword;
	}

	public void setRpassword(JTextField rpassword) {
		Rpassword = rpassword;
	}

	/**
	 * ����������
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton jb = (JButton) e.getSource();
		//�����µİ���Ϊ��½
		if (jb.getText().equals("Login")) {
			int userid = Integer.valueOf(userid_field.getText());
			if (userid_field.getText().equals("")) {//������ID��Ϊ��
				JOptionPane.showMessageDialog(null, "ID��Ϊ��", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				String password = new String(password_field.getPassword());
				if (password.equals(""))
					JOptionPane.showMessageDialog(null, "���벻Ϊ��", "Error", JOptionPane.ERROR_MESSAGE);
				else if (cc.Login(userid, password)) {//��������ȷ
					Figures.cc = cc;
					new FriendListUI();
					LoginJF.dispose();
				}
				else {//���������
					JOptionPane.showMessageDialog(null, "�û������������", "Error", JOptionPane.ERROR_MESSAGE);
					password_field.setText("");
				}
			}
		} 
		//�����µİ���Ϊע�ᰴ��
		/*
		else if (jb.getText().equals("Register")) {
			if (!is_Registering) {
				ru = new RegisterUI(this); // ����ǰ����������ȥ
				is_Registering = true;
			}
		} else if (jb.getText().equals("ע��")) {
			if (!cc.Reg(NikeName.getText(), Rpassword.getText())) {
				JOptionPane.showMessageDialog(null, "ע��ʧ��", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				ru.dispose();
				is_Registering = false;// ���Դ�ע�ᴰ��
			}
		}
		*/
	}

}
