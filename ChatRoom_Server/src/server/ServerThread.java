package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import dataBase.*;
import msg.*;
import tools.*;

public class ServerThread extends Thread {
	private Socket client;
	private OutputStream ous;
	private int UserJK;
	private boolean is_Online = false;
	private UserModel model;

	public int getUserJK() {
		return UserJK;
	}

	public ServerThread(Socket client) {
		this.client = client;
	}

	public void run() {
		while (!is_Online) { // 该线程中客户端未登陆
			try {
				processLogin();
			} catch (Exception e) {

				/*
				 * 客户端断开连接
				 */
				System.out.println(client.getRemoteSocketAddress() + "已断开");
				try {
					client.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}
		while (is_Online) { // 该线程中客户端已登陆
			try {
				processChat();
			} catch (Exception e) {
				/*
				 * 客户端断开连接
				 */
				System.out.println(client.getRemoteSocketAddress() + "已断开");
				ThreadRegDelTool.DelThread(this);// 从线程数据库中间删除这条信息
				try {
					client.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}
	}

	/*
	 * 该方法用于处理从客户端传过来的信息 (未登录)
	 */
	public void processLogin() throws Exception {
		//connect to DataBase
		DBConnection conn = DBConnection.getInstance();
		model = new UserModel(conn);
		
		ous = client.getOutputStream();
		InputStream ins = client.getInputStream();
		DataInputStream dis = new DataInputStream(ins);
//		int totalLen = dis.readInt();
//		byte[] data = new byte[totalLen - 4];
//		dis.readFully(data);
		MsgHead msg = MsgHead.readMessageFromStream(dis);

		/*
		 * 下面是针对不同的信息进行处理
		 */

		// 如果传过来的是注册信息
		if (msg.getType() == 0x01) {
			MsgReg mr = (MsgReg) msg;

			// 注册用户

			UserInfo newUser = model.createUser(mr.getPwd(), mr.getNikeName(), 1);
			int JKNum = newUser.getJKNum();

			/*
			 * 服务器准备返回信息
			 */
			byte state = 0;
			MsgRegResp mrr = new MsgRegResp(JKNum, state);
            mrr.send(ous);
		}

		// 如果传过来是登陆信息
		else if (msg.getType() == 0x02) {
			MsgLogin ml = (MsgLogin) msg;
			boolean userChecked = model.userAuthorization(ml.getSrc(), ml.getPwd());

			/*
			 * 服务器准备返回信息
			 */
			MsgLoginResp mlr = new MsgLoginResp(userChecked);
			mlr.send(ous);

			/*
			 * 如果登陆操作完成， 发送好友列表
			 */
			if (userChecked) {
				UserJK = ml.getSrc();
				ThreadRegDelTool.RegThread(this); // 向线程数据库中注册这个线程
				UserInfo user = model.getUserByJK(ml.getSrc());
				MsgTeamList mtl = new MsgTeamList(user);
				mtl.send(ous);
				is_Online = true;// 设置已登录客户端
			}

		}
		conn.close();

	}
	/*
	 * 该方法用于处理从客户端传过来的信息 (已登录)
	 */
	public void processChat() throws Exception {
		InputStream ins = client.getInputStream();
		DataInputStream dis = new DataInputStream(ins);
		MsgHead msg = MsgHead.readMessageFromStream(dis);
		/*
		 * 下面是针对不同的信息进行处理
		 */

		if (msg.getType() == 0x04) {//如果收到的是发送信息请求
			MsgChatText mct = (MsgChatText) msg;
			int from = mct.getSrc();
			int to = mct.getDest();
			String msgText = mct.getMsgText();
			System.out.println("Sending Test!!");
			System.out.println("From "+from+" To "+to+" Text "+msgText);
			
			if(!ChatTool.sendMsg(from, to, msgText)){
				System.out.println("SaveOnServer");
				
				//保存到服务器上	
				ChatTool.saveOnServer(from, to, msgText);
			}
		}

	}
	
	/*
	 * 该方法用来向用户发送其他人来的信息
	 */
	public void SendMsg(int from,String msg) throws IOException{
		MsgChatText mct = new MsgChatText();
		int totalLen = 13;
		byte msgType = 0x04;
		byte[] data = msg.getBytes();
		totalLen += data.length;
		
		mct.setTotalLen(totalLen);
		mct.setType(msgType);
		mct.setDest(UserJK);
		mct.setSrc(from);
		mct.setMsgText(msg);
		
		byte[] send = PackageTool.packMsg(mct);
		ous.write(send);
		ous.flush();

	}
	
	

}
