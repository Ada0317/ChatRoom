package loginUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.ChatClient;
import object.CloseButton;
import object.ExitButton;
import object.MinimizeButton;
import object.RecButton;

public class RegisterUI extends JFrame {

	private int xx, yy;
	private boolean isDraging = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField password;
	private JTextField NikeName;
	private JFrame regframe = this;

	/**
	 * Create the frame.
	 */
	public RegisterUI(LoginAction la,ChatClient cc) {

		// 设置无标题栏
		setUndecorated(true);

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

		JLabel lblNikename = new JLabel("NikeName");
		lblNikename.setForeground(Color.WHITE);
		lblNikename.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		lblNikename.setBounds(37, 167, 131, 42);
		contentPane.add(lblNikename);

		JLabel lblPassword = new JLabel("PassWord");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		lblPassword.setBounds(37, 212, 131, 42);
		contentPane.add(lblPassword);

		password = new JTextField();
		password.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		password.setColumns(10);
		password.setBorder(null);
		password.setBounds(176, 219, 219, 35);
		contentPane.add(password);
		//a.setRpassword(password);

		// 设置自制按钮
		CloseButton eb = new CloseButton();
		eb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				la.setIs_Registering(false);
				dispose();
			}
		});
		int windowWeith = this.getWidth();
		eb.setBounds(windowWeith - 4 - 40, 0, 40, 30);
		contentPane.add(eb);
		MinimizeButton mb = new MinimizeButton(this);
		mb.setBounds(windowWeith - 4 - 80, 0, 40, 30);
		contentPane.add(mb);

		// 只能输入数字和字母
		password.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				int keyChar = e.getKeyChar();
				if ((keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9)
						|| (keyChar >= KeyEvent.VK_A && keyChar <= KeyEvent.VK_Z)
						|| (keyChar >= 'a' && keyChar <= 'z')) {

				} else {
					e.consume(); // 关键，屏蔽掉非法输入
				}
			}
		});

		NikeName = new JTextField();
		NikeName.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		NikeName.setColumns(10);
		NikeName.setBounds(176, 173, 219, 35);
		NikeName.setBorder(null);
		contentPane.add(NikeName);
		//la.setNikeName(NikeName);

		JLabel lbljk = new JLabel(
				"*提示：当注册成功，将返回唯一的JK码，请妥善保管。");
		lbljk.setForeground(Color.WHITE);
		lbljk.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lbljk.setBounds(37, 268, 372, 28);
		contentPane.add(lbljk);

		RecButton btnRegister = new RecButton("Register Now");
		btnRegister.setBounds(37, 328, 206, 42);
		contentPane.add(btnRegister);
		btnRegister.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("One click");
				if (!cc.Reg(NikeName.getText(), password.getText())) {
					JOptionPane.showMessageDialog(null, "注册失败", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					regframe.dispose();
					la.setIs_Registering(false);// 可以打开注册窗口
				}
			}
		});

		JLabel lblRegisterNewUser = new JLabel("Register New User");
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
}
