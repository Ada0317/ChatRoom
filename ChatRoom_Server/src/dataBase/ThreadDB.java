package dataBase;

import java.util.HashMap;
import java.util.Map;
import server.ServerThread;

/*
 * 用来保存服务器线程的数据库
 * 利用Hash Map来保存
 * 只有在登陆后才会记载入该数据库
 */
public class ThreadDB {
	public static Map<String, ServerThread> threadDB = new HashMap<String, ServerThread>();
}
