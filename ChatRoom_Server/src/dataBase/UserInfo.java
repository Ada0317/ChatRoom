package dataBase;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * �û�������Ϣ
 */
public class UserInfo {
	/*
	 * �û�������Ϣ
	 */

	private int JKNum;// ������û���JK��
	private String nickName;// ������û����ǳ�
//	private String passWord;// ������û���IP
	private int avatar;

	UserInfo(ResultSet userResult) throws SQLException {
	    JKNum = userResult.getInt("user_id");
	    nickName = userResult.getString("nickname");
	    avatar = userResult.getInt("avatar");
    }

	/*
	 * �û�������Ϣ
	 */

	private byte collectionCount;// �����ж��������
	private String ListName[];// ����ÿ�����������
	private byte[] bodyCount;// ÿ���ж��ٸ���
	private int bodyNum[][];// ÿ�����ѵ�JK��
	private int bodypic[][];//����ͷ��
	private String nikeName[][];// ÿ�����ѵ��ǳ�

	public byte getListCount() {
		return collectionCount;
	}

	public void setListCount(byte listCount) {
		this.collectionCount = listCount;
	}

	public String[] getListName() {
		return ListName;
	}

	public void setListName(String[] listName) {
		ListName = listName;
	}

	public byte[] getBodyCount() {
		return bodyCount;
	}

	public void setBodyCount(byte[] bodyCount) {
		this.bodyCount = bodyCount;
	}

	public int[][] getBodyNum() {
		return bodyNum;
	}

	public void setBodyNum(int[][] bodyNum) {
		this.bodyNum = bodyNum;
	}

	public String[][] getNikeName() {
		return nikeName;
	}

	public void setNikeName(String[][] nikeName) {
		this.nikeName = nikeName;
	}

	public int getJKNum() {
		return JKNum;
	}

	public void setJKNum(int jKNum) {
		JKNum = jKNum;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nick) {
		nickName = nick;
	}

//	public String getPassWord() {
//		return passWord;
//	}
//
//	public void setPassWord(String pw) {
//		passWord = pw;
//	}

	public boolean equals(UserInfo compare) {
		if (compare.getJKNum() == JKNum/* && compare.getPassWord().equals(passWord)*/) {
			return true;
		}
		return false;
	}

	public int[][] getBodypic() {
		return bodypic;
	}

	public void setBodypic(int bodypic[][]) {
		this.bodypic = bodypic;
	}

	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}

}
