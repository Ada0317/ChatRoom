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
 * 登陆期间按键监听器
 * 登陆和注册都公用此监听器
 * 用以监听三个按钮
 * 实现登陆、注册、提交注册信息的功能
 * @author He11o_Liu
 *
 */
public class LoginAction implements ActionListener {

	private JTextField userid_field;// Login界面的ID号
	private JPasswordField password_field;// Login界面的密码
	private JTextField NikeName;// Register界面的昵称
	private JTextField Rpassword;// Register界面的密码
	private ChatClient cc; //LoginUI传过来的用于连接服务器的ChatClient
	//private RegisterUI ru;
	//private boolean is_Registering = false;// 这个参数用于判断是否已经打开了注册界面
	public static JFrame LoginJF;//登陆界面以及注册界面的JF 用于关闭窗口

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
	 * 按键监听器
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton jb = (JButton) e.getSource();
		//若按下的按键为登陆
		if (jb.getText().equals("Login")) {
			int userid = Integer.valueOf(userid_field.getText());
			if (userid_field.getText().equals("")) {//若输入ID号为空
				JOptionPane.showMessageDialog(null, "ID不为空", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				String password = new String(password_field.getPassword());
				if (password.equals(""))
					JOptionPane.showMessageDialog(null, "密码不为空", "Error", JOptionPane.ERROR_MESSAGE);
				else if (cc.Login(userid, password)) {//若密码正确
					Figures.cc = cc;
					new FriendListUI();
					LoginJF.dispose();
				}
				else {//若密码错误
					JOptionPane.showMessageDialog(null, "用户名或密码错误", "Error", JOptionPane.ERROR_MESSAGE);
					password_field.setText("");
				}
			}
		} 
		//若按下的按键为注册按键
		/*
		else if (jb.getText().equals("Register")) {
			if (!is_Registering) {
				ru = new RegisterUI(this); // 将当前监听器传过去
				is_Registering = true;
			}
		} else if (jb.getText().equals("注册")) {
			if (!cc.Reg(NikeName.getText(), Rpassword.getText())) {
				JOptionPane.showMessageDialog(null, "注册失败", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				ru.dispose();
				is_Registering = false;// 可以打开注册窗口
			}
		}
		*/
	}

}
