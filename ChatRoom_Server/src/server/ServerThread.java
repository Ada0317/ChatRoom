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
		model = new UserModel(DBConnection.getInstance());
		
		ous = client.getOutputStream();
		InputStream ins = client.getInputStream();
		DataInputStream dis = new DataInputStream(ins);
		int totalLen = dis.readInt();
		byte[] data = new byte[totalLen - 4];
		dis.readFully(data);
		MsgHead msg = ParseTool.parseMsg(data);// �������Ϣ

		/*
		 * ��������Բ�ͬ����Ϣ���д���
		 */

		// �������������ע����Ϣ
		if (msg.getType() == 0x01) {
			MsgReg mr = (MsgReg) msg;

			// ע���û�

			UserInfo newuser = model.createUser(mr.getPwd(), mr.getNikeName(), 1);
			int JKNum = newuser.getJKNum();

			/*
			 * ������׼��������Ϣ
			 */

			MsgRegResp mrr = new MsgRegResp();
			int Len = 14;// MsgRegResp�ĳ���Ϊ14
			byte msgtype = 0x11;// MsgRegResp������Ϊ0x11
			byte state = 0;

			// ����MsgRegResp�ĸ�������
			mrr.setTotalLen(Len);
			mrr.setType(msgtype);
			mrr.setDest(JKNum); // MsgRegResp��Dest��ע��õ�JK��
			mrr.setSrc(Figures.ServerJK); // ��������JK��
			mrr.setState(state);

			// д������
			byte[] sendmsg = PackageTool.packMsg(mrr);// ���������Ϣ���
			ous.write(sendmsg);
			ous.flush();

		}

		// ����������ǵ�½��Ϣ
		else if (msg.getType() == 0x02) {
			MsgLogin ml = (MsgLogin) msg;
			byte checkmsg;// ��������״̬��Ϣ

			// DAO��֤�û��Ƿ����
			System.out.println(model.userAuthorization(ml.getSrc(), ml.getPwd()));
			if (model.userAuthorization(ml.getSrc(), ml.getPwd())) {// �����֤���û�����
				checkmsg = 0;
			} else {
				checkmsg = 1;
			}

			/*
			 * ������׼��������Ϣ
			 */
			MsgLoginResp mlr = new MsgLoginResp();
			int len = 14;
			byte msgtype = 0x22;

			// ����resp�ĸ�������
			mlr.setTotalLen(len);
			mlr.setType(msgtype);
			mlr.setDest(Figures.LoginJK);
			mlr.setSrc(Figures.ServerJK);
			mlr.setState(checkmsg);

			// д������
			byte[] sendmsg = PackageTool.packMsg(mlr);// ���������Ϣ���
			ous.write(sendmsg);
			ous.flush();

			
			
			/*
			 * �����½������ɣ� ���ͺ����б�
			 */
			if (checkmsg == 0) {
				UserJK = ml.getSrc();
				ThreadRegDelTool.RegThread(this); // ���߳����ݿ���ע������߳�
				UserInfo user = model.getUserByJK(ml.getSrc());
				msgtype = 0x03;
				String userName = user.getNickName();
				int pic = user.getAvatar();
				byte listCount = user.getCollectionCount();
				byte[] bodyCount = user.getBodyCount();
				byte[][] bodyState;
				int[][] BodyNum = user.getBodyNum();
				int[][] BodyPic = user.getBodypic();
				/*
				 * ���㳤��
				 */
				int i, j;

				len = 13; // ��Ϣͷ����
				len += 10; // userName
				len += 4;  //pic
				len += 1; // listCount
				len += (10 * listCount); // listName
				len += listCount; // bodyCount

				bodyState = new byte[listCount][];

				for (i = 0; i < listCount; i++) {
					len += bodyCount[i] * 19; // ÿ�����ѳ���Ϊ19

					bodyState[i] = new byte[bodyCount[i]];
				}

				/*
				 * ����������״̬ ʵ�ַ��� ȥ�߳����ݿ⿴���ǲ��Ǵ���ͬ��JKNUM���߳�
				 */

				for (i = 0; i < listCount; i++) {
					for (j = 0; j < bodyCount[i]; j++) {
						if (ThreadDB.threadDB.containsKey(String.valueOf(BodyNum[i][j]))) {
							bodyState[i][j] = 0;
						} else {
							bodyState[i][j] = 1;
						}
					}
				}

				// ����mtl�ĸ�������
				MsgTeamList mtl = new MsgTeamList();
				mtl.setTotalLen(len);
				mtl.setType(msgtype);
				mtl.setDest(ml.getSrc());
				mtl.setSrc(Figures.ServerJK);
				mtl.setUserName(userName);
				mtl.setPic(pic);
				mtl.setListCount(listCount);
				mtl.setListName(user.getListName());
				mtl.setBodyCount(bodyCount);
				mtl.setBodyNum(BodyNum);
				mtl.setBodyPic(BodyPic);
				mtl.setNikeName(user.getBodyName());
				mtl.setBodyState(bodyState);


				// д������
				sendmsg = PackageTool.packMsg(mtl);
				
				ous.write(sendmsg);
				ous.flush();
				is_Online = true;// �����ѵ�¼�ͻ���
			}

		}

	}

	/*
	 * �÷������ڴ���ӿͻ��˴���������Ϣ (�ѵ�¼)
	 */
	public void processChat() throws Exception {
		InputStream ins = client.getInputStream();
		DataInputStream dis = new DataInputStream(ins);
		
		int totalLen = dis.readInt();
		byte[] data = new byte[totalLen - 4];
		dis.readFully(data);
		MsgHead msg = ParseTool.parseMsg(data);// �������Ϣ

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
				ChatTool.saveOnServer(from, to,msgText);
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
