package tools;

import dataBase.UserDB;
import dataBase.UserInfo;

public class DAOTool {
	public static boolean CheckLogin(UserInfo check) {
		int JKNum = check.getJKNum();

		if (JKNum >= UserDB.userDB.size()) {// �����ݿ��в����ܴ�����������
			return false;
		}

		if (check.equals(UserDB.userDB.get(JKNum))) {
			return true;
		}
		return false;
	}

	public static UserInfo getinfo(int JKNum) {
		return UserDB.userDB.get(JKNum);
	}
}
