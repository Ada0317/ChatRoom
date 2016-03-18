package tools;

import java.io.IOException;

import dataBase.ThreadDB;
import server.ServerThread;

/*
 * �����������ʵ�����¹���
 * 1.��Ŀ��JK�ŷ�����Ϣ
 * 2.��δ���ͳɹ�����Ϣ���ڷ�����
 */
public class ChatTool {
	/*
	 * �������������JKNum���û���������ΪMsg����Ϣ
	 * 
	 * @return �Ƿ�ɹ�����
	 */
	public static boolean sendMsg(int from,int to, String msg) {
		if (!ThreadDB.threadDB.containsKey(String.valueOf(to))) {
			return false;
		}
		
		/*
		 * ���û�������Ϣ
		 */
		ServerThread st = ThreadDB.threadDB.get(String.valueOf(to));
		try {
			st.SendMsg(from, msg);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * �����������JKNum���û������� ������JKNum���û���������ΪMsg����Ϣ
	 */
	public static void saveOnServer(int from,int to, String Msg) {

	}

}
