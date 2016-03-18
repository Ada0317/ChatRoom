package chatUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import dataBase.DialogDB;
import dataBase.Figures;
import object.CloseButton;
import object.MinimizeButton;
import object.RecButton;
import tools.DialogTool;

public class DialogUI extends JFrame {

	private int xx, yy;
	private boolean isDraging = false;

	private String nikeName;
	private int bodyNum;
	private JTextArea MsgArea;
	private JTextArea sendArea;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public int getBodyNum() {
		return bodyNum;
	}

	public void setBodyNum(int bodyNum) {
		this.bodyNum = bodyNum;
	}

	public DialogUI(String nikeName, int bodyPic, int bodyNum) {
		this.nikeName = nikeName;
		this.bodyNum = bodyNum;

		// 设置无标题栏
		setUndecorated(true);

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

		setBounds(100, 100, 653, 494);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// 设置自制按钮

		CloseButton eb = new CloseButton(this);
		eb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		int windowWeith = this.getWidth();
		eb.setBounds(windowWeith - 4 - 40, 0, 40, 30);
		contentPane.add(eb);
		MinimizeButton mb = new MinimizeButton(this);
		mb.setBounds(windowWeith - 4 - 80, 0, 40, 30);
		contentPane.add(mb);

		JLabel NickName = new JLabel("");
		NickName.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 30));
		NickName.setForeground(Color.WHITE);
		NickName.setBounds(31, 93, 200, 44);
		NickName.setText(nikeName);
		contentPane.add(NickName);


		MsgArea = new JTextArea();
		MsgArea.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 20));
		MsgArea.setBackground(Color.LIGHT_GRAY);
		MsgArea.setBounds(0, 147, 653, 152);
		contentPane.add(MsgArea);

		sendArea = new JTextArea();
		sendArea.setFont(new Font("Microsoft YaHei UI Light", Font.PLAIN, 20));
		sendArea.setBackground(Color.LIGHT_GRAY);
		sendArea.setBounds(0, 325, 653, 96);
		contentPane.add(sendArea);

		RecButton Send = new RecButton("Send");
		Send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!sendArea.getText().equals("")) {
					String msg = sendArea.getText();
					DialogTool.SendMessage(bodyNum, msg);
					SendMsg(msg);
				}

			}
		});
		Send.setBounds(77, 440, 165, 40);
		contentPane.add(Send);

		RecButton Close = new RecButton("Close");
		Close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogDB.dialogDB.remove(String.valueOf(bodyNum));
				dispose();
			}
		});
		Close.setBounds(398, 440, 165, 40);
		contentPane.add(Close);
		
		JLabel lblUserInfor = new JLabel("User Infor");
		lblUserInfor.setForeground(Color.WHITE);
		lblUserInfor.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 60));
		lblUserInfor.setBounds(31, 10, 513, 91);
		contentPane.add(lblUserInfor);

		setVisible(true);

	}

	/*
	 * 用于显示对方发送来的消息
	 */
	public void ShowMsg(String msg) {
		String text = MsgArea.getText();
		text += nikeName + " Says:" + msg + "\r\n";
		MsgArea.setText(text);
	}

	/*
	 * 用于显示已经发送的消息
	 */
	public void SendMsg(String msg) {
		String text = MsgArea.getText();
		text += Figures.NickName + " Says:" + msg + "\r\n";
		MsgArea.setText(text);
	}

	/*
	 * 抖动起来吧23333
	 */
	public void LetsShack() {
		int x = this.getX();
		int y = this.getY();
		for (int i = 0; i < 20; i++) {
			if ((i & 1) == 0) {
				x += 3;
				y += 3;
			} else {
				x -= 3;
				y -= 3;
			}
			this.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}
}
