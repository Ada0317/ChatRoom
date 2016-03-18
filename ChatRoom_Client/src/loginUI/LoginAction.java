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

/*
 * 
 * ��½��ע�ᶼ���ô˼�����
 * ���Լ���������ť
 * ʵ�ֵ�½��ע�ᡢ�ύע����Ϣ�Ĺ���
 * 
 */
public class LoginAction implements ActionListener {

	private JTextField JKarea;// �����Login�����JK��
	private JPasswordField password;// �����Login���������
	private JTextField NikeName;// �����Register������ǳ�
	private JTextField Rpassword;// �����Register���������
	private ChatClient cc;
	private RegisterUI ru;

	/*
	 * �����ǵ�½�����Լ�ע������JF ���ڹرմ���
	 */
	public static JFrame LoginJF;

	private int RegisterNum = 0;// ������������ж��Ƿ��Ѿ�����ע�����

	public ChatClient getCc() {
		return cc;
	}

	public void setCc(ChatClient cc) {
		this.cc = cc;
	}

	public JTextField getUsername() {
		return JKarea;
	}

	public void setUsername(JTextField username) {
		this.JKarea = username;
	}

	public JPasswordField getPassword() {
		return password;
	}

	public void setPassword(JPasswordField password) {
		this.password = password;
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

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton jb = (JButton) e.getSource();
		int JKNum;
		if (jb.getText().equals("Login")) {
			String SJK = JKarea.getText();
			if (SJK.equals("")) {
				JOptionPane.showMessageDialog(null, "�û�����Ϊ��", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				JKNum = Integer.valueOf(SJK).intValue();
				String passwordS = new String(password.getPassword());
				if (passwordS.equals("")) {
					JOptionPane.showMessageDialog(null, "���벻Ϊ��", "Error", JOptionPane.ERROR_MESSAGE);
				}

				/*
				 * ��½�ɹ�
				 */

				else if (cc.Login(JKNum, passwordS)) {
					Figures.cc = cc; //����½��ȥ��ChatClient�㲻�ᷢ���ı䣬ר��������������û���ع���
					new FriendListUI();
					LoginJF.dispose();
				}

				/*
				 * ��½ʧ��
				 */

				else {
					JOptionPane.showMessageDialog(null, "�û������������", "Error", JOptionPane.ERROR_MESSAGE);
					password.setText("");
				}
			}

		} else if (jb.getText().equals("Register")) {
			if (RegisterNum == 0) {// �����������ע�ᴰ��
				ru = new RegisterUI(this); // ����ǰ����������ȥ
				RegisterNum++;
			}
		} else if (jb.getText().equals("ע��")) {
			System.out.println("");
			if (!cc.Reg(NikeName.getText(), Rpassword.getText())) {
				JOptionPane.showMessageDialog(null, "ע��ʧ��", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				ru.dispose();
				RegisterNum--;// ���Դ�ע�ᴰ��
			}
		}
	}

}
