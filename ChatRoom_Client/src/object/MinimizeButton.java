package object;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class MinimizeButton extends JButton {
	private static final long serialVersionUID = 1L;

	private boolean is_exit = true;

	public MinimizeButton(JFrame jf) {
		this.setPreferredSize(new Dimension(40, 30));
		this.setBackground(Color.DARK_GRAY);
		this.setText("-");
		this.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 20));
		this.setFocusPainted(false);// 设置不要焦点（文字的边框）
		this.setBorder(null);
		this.setForeground(Color.WHITE);

		// 设置按下的颜色
		UIManager.put("Button.select", new Color(220, 220, 220));
		// 在Pressed里面写不顶用

		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (!is_exit) {
					jf.setExtendedState(JFrame.ICONIFIED);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				setBackground(Color.DARK_GRAY);
				is_exit = true;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				setBackground(new Color(192, 192, 192));
				is_exit = false;
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}
}
