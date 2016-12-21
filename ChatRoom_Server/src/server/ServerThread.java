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
		while (!is_Online) { // ���߳��пͻ���δ��½
			try {
				processLogin();
			} catch (Exception e) {

				/*
				 * �ͻ��˶Ͽ�����
				 */
				System.out.println(client.getRemoteSocketAddress() + "�ѶϿ�");
				try {
					client.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}
		while (is_Online) { // ���߳��пͻ����ѵ�½
			try {
				processChat();
			} catch (Exception e) {
				/*
				 * �ͻ��˶Ͽ�����
				 */
				System.out.println(client.getRemoteSocketAddress() + "�ѶϿ�");
				ThreadRegDelTool.DelThread(this);// ���߳����ݿ��м�ɾ��������Ϣ
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
	 * �÷������ڴ���ӿͻ��˴���������Ϣ (δ��¼)
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
		 * ��������Բ�ͬ����Ϣ���д���
		 */

		// �������������ע����Ϣ
		if (msg.getType() == 0x01) {
			MsgReg mr = (MsgReg) msg;

			// ע���û�

			UserInfo newUser = model.createUser(mr.getPwd(), mr.getNikeName(), 1);
			int JKNum = newUser.getJKNum();

			/*
			 * ������׼��������Ϣ
			 */
			byte state = 0;
			MsgRegResp mrr = new MsgRegResp(JKNum, state);
            mrr.send(ous);
		}

		// ����������ǵ�½��Ϣ
		else if (msg.getType() == 0x02) {
			MsgLogin ml = (MsgLogin) msg;
			boolean userChecked = model.userAuthorization(ml.getSrc(), ml.getPwd());

			/*
			 * ������׼��������Ϣ
			 */
			MsgLoginResp mlr = new MsgLoginResp(userChecked);
			mlr.send(ous);

			/*
			 * �����½������ɣ� ���ͺ����б�
			 */
			if (userChecked) {
				UserJK = ml.getSrc();
				ThreadRegDelTool.RegThread(this); // ���߳����ݿ���ע������߳�
				UserInfo user = model.getUserByJK(ml.getSrc());
				MsgTeamList mtl = new MsgTeamList(user);
				mtl.send(ous);
				is_Online = true;// �����ѵ�¼�ͻ���
			}

		}
		conn.close();

	}
	/*
	 * �÷������ڴ���ӿͻ��˴���������Ϣ (�ѵ�¼)
	 */
	public void processChat() throws Exception {
		InputStream ins = client.getInputStream();
		DataInputStream dis = new DataInputStream(ins);
		MsgHead msg = MsgHead.readMessageFromStream(dis);
		/*
		 * ��������Բ�ͬ����Ϣ���д���
		 */

		if (msg.getType() == 0x04) {//����յ����Ƿ�����Ϣ����
			MsgChatText mct = (MsgChatText) msg;
			int from = mct.getSrc();
			int to = mct.getDest();
			String msgText = mct.getMsgText();
			System.out.println("Sending Test!!");
			System.out.println("From "+from+" To "+to+" Text "+msgText);
			
			if(!ChatTool.sendMsg(from, to, msgText)){
				System.out.println("SaveOnServer");
				
				//���浽��������	
				ChatTool.saveOnServer(from, to, msgText);
			}
		}

	}
	
	/*
	 * �÷����������û�����������������Ϣ
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
