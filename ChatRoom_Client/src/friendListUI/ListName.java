package friendListUI;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ListName extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lb_Name;
	private boolean is_click = false;
	/**
	 * Create the panel.
	 */
	public ListName(String name, JLabel[] users) {
		setBackground(Color.DARK_GRAY);

		lb_Name = new JLabel();
		lb_Name.setForeground(Color.WHITE);
		lb_Name.setBounds(new Rectangle(20, 10, 95, 20));
		lb_Name.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 18));
		lb_Name.setText(name);
		this.add(lb_Name);

		setIcon(new ImageIcon("img/ListImg/ListOff.jpg"));
		setSize(new Dimension(272, 50));
		for (int i = 0; i < users.length; i++) {
			users[i].setVisible(false);
		}
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				if(is_click){
					setIcon(new ImageIcon("img/ListImg/ListOn.jpg"));
				}
				else{
					setIcon(new ImageIcon("img/ListImg/ListOff.jpg"));
				}
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				if(is_click){

					setIcon(new ImageIcon("img/ListImg/ListOnEnter.jpg"));
				}
				else{
					setIcon(new ImageIcon("img/ListImg/ListOffEnter.jpg"));
				}
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				is_click = !is_click;
				if(is_click){
					for (int i = 0; i < users.length; i++) {
						users[i].setVisible(true);
					}
					setIcon(new ImageIcon("img/ListImg/ListOnEnter.jpg"));
				} else {
					for (int i = 0; i < users.length; i++) {
						users[i].setVisible(false);
					}
					setIcon(new ImageIcon("img/ListImg/ListOffEnter.jpg"));
				}
			}
		});
	}
}
