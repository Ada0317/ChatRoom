package friendListUI;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import dataBase.ListInfo;

/*
 * 这个是好友列表的JPabel
 * 构造函数需要传入好友列表信息
 */
public class ListPane extends JPanel {

	private static final long serialVersionUID = 1L;
	private ListInfo list;
	private Member user[][];
	private byte listCount ;
	private byte[] bodyCount;

	public ListPane(ListInfo list) {
		super();
		this.list = list;
		initialize();
	}

	private void initialize() {
		 listCount = list.getListCount();
		String[] listName = list.getListName();
		bodyCount = list.getBodyCount();
		int[][] bodyNum = list.getBodyNum();
		int[][] bodyPic = list.getBodypic();
		String[][] nikeName = list.getNikeName();
		byte[][] state = list.getBodyState();
		ListName[] list = new ListName[listCount];
		user = new Member[listCount][];
		int i, j;
		for (i = 0; i < listCount; i++) {
			user[i] = new Member[bodyCount[i]];
			for (j = 0; j < bodyCount[i]; j++) {
				int pic = bodyPic[i][j];
				int num = bodyNum[i][j];
				String name = nikeName[i][j];
				byte State = state[i][j];
				user[i][j] = new Member(pic, name, num, State);

			}
			list[i] = new ListName(listName[i], user[i]);
			this.add(list[i]);
			for (j = 0; j < bodyCount[i]; j++) {
				this.add(user[i][j]);
			}

		}
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setSize(272, 450);
		this.setLocation(20, 5);
	}

	public void Hav_Mem_Msg(int JKNum){
		for(int i = 0;i<listCount;i++){
			for(int j = 0; j<bodyCount[i];j++){
				if(user[i][j].getMemberJKNum() == JKNum){
					user[i][j].hav_msg();
				}
			}
		}
	}
}
