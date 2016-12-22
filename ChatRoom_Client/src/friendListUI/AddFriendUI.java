package friendListUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.ChatClient;
import dataBase.Figures;
import object.CloseButton;
import object.MinimizeButton;
import object.RecButton;

public class AddFriendUI extends JFrame {

	private int xx, yy;
	private boolean isDraging = false;
	private FriendListUI flu;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField list_name;
	private JTextField Friend_JK;

	/**
	 * Create the frame.
	 */
	public AddFriendUI(FriendListUI flu, ChatClient cc) {
		this.flu = flu;
		// 设置无标题栏
		setUndecorated(true);
		Figures.afu = this;

		// 监听鼠标 确保窗体能够拖拽
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isDraging = true;
				xx = e.getX();
				yy = e.getY();
			}

			public void mouseReleased(MouseEvent e) {
				isDraging = false;
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (isDraging) {
					int left = getLocation().x;
					int top = getLocation().y;
					setLocation(left + e.getX() - xx, top + e.getY() - yy);
				}
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 535, 418);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);

		JLabel lblNikename = new JLabel("好友JK号");
		lblNikename.setForeground(Color.WHITE);
		lblNikename.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		lblNikename.setBounds(37, 167, 131, 42);
		contentPane.add(lblNikename);

		JLabel lblPassword = new JLabel("选择列表");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		lblPassword.setBounds(37, 212, 131, 42);
		contentPane.add(lblPassword);

		list_name = new JTextField();
		list_name.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		list_name.setColumns(10);
		list_name.setBorder(null);
		list_name.setBounds(176, 216, 219, 35);
		contentPane.add(list_name);
		// a.setRpassword(password);

		// 设置自制按钮
		CloseButton eb = new CloseButton();
		eb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				flu.setAdding(false);
				dispose();
			}
		});
		int windowWeith = this.getWidth();
		eb.setBounds(windowWeith - 4 - 40, 0, 40, 30);
		contentPane.add(eb);
		MinimizeButton mb = new MinimizeButton(this);
		mb.setBounds(windowWeith - 4 - 80, 0, 40, 30);
		contentPane.add(mb);

		Friend_JK = new JTextField();
		Friend_JK.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		Friend_JK.setColumns(10);
		Friend_JK.setBounds(176, 173, 219, 35);
		Friend_JK.setBorder(null);
		// 只能输入数字
		Friend_JK.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				if (!(keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9)) {
					e.consume(); // 关键，屏蔽掉非法输入
				}
			}
		});
		contentPane.add(Friend_JK);
		// la.setNikeName(NikeName);

		RecButton btnRegister = new RecButton("添加好友");
		btnRegister.setBounds(37, 323, 206, 42);
		contentPane.add(btnRegister);
		btnRegister.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// System.out.println("One click");
				try {
					if("".equals(Friend_JK.getText().toString())&&"".equals(list_name.getText().toString())){
						JOptionPane.showMessageDialog(null, "输入不能为空", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else if (Integer.parseInt(Friend_JK.getText().toString())==Figures.JKNum){
						JOptionPane.showMessageDialog(null, "好友不能是自己", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else{
						cc.SendaddFriend(Integer.parseInt(Friend_JK.getText().toString()), list_name.getText().toString());
						
					}
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		JLabel lblRegisterNewUser = new JLabel("Add Friend");
		lblRegisterNewUser.setForeground(Color.WHITE);
		lblRegisterNewUser.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 57));
		lblRegisterNewUser.setBackground(Color.WHITE);
		lblRegisterNewUser.setBounds(37, 60, 494, 80);
		contentPane.add(lblRegisterNewUser);

		JLabel label_1 = new JLabel("Chat Room");
		label_1.setForeground(Color.WHITE);
		label_1.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 20));
		label_1.setBounds(37, 20, 165, 35);
		contentPane.add(label_1);
		setResizable(false);
		setVisible(true);
	}

	public void showResult(int result) {
		System.out.println("To show Result" + result);
		if (result == 0) {
			JOptionPane.showMessageDialog(null, "添加成功");
			Figures.flu.setAdding(false);
			this.dispose();
		} else if (result == 1){
			JOptionPane.showMessageDialog(null, "查无此人", "Error", JOptionPane.ERROR_MESSAGE);
		} else if (result == 2) {
			JOptionPane.showMessageDialog(null, "已有此好友", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "添加失败", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

}