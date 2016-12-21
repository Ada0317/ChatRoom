package server;

import java.io.IOException;
import java.sql.SQLException;

public class RefreshThread extends Thread{
	ServerThread server;
	RefreshThread(ServerThread st){
		server = st;
	}
	public void run() {
		while(true){
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					break;
				}
				try {
					server.SendFriendList();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					break;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					break;
				}

		}
	}
}
