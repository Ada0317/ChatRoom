package dataBase;

import java.util.HashMap;
import java.util.Map;

import chatUI.DialogUI;


/*
 * 用来保存客户端会话窗口的数据库
 * 利用Hash Map来保存
 * 打开的窗口将会记录进该数据库
 */
public class DialogDB {
	public static Map<String, DialogUI> dialogDB = new HashMap<String, DialogUI>();
}
