package loginUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

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

	/**
	 * Create the frame.
	 */
	public RegisterUI(LoginAction la) {

		// 设置无标题栏
		setUndecorated(true);

		// 可以拽着跑
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
		la.setRpassword(password);

		// 设置自制按钮
		ExitButton eb = new ExitButton();
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
		la.setNikeName(NikeName);

		JLabel lbljk = new JLabel(
				"*\u63D0\u793A\uFF1A\u5F53\u6CE8\u518C\u6210\u529F\uFF0C\u5C06\u8FD4\u56DE\u552F\u4E00\u7684JK\u7801\uFF0C\u8BF7\u59A5\u5584\u4FDD\u7BA1\u3002");
		lbljk.setForeground(Color.WHITE);
		lbljk.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		lbljk.setBounds(37, 268, 372, 28);
		contentPane.add(lbljk);

		RecButton btnRegister = new RecButton("Register Now");
		btnRegister.setBounds(37, 328, 206, 42);
		contentPane.add(btnRegister);
		btnRegister.addActionListener(la);

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
		btnRegister.addActionListener(la);

		setVisible(true);
	}
}
