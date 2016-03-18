package object;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.UIManager;

public class RecButton extends JButton {
	private static final long serialVersionUID = 1L;

	public RecButton(String text) {
		setBounds(37, 285, 165, 40);
		setBackground(new Color(0x696969));
		setForeground(Color.WHITE);
		setBorder(null);
		setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 25));
		setPreferredSize(new Dimension(170, 40));
		setText(text);
		setFocusPainted(false);// 设置不要焦点（文字的边框）

		UIManager.put("Button.select", new Color(0x4D4D4D));
		// 在Pressed里面写不顶用
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				setBackground(new Color(0x696969));
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				setBackground(new Color(0x878787));

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

}
