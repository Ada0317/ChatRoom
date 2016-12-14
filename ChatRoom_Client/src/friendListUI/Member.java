package friendListUI;

import javax.swing.ImageIcon;

import javax.swing.JPanel;

import chatUI.DialogUI;
import dataBase.DialogDB;
import tools.DialogRegDelTool;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Font;

/*
 * 这里把列表中的每一个用户包装成一个JLabel
 * 方便放在JScollPane里面
 * 
 */
public class Member extends JLabel {

	private static final long serialVersionUID = 1L;
	public JPanel jPanel = new JPanel(); // 模板容器；
	private JLabel lb_nickName = null; // 显示昵称；
	private JLabel lb_JKnum = null; // 显示JK号
	private boolean is_exit = true;
	private int MemberJKNum;
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	private int pic;
	private String nickname;
	public int getPic() {
		return pic;
	}

	public void setPic(int pic) {
		this.pic = pic;
	}

	public int getMemberJKNum() {
		return MemberJKNum;
	}

	public void setMemberJKNum(int memberJKNum) {
		MemberJKNum = memberJKNum;
	}

	public Member(int picNum, String nickname, int JKNum, byte state) {
		MemberJKNum = JKNum;
		pic = picNum;
		this.nickname = nickname;
		setBackground(Color.darkGray);
		/*
		 * 设置用户名
		 */
		lb_nickName = new JLabel();
		lb_nickName.setForeground(Color.WHITE);
		lb_nickName.setBounds(new Rectangle(70, 10, 95, 20));
		lb_nickName.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		lb_nickName.setText(nickname);

		/*
		 * 设置用户账号
		 */
		lb_JKnum = new JLabel();
		lb_JKnum.setForeground(Color.WHITE);
		lb_JKnum.setBounds(new Rectangle(70, 38, 80, 20));
		lb_JKnum.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 15));
		lb_JKnum.setText("JKNum:(" + JKNum + ")");

		/*
		 * 设置是否在线
		 */
		String SState;
		if (state == 0)
			SState = "OnLine";
		else
			SState = "OffLine";
		JLabel lb_State = new JLabel();
		lb_State.setText(SState);
		lb_State.setForeground(Color.WHITE);
		lb_State.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		lb_State.setBounds(new Rectangle(70, 10, 95, 20));
		lb_State.setBounds(180, 10, 95, 20);
		add(lb_State);

		/*
		 * 设置用户头像
		 */
		JButton UserIcon = new JButton();
		UserIcon.setBorder(null);
		UserIcon.setBounds(10, 10, 50, 50);
		UserIcon.setIcon(new ImageIcon("img/AvatarImg/" + pic + ".jpg"));

		/*
		 * 增加背景 这里必须这么干
		 */
		setIcon(new ImageIcon("img/ListImg/memberBGOff.jpg"));

		setSize(new Dimension(272, 70));
		setLayout(null);
		add(UserIcon);
		add(lb_nickName);
		add(lb_JKnum);
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if(is_exit){
					setIcon(new ImageIcon("img/ListImg/memberBGOff.jpg"));
				}
				else{
					setIcon(new ImageIcon("img/ListImg/memberBGOn.jpg"));
				}
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				setIcon(new ImageIcon("img/ListImg/memberBGOff.jpg"));
				is_exit = true;
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				setIcon(new ImageIcon("img/ListImg/memberBGOn.jpg"));
				is_exit = false;
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				DialogUI dialog;
				if(DialogDB.dialogDB.containsKey(String.valueOf(JKNum))){
					dialog = DialogDB.dialogDB.get(String.valueOf(JKNum));
					dialog.LetsShack();
				}
				else{
					dialog = new DialogUI(nickname,picNum,JKNum);
					DialogRegDelTool.RegDialog(JKNum, dialog);
				}
			}
		});
		
		
		
	}

	/*
	 * 当有来自这个人的信息
	 */
	public void hav_msg(){
		System.out.println("Have_A_MSG");
		setIcon(new ImageIcon("img/ListImg/memberBGMsg.jpg"));
	}
	
	
}
