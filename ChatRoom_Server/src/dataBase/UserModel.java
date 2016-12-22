package dataBase;

import java.sql.*;
import java.util.*;

/**
 * Created by hcyue on 2016/12/3.
 * Modified by He11o_Liu on 2016/12/13.
 */
public class UserModel {
    DBConnection db;

    public UserModel(DBConnection conn) {
        this.db = conn;
    }

    /**
     * getUserByJK
     * 根据JK号获取UserInfo对象
     * 包括用户基本属性以及好友列表
     * @param JK
     * @return UserInfo user
     * @throws SQLException
     * @author Hcyue
     * @author He11o_Liu
     * 2016/12/13
     */
    public UserInfo getUserByJK(int JK) throws SQLException {
        ResultSet rs = db.query("SELECT * FROM users where user_id=" + JK);
        if (rs.next()==false) return null;
        UserInfo user = new UserInfo(rs);
        
        //Get Friend List
        CollectionModel collectionModel = new CollectionModel(db);
        List<CollectionInfo> coll = collectionModel.getColletionsByJK(JK);
        CollectionInfo collection;
		List<UserInfo> memberlist;
		UserInfo member;
		
		int collectionCount = coll.size();
		int memberCount = 0;
		user.setCollectionCount((byte) collectionCount);
		
		String[] ListName = new String[collectionCount];
		byte[] bodyCount = new byte[collectionCount];// 每组有多少个人
		int bodyNum[][] = new int[collectionCount][];// 每个好友的JK号
		int bodypic[][] = new int[collectionCount][];//好友头像
		String bodyName[][] = new String[collectionCount][];// 每个好友的昵称
		
        for(int j = 0; j<coll.size();j++){
        	try {
        		collection = coll.get(j);
        		ListName[j] = collection.getName();
				memberlist = collection.getMembers();
				
				memberCount = memberlist.size();
				bodyCount[j] = (byte)memberCount;
				
				bodyNum[j] = new int[memberCount];
				bodyName[j] = new String[memberCount];
				bodypic[j] = new int[memberCount];
				
				for(int i = 0; i < memberlist.size();i++){
					member = memberlist.get(i);
					bodyNum[j][i] = member.getJKNum();
					bodyName[j][i] = member.getNickName();
					bodypic[j][i] = member.getAvatar();
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        //set friend list
        user.setBodyName(bodyName);
        user.setListName(ListName);
        user.setBodyCount(bodyCount);
        user.setBodyNum(bodyNum);
        user.setBodypic(bodypic);
        
        rs.close();
        return user;
    }
    
    /**
     * userAuthorization
     * 验证用户密码
     * @param jk
     * @param passwd
     * @return boolean Result
     * @throws SQLException
     * @author Hcyue
     */
    public boolean userAuthorization(int jk, String passwd) throws SQLException {
        ResultSet rs = db.query(String.format("SELECT * FROM users WHERE user_id=%d AND password='%s'", jk, passwd));
        if (rs.next() == false) return false;
        rs.close();
        return true;
    }
    
    /**
     * getUsersInCollection
     * @param coll_id
     * @return
     * @throws SQLException
     * @author Hcyue
     */
    public List<UserInfo> getUsersInCollection(int coll_id) throws SQLException {
        ResultSet rs = db.query("SELECT * FROM users WHERE user_id IN (SELECT user_id FROM collection_entry WHERE collection_id = "+ coll_id +")");
        ArrayList<UserInfo> res = new ArrayList<>();
        while (rs.next()) {
            res.add(new UserInfo(rs));
        }
        rs.close();
        return res;
    }

    /**
     * createUser
     * 创建对象
     * @param passwd
     * @param nick
     * @param avatar
     * @return
     * @throws SQLException
     */
    public UserInfo createUser(String passwd, String nick, int avatar) throws SQLException {
        String sql = String.format("INSERT INTO users (nickname, password, avatar) VALUES ('%s', '%s', %d)", nick, passwd, avatar);
        int res = db.insertAndGet(sql);
        return getUserByJK(res);
    }
    
    /**
     * removeUser
     * 删除对象
     * @param jk
     * @return
     * @throws SQLException
     */
    public int removeUser(int jk) throws SQLException {
        String sql = String.format("DELETE FROM users WHERE user_id=%d", jk);
        int res = db.update(sql);
        return res;
    }
    
    /**
     * 
     * @param jk
     * @param list_name
     * @return 1 不存在这个人
     * @return 2 已经有了
     * @return 0 成功
     * @throws Exception 
     * @author He11o_Liu
     */
    public int add_friend(int add_jk, int own_jk, String list_name) throws Exception{
    	//check add_jk
    	UserInfo dest = getUserByJK(add_jk);
    	if (dest == null){
    		//不存在这个人
    		return 1;
    	}
    	
        CollectionModel collectionModel = new CollectionModel(db);
        List<CollectionInfo> coll = collectionModel.getColletionsByJK(own_jk);
        CollectionInfo collection;
        for(int j = 0; j<coll.size();j++){
        	collection = coll.get(j);
        	for(int i = 0; i<collection.getMembers().size(); i++){
        		if(collection.getMembers().get(i).getJKNum()==add_jk)
        			return 2;
			}
        }
        
        
        
        
        
    	//check list_name
    	
    	//find coll_id
    	CollectionInfo ci = collectionModel.getCollectionByNameAndOwner(list_name, own_jk);
    	if(ci == null){
        	//create collection
        	ci = collectionModel.createCollection(own_jk, list_name);
        }
    	if(ci != null){
    		// this part can be done in client
    		// add to collection
    		collectionModel.addUserToCollection(add_jk, ci.getId());
    	}
    	return 0;
    }
    
    /*
    public static void main(String args[]) throws SQLException {
//        UserModel model = new UserModel(DBConnection.getInstance());
//        UserInfo newuser = model.createUser("123", "Zhuzi", 0);
//        System.out.println(newuser.getJKNum()+newuser.getNickName());
//        UserInfo user = model.getUserByJK(0);
//        
//        String userName = user.getNickName();
//		int pic = user.getAvatar();
//		String[] listName = user.getListName();
//		byte listCount = user.getCollectionCount();
//		byte[] bodyCount = user.getBodyCount();
//		String[][] bodyName = user.getBodyName();
//		
//		for(int i = 0; i<listCount;i++){
//			System.out.println(listName[i]);
//			for(int j = 0; j<bodyCount[i];j++){
//				System.out.println(bodyName[i][j]);
//			}
//		}
    	
    	
    	
        
//        List<UserInfo> testlist = model.getUsersInCollection(0);
//        for(int i = 0; i < testlist.size();i++){
//        	System.out.println(testlist.get(i).getNickName());
//        }
        
        
        
        //UserInfo user = model.createUser("123", "He11o_Liu", 001);
        //int jk = user.getJKNum();
        //UserInfo res = model.getUserByJK(0);
        //if(res!= null) System.out.printf("jk: %d nick: %s\n", res.getJKNum(), res.getNickName());
        //else System.out.println("Not in the database");
        //System.out.println(model.userAuthorization(0,"123"));
        //model.removeUser(jk);
    }*/
    
    
    public static void main(String args[]) throws SQLException {
        DBConnection db = DBConnection.getInstance();
        
        //UserModel userModel = new UserModel(db);
        CollectionModel collectionModel = new CollectionModel(db);
        //UserInfo user = userModel.getUserByJK(0);
        /*
        List<CollectionInfo>  coll = collectionModel.getColletionsByJK(0);
		List<UserInfo> testlist;
        for(int j = 0; j<coll.size();j++){
        	try {
        		System.out.println(coll.get(j).toString());
				testlist = coll.get(j).getMembers();
				for(int i = 0; i < testlist.size();i++){
		        	System.out.println(testlist.get(i).getNickName());
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
        }*/
     
        CollectionInfo ci = collectionModel.getCollectionByNameAndOwner("我的好友", 6);
        List<UserInfo> testlist;
        if(ci == null){
        	System.out.println("No such list");
        }
		try {
			testlist = ci.getMembers();
			System.out.println(ci.getId()+"  "+ci.getName());
		    for(int i = 0; i< testlist.size();i++){
		        System.out.println(testlist.get(i).getNickName());
		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
