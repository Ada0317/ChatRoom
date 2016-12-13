package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import dataBase.Figures;
import dataBase.ListInfo;
import msg.*;
import tools.DialogTool;
import tools.PackageTool;
import tools.ParseTool;
/**
 * ChatClient
 * �������������������ͨ��
 * @author He11o_Liu
 */
public class ChatClient extends Thread {
	private String ServerIP;
	private int port;
	private Socket client;
	private static int OwnJKNum;//����½�ɹ��󣬾͸�ChatClient��ΨһJK��
	private InputStream ins;
	private OutputStream ous;

	/**
	 * ChatClient�Ĺ�������
	 * @param ServerIP ��������IP
	 * @param port �˿�
	 */
	public ChatClient(String ServerIP, int port) {
		this.ServerIP = ServerIP;
		this.port = port;
	}

	/**
	 * ���ϵĴ���ӷ�������������Ϣ
	 */
	public void run() {
		while(true){
			try {
				processMsg();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("������������������Ͽ�����");
				JOptionPane.showMessageDialog(null,"��������Ͽ�����","ERROR",JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}

	/**
	 * ���ӵ�������
	 * @return �Ƿ����ӵ�������
	 */
	public boolean ConnectServer() {
		try {
			client = new Socket(ServerIP, port);
			System.out.println("������������");
			ins = client.getInputStream();
			ous = client.getOutputStream();// ��ȡ�����ӵ����������
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Register
	 * ע���û�
	 * @param NikeName �ǳ�
	 * @param PassWord ����
	 * @return ע��״̬
	 */
	public boolean Reg(String NikeName, String PassWord) {
		try {
			MsgReg mr = new MsgReg();
			int len = 33; // MsgReg�ĳ���Ϊ�̶���33
			byte type = 0x01; // MsgReg����Ϊ0x01

			// ����MsgReg�Ĳ���
			mr.setTotalLen(len);
			mr.setType(type);
			mr.setDest(Figures.ServerJK); // ��������JK��
			mr.setSrc(Figures.LoginJK);
			mr.setNikeName(NikeName);
			mr.setPwd(PassWord);

			// ���MsgReg
			byte[] sendMsg = PackageTool.packMsg(mr);
			ous.write(sendMsg);

			// ���շ������ķ�����Ϣ
			byte[] data = receiveMsg();

			// ������ת��Ϊ��
			MsgHead recMsg = ParseTool.parseMsg(data);

			if (recMsg.getType() != 0x11) {// ���ǻ�Ӧע����Ϣ
				System.out.println("ͨѶЭ�����");
				return false;
			}

			MsgRegResp mrr = (MsgRegResp) recMsg;
			if (mrr.getState() == 0) {
				/*
				 * ע��ɹ�
				 */
				System.out.println("ע���JK��Ϊ" + mrr.getDest());
				JOptionPane.showMessageDialog(null, "ע��ɹ�\nJK��Ϊ" + mrr.getDest());
				return true;
			} else {
				/*
				 * ע��ʧ��
				 */
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("��������Ͽ�����");
		return false;
	}

	/**
	 * Login
	 * ����������͵�½����
	 * @param id
	 * @param pwd
	 * @return �ܷ��½
	 */
	public boolean Login(int id, String pwd) {
		try {
			MsgLogin ml = new MsgLogin();
			int len = 23;
			byte type = 0x02;

			// ����MsgLogin�ĸ��ֶ���
			ml.setTotalLen(len);
			ml.setType(type);
			ml.setDest(Figures.ServerJK);
			ml.setSrc(id);
			ml.setPwd(pwd);
			// ���MsgLogin
			byte[] sendmsg = PackageTool.packMsg(ml);
			ous.write(sendmsg);
			//���շ������ķ�����Ϣ
			byte[] data = receiveMsg();
			// ������ת��Ϊ��
			MsgHead recMsg = ParseTool.parseMsg(data);
			if (recMsg.getType() != 0x22) {// ���ǵ�½������Ϣ
				System.out.println("ͨѶЭ�����");
				return false;
			}
			MsgLoginResp mlr = (MsgLoginResp) recMsg;
			byte resp = mlr.getState();
			if (resp == 0) {
				System.out.println("��½�ɹ�");
				OwnJKNum = id;
				return true;
			} else if (resp == 1) {
				System.out.println("JK�Ż��������");
				return false;
			} else {
				System.out.println("δ֪����");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("��������Ͽ�����");
		return false;
	}
	
	/**
	 * ListInfo
	 * ���պ����б�
	 * @return
	 * @throws IOException
	 */
	public ListInfo getInfo() throws IOException {
		ListInfo list = new ListInfo();
		byte[] data = receiveMsg();
		MsgHead recMsg = ParseTool.parseMsg(data);
		if (recMsg.getType() != 0x03) {
			System.out.println("ͨѶЭ�����");
			System.exit(0);
		}
		MsgTeamList mtl = (MsgTeamList) recMsg;
		list.setNickName(mtl.getUserName());
		list.setJKNum(mtl.getDest());
		list.setPic(mtl.getPic());
		list.setListCount(mtl.getListCount());
		list.setListName(mtl.getListName());
		list.setBodyCount(mtl.getBodyCount());
		list.setBodyNum(mtl.getBodyNum());
		list.setBodypic(mtl.getBodyPic());
		list.setNikeName(mtl.getNikeName());
		list.setBodyState(mtl.getBodyState());
		return list;
	}
	
	/**
	 * sendMsg
	 * ������Ϣ
	 * @param to
	 * @param Msg
	 * @throws IOException
	 */
	public void sendMsg(int to,String Msg) throws IOException{
		MsgChatText mct = new MsgChatText();
		byte data[] = Msg.getBytes();
		int TotalLen = 13;
		TotalLen += data.length;
		byte type = 0x04;
		mct.setTotalLen(TotalLen);
		mct.setType(type);
		mct.setDest(to);
		mct.setSrc(OwnJKNum);
		mct.setMsgText(Msg);
		
		byte[] sendMsg = PackageTool.packMsg(mct);
		ous.write(sendMsg);
		ous.flush();
		
	}

	/*
	 * ����������ڴ��������м��ȡһ�����ȵ���Ϣ ��Ϣ����Ϊ��ǰ���һ������
	 * 
	 * @return byte[]�����ĳ�����Ϣ
	 */
	public byte[] receiveMsg() throws IOException {
		DataInputStream dis = new DataInputStream(ins);
		int totalLen = dis.readInt();
		// ��ȡtotalLen���ȵ�����
		byte[] data = new byte[totalLen - 4];
		dis.readFully(data);
		return data;
	}

	/**
	 * processMsg
	 * ���ܷ�������������Ϣ
	 * @throws IOException
	 */
	public void processMsg() throws IOException{
		byte[] data = receiveMsg();
		// ������ת��Ϊ��
		MsgHead recMsg = ParseTool.parseMsg(data);
		byte MsgType = recMsg.getType();
		
		//���ݲ�ͬ����Ϣ���д���
		if(MsgType == 0x04){
			MsgChatText mct = (MsgChatText) recMsg;
			int from = mct.getSrc();
			String Msg = mct.getMsgText();
			DialogTool.ShowMessage(from, Msg);
		}
	}

	
	/*
	 * ���д����Ϊ���Դ���
	 */

	
	/*
	 * ���ڲ����Ƿ�ɹ������б�
	 */
	public void printList(ListInfo list) {
		System.out.println("Nick " + list.getNickName());
		System.out.println("JK " + list.getJKNum());
		System.out.println("Pic "+list.getPic());
		byte listCount = list.getListCount();
		System.out.println("listCount " + listCount);
		String[] listName = list.getListName();
		byte[] bodyCount = list.getBodyCount();
		int[][] bodyNum = list.getBodyNum();
		int[][] bodyPic = list.getBodypic();
		String[][] nikeName = list.getNikeName();
		byte[][] state = list.getBodyState();
		int i, j;
		for (i = 0; i < listCount; i++) {
			System.out.println("ListName " + listName[i]);
			System.out.println("bodyCount " + bodyCount[i]);
			for (j = 0; j < bodyCount[i]; j++) {
				System.out.println("JK " + bodyNum[i][j]);
				System.out.println(	"Pic"+ bodyPic[i][j] );
				System.out.println(" nikeName " + nikeName[i][j] );
				System.out.println(" State " + state[i][j]);
			}
			System.out.println("\n");
		}
	}

	/*
	 * �������Կͻ���
	 */
	/*
	 * public static void main(String[] args) { System.out.println("���Կͻ��˿���");
	 * ChatClient cc = new ChatClient("localhost",9090); if(cc.ConnectServer()){
	 * System.out.println("���ӷ��������"); if(cc.Reg("Test","PWD")){
	 * System.out.println("ע��������"); } else{ System.out.println("ע��ʧ��"); }
	 * 
	 * 
	 * 
	 * //��½���� Scanner sc = new Scanner(System.in); int num = sc.nextInt();
	 * if(cc.Login(num, "PWD")){ System.out.println("��½���Գɹ�"); } else{
	 * System.out.println("��½ʧ��"); } } else{ System.out.println("�޷����ӷ�����"); }
	 * 
	 * }
	 */

}
