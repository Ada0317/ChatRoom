package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;

import dataBase.*;
import msg.*;
import tools.*;

public class ServerThread extends Thread {
    public boolean isSending = false;
    private Socket client;
    private OutputStream ous;
    private int userJK;
    private boolean isOnline = false;
//	private UserModel model;

    public int getUserJK() {
        return userJK;
    }

    public ServerThread(Socket client) {
        this.client = client;
    }

    public void run() {
        while (!isOnline) { // 该线程中客户端未登陆
            try {
                processLogin();
            } catch (Exception e) {

				/*
                 * 客户端断开连接
				 */
                System.out.println(client.getRemoteSocketAddress() + "已断开");
                isOnline = false;

                try {
                    client.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
            }
        }
        while (isOnline) { // 该线程中客户端已登陆
            //开始更新列表
            try {
                processChat();
            } catch (Exception e) {
				/*
				 * 客户端断开连接
				 */
                System.out.println(client.getRemoteSocketAddress() + "已断开");
                ThreadRegDelTool.DelThread(userJK);// 从线程数据库中间删除这条信息
                isOnline = false;
                try {
                    broadcastState();
                } catch (SQLException | IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
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
    private void processLogin() throws Exception {
        //connect to DataBase
        DBConnection conn = DBConnection.getInstance();
        UserModel model = new UserModel(conn);

        ous = client.getOutputStream();
        InputStream ins = client.getInputStream();
        DataInputStream dis = new DataInputStream(ins);

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

            byte checkmsg;// 用来保存状态信息
            if (ThreadDB.threadDB.containsKey(String.valueOf(ml.getSrc()))) {//已经在线了
                checkmsg = 2;
            } else if (model.userAuthorization(ml.getSrc(), ml.getPwd())) {// 如果验证了用户存在
                checkmsg = 0;
            } else {
                checkmsg = 1;
            }

			/*
			 * 服务器准备返回信息
			 */
            MsgLoginResp mlr = new MsgLoginResp(checkmsg);
            mlr.send(ous);

			/*
			 * 如果登陆操作完成， 发送好友列表
			 */
            if (checkmsg == 0) {
                userJK = ml.getSrc();
                ThreadRegDelTool.RegThread(this); // 向线程数据库中注册这个线程
                sendFriendList();
                isOnline = true;// 设置已登录客户端
                broadcastState();
            }

        }
        conn.close();

    }

    private void broadcastState() throws SQLException, IOException {
        DBConnection conn = DBConnection.getInstance();
        UserModel model = new UserModel(conn);
        UserInfo user = model.getUserByJK(userJK);
        for (int i = 0; i < user.getCollectionCount(); i++) {
            for (int j = 0; j < user.getBodyCount()[i]; j++) {
                ServerThread st = ThreadDB.threadDB.get(String.valueOf(user.getBodyNum()[i][j]));
                if (st != null) {
                    st.sendFriendList();
                }
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

        int totalLen = dis.readInt();
        byte[] data = new byte[totalLen - 4];
        dis.readFully(data);
        MsgHead msg = ParseTool.parseMsg(data);// 解包该信息

		/*
		 * 下面是针对不同的信息进行处理
		 */

        if (msg.getType() == 0x04) {//如果收到的是发送信息请求
            MsgChatText mct = (MsgChatText) msg;
            int from = mct.getSrc();
            int to = mct.getDest();
            String msgText = mct.getMsgText();
//			System.out.println("Sending Test!!");
//			System.out.println("From "+from+" To "+to+" Text "+msgText);

            if (!ChatTool.sendMsg(from, to, msgText)) {
                System.out.println("SaveOnServer");

                //保存到服务器上
                ChatTool.saveOnServer(from, to, msgText);
            }
        } else if (msg.getType() == 0x05) {//如果受到添加好友的请求
            System.out.println("Add friend request");
            MsgAddFriend maf = (MsgAddFriend) msg;
            int own_jk = maf.getSrc();
            int add_jk = maf.getAdd_ID();
            String list_name = maf.getList_name();
            DBConnection conn = DBConnection.getInstance();
            UserModel model = new UserModel(conn);
            int result = model.addFriend(add_jk, own_jk, list_name);
            System.out.println("Add finish " + result);
            MsgAddFriendResp mafr = new MsgAddFriendResp();
            mafr.setSrc(Figures.ServerJK);
            mafr.setDest(own_jk);
            mafr.setTotalLen(14);
            mafr.setType((byte) 0x55);
            if (result == 0) {//success
                model.addFriend(own_jk, add_jk, "新添加好友");
                //send add_jk new list
                mafr.setState((byte) 0);
                //send own_jk new list
            } else if (result == 1) {//不存在这个人
                mafr.setState((byte) 1);
            } else if (result == 2) {//如果已经存在了这个人
                mafr.setState((byte) 2);
            } else if (result == 3) {//创建列表失败
                mafr.setState((byte) 3);
            }
            mafr.send(ous);

            sendFriendList();

            //send Add_JK Friend list
            model.addFriend(own_jk, add_jk, list_name);
            //给被添加者更新列表
            ServerThread st = ThreadDB.threadDB.get(String.valueOf(add_jk));
            if (st != null) {
                st.sendFriendList();
            }
            conn.close();
        }

    }

    /**
     * 发送好友列表
     *
     * @throws IOException
     * @throws SQLException
     */
    private void sendFriendList() throws IOException, SQLException {
        System.out.println("发送好友列表");

        DBConnection conn = DBConnection.getInstance();
        UserModel model = new UserModel(conn);
        UserInfo user = model.getUserByJK(userJK);
        MsgTeamList mtl = new MsgTeamList(user);
        mtl.send(ous);
        conn.close();
    }
	
	/*
	 * 该方法用来向用户发送其他人来的信息
	 */

    public void sendMsg(int from, String msg) throws IOException {
        MsgChatText mct = new MsgChatText(from, userJK, msg);
        mct.send(ous);
    }
}
