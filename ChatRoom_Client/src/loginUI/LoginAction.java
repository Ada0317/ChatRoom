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
 * 登陆和注册都公用此监听器
 * 用以监听三个按钮
 * 实现登陆、注册、提交注册信息的功能
 * 
 */
public class LoginAction implements ActionListener {

	private JTextField JKarea;// 这个是Login界面的JK码
	private JPasswordField password;// 这个是Login界面的密码
	private JTextField NikeName;// 这个是Register界面的昵称
	private JTextField Rpassword;// 这个是Register界面的密码
	private ChatClient cc;
	private RegisterUI ru;

	/*
	 * 下面是登陆界面以及注册界面的JF 用于关闭窗口
	 */
	public static JFrame LoginJF;

	private int RegisterNum = 0;// 这个参数用于判断是否已经打开了注册界面

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
				JOptionPane.showMessageDialog(null, "用户名不为空", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				JKNum = Integer.valueOf(SJK).intValue();
				String passwordS = new String(password.getPassword());
				if (passwordS.equals("")) {
					JOptionPane.showMessageDialog(null, "密码不为空", "Error", JOptionPane.ERROR_MESSAGE);
				}

				/*
				 * 登陆成功
				 */

				else if (cc.Login(JKNum, passwordS)) {
					Figures.cc = cc; //当登陆进去后，ChatClient便不会发生改变，专门用来操作与该用户相关功能
					new FriendListUI();
					LoginJF.dispose();
				}

				/*
				 * 登陆失败
				 */

				else {
					JOptionPane.showMessageDialog(null, "用户名或密码错误", "Error", JOptionPane.ERROR_MESSAGE);
					password.setText("");
				}
			}

		} else if (jb.getText().equals("Register")) {
			if (RegisterNum == 0) {// 不允许打开两个注册窗口
				ru = new RegisterUI(this); // 将当前监听器传过去
				RegisterNum++;
			}
		} else if (jb.getText().equals("注册")) {
			System.out.println("");
			if (!cc.Reg(NikeName.getText(), Rpassword.getText())) {
				JOptionPane.showMessageDialog(null, "注册失败", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				ru.dispose();
				RegisterNum--;// 可以打开注册窗口
			}
		}
	}

}
