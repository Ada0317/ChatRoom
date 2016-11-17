package tools;

import dataBase.UserDB;
import dataBase.UserInfo;

/*
 * 该类主要用于注册用户
 */
public class RegTool {
	public static int UserReg(UserInfo newuser) {
		int JKNum = UserDB.userDB.size();
		newuser.setJKNum(JKNum);
		UserDB.userDB.add(newuser);
		return JKNum;
	}
}
