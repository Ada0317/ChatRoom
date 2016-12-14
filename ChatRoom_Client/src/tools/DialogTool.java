package tools;

import java.io.IOException;

import chatUI.DialogUI;
import dataBase.DialogDB;
import dataBase.Figures;
import dataBase.UserInfo;

/*
 * 这个工具主要处理会话相关功能
 * 1.显示消息
 * 2.发送消息
 */
public class DialogTool {
	
	/*
	 * 这个方法用来显示消息
	 * @param from 发送者JK号
	 * @param msg 文本内容
	 */
	public static void ShowMessage(int from,String msg){
		/*
		 * 找到是否打开了于是对话的对话UI
		 */
		if(DialogDB.dialogDB.containsKey(String.valueOf(from))){
			/*
			 * 有的话
			 */
			DialogUI dialog = DialogDB.dialogDB.get(String.valueOf(from));
			dialog.ShowMsg(msg);
			dialog.LetsShack();
		}
		else{
			/*
			 * 没有的话 A new Msg
			 */
			//Figures.list.Hav_Mem_Msg(from);
			/*
			 * 直接弹出窗口
			 */
			UserInfo user = Figures.list.findUserByJK(from);
			System.out.println("New MSG From"+user.getNickName());
			DialogUI dialog =  new DialogUI(user.getNickName(),user.getPic(), from);
			DialogRegDelTool.RegDialog(from, dialog);
			dialog.ShowMsg(msg);
			dialog.LetsShack();
		}
		
		
	}
	
	
	
	
	/*
	 * 这个方法用来发送消息
	 * @param to 发送给的人的JK号
	 * @param msg 发送的信息
	 * @param cc 总服务ChatClient
	 */
	public static void SendMessage(int to,String msg){
		
		/*
		 * 发送信息
		 */
		try {
			Figures.cc.sendMsg(to, msg);
			System.out.println("发送消息成功");
		} catch (IOException e) {
			System.out.println("发送消息失败");
			e.printStackTrace();
		}
		
	}
}
