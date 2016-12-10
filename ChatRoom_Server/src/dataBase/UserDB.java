package dataBase;

import java.util.ArrayList;
import java.util.List;

/*
 * 用户数据库
 * 用一个list保存用户信息
 */
public class UserDB {
	public static List<UserInfo> userDB = new ArrayList<UserInfo>();

	/*
	 * 用于测试用户界面
	 */
	static {
		/*
		 * 测试用户1 
		 * JK 0
		 * pwd 123
		 */
		UserInfo user = new UserInfo();
		user.setJKNum(0);
		user.setNickName("Liu");
		user.setPassWord("123");
		user.setAvatar(2);
		
		user.setListCount((byte) 2);

		String listName[] = { "我的好友", "大学同学" };
		byte bodyCount[];
		bodyCount = new byte[2];
		bodyCount[0] = 3;
		bodyCount[1] = 2;
		int bodyNum[][] = new int[2][];
		int bodyPic[][] = new int[2][];
		bodyNum[0] = new int[] { 1, 2, 3 };
		bodyPic[0] = new int[] { 1, 1, 1 };
		bodyNum[1] = new int[] { 4, 5 };
		bodyPic[1] = new int[] { 1, 1 };
		String nikeName[][] = new String[2][];
		nikeName[0] = new String[] { "Penny", "Dwyant", "Abbott" };
		nikeName[1] = new String[] { "Nancy", "Aaron" };

		user.setListName(listName);
		user.setBodyCount(bodyCount);
		user.setBodyNum(bodyNum);
		user.setBodypic(bodyPic);
		user.setNikeName(nikeName);

		userDB.add(user);
		
		
		/*
		 * 测试用户2 
		 * JK 1
		 * pwd 123
		 */
		user = new UserInfo();
		user.setJKNum(1);
		user.setNickName("Penny");
		user.setPassWord("123");
		user.setAvatar(1);
		user.setListCount((byte) 1);

		listName = new String[1];
		listName[0]= "我的好友" ;
		bodyCount = new byte[1];
		bodyCount[0] = 1;
		bodyNum = new int[1][];
		bodyPic = new int[1][];
		bodyNum[0] = new int[] { 0 };
		bodyPic[0] = new int[] { 2 };
		nikeName = new String[1][];
		nikeName[0] = new String[] { "Liu" };
		
		user.setListName(listName);
		user.setBodyCount(bodyCount);
		user.setBodyNum(bodyNum);
		user.setBodypic(bodyPic);
		user.setNikeName(nikeName);

		userDB.add(user);
		

	}
}
