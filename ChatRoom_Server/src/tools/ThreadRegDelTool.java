package tools;

import dataBase.ThreadDB;
import server.ServerThread;

/*
 * 这个工具包含以下功能
 * 1.将登陆的客户对应的线程保存到线程数据库
 * 2.将下线的客户对应的线程从数据库中删除
 */
public class ThreadRegDelTool {
	// 注册到线程数据库
	public static void RegThread(ServerThread thread) {
		ThreadDB.threadDB.put(String.valueOf(thread.getUserJK()), thread);
	}

	// 从线程数据库中间删除
	public static void DelThread(ServerThread thread) {
		ThreadDB.threadDB.remove(thread.getUserJK());
	}

}
